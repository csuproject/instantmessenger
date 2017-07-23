package TeamOrange.instantmessenger.xmpp;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.LoginEvent;
import TeamOrange.instantmessenger.lambda.StatusEvent;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppOccupant;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.UserStatus;
import exceptions.ConfideAuthenticationException;
import exceptions.ConfideXmppException;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSession;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.PresenceEvent;
import rocks.xmpp.core.stanza.StanzaException;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.disco.model.items.Item;
import rocks.xmpp.extensions.muc.Occupant;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.chat.ChatSession;
import rocks.xmpp.im.roster.RosterEvent;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;
import rocks.xmpp.im.subscription.PresenceManager;


public class BabblerBase {

	private String hostName;
	private XmppClient client;

	// Listeners
	private MessageListener messageListener;
	private StatusListener statusListener;
	private RosterListener rosterListener;

	private StatusEvent statusEvent;

	// Managers
	private MessageManager messageManager;
	private AccountManager accountManager;
	private ContactManager contactManager;
	private ConnectionManager connectionManager;
	private MucManager mucManager;


	public BabblerBase(String hostName, MessageListener messageListener,
			StatusListener presenceListener, RosterListener rosterListener) {
		this.hostName = hostName;

		this.messageListener = messageListener;
		this.statusListener = statusListener; // TODO: what is happening here?
		this.rosterListener = rosterListener;

		this.messageManager = new MessageManager();
		this.accountManager = new AccountManager();
		this.contactManager = new ContactManager();
		this.connectionManager = new ConnectionManager();
		this.mucManager = new MucManager();
	}

	//////////////////////////////////////////////////////////////////////////////
	//------------------------------MessageManager------------------------------//
	//////////////////////////////////////////////////////////////////////////////

	/**
	 * Requests a contact-add with the user who has the given Jid
	 * @param to the Jid of the user
	 */
	public void requestContactAdd(AppJid to){
		messageManager.requestContactAdd(client, to);
	}

	/**
	 * Sends a response to a contact-add request.
	 * @param jid the jid of the user to respond to
	 * @param accepted a boolean value. True to accept, False to decline
	 */
	public void alertUserOfContactRequestResponse(AppJid jid, boolean accepted){
		messageManager.alertUserOfContactRequestResponse(client, jid, accepted);
	}

	/**
	 * Creates a chat session with the user whos jid is held in to
	 * @param to the Jid of the user to create a chat session with
	 * @return An AppChatSession representing the chat session created
	 */
	public AppChatSession createChatSession(AppJid to){
		ChatSession chatSession = messageManager.createChatSession(client, to);
		XmppChatSession xmppChatSession = new XmppChatSession(chatSession);
		AppChatSession appChatSession = new AppChatSession(xmppChatSession);
		return appChatSession;
	}

	/**
	 * Creates a chat session with the user whos jid is held in to, but with the thread provided
	 * @param to the Jid of the user to create a chat session with
	 * @param thread the thread to give the chat session
	 * @return An AppChatSession representing the chat session created
	 */
	public AppChatSession createChatSessionWithGivenThread(AppJid to, String thread){
		ChatSession chatSession = messageManager.createChatSessionWithGivenThread(client, to, thread);
		XmppChatSession xmppChatSession = new XmppChatSession(chatSession);
		AppChatSession appChatSession = new AppChatSession(xmppChatSession);
		return appChatSession;
	}

	//////////////////////////////////////////////////////////////////////////////
	//------------------------------ConnectionMannager------------------------------//
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets up the connection, and adds listeners
	 * Sets client to be the XmppSession representing the connection
	 */
	public void setupConnection(){
		client = connectionManager.setupConnection(hostName, this);
	}

	/**
	 * Actually connects
	 * @throws ConfideXmppException
	 */
	public void connect() throws ConfideXmppException{
		connectionManager.connect(client);
		client.addInboundPresenceListener(presenceListener->newPresence(presenceListener)); // TODO: What is happening here ??
	}

	/**
	 * Closes the connection
	 * @throws ConfideXmppException
	 */
	public void close() throws ConfideXmppException{
		connectionManager.close(client);
	}

	//////////////////////////////////////////////////////////////////////////////
	//------------------------------AccountMannager------------------------------//
	//////////////////////////////////////////////////////////////////////////////

	/**
	 * Logs in with the provided username and password.
	 * @param userName the username
	 * @param password the password
	 * @return an AppJid representing the user who just logged in
	 * @throws ConfideXmppException
	 */
	public AppJid login(String userName, String password)
			throws ConfideXmppException {
		Jid jid = accountManager.login(client, userName, password);
		AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

//	/**
//	 * logs out by logging in anonymously
//	 * @throws ConfideXmppException
//	 */
//	public void logout() throws ConfideXmppException {
//		accountManager.logout(client);
//	}

	/**
	 * Create User on Server
	 * @param userName
	 * @param password
	 */
	public void createUser(String userName, String password){
		accountManager.createUser(client, userName, password);
	}

	//////////////////////////////////////////////////////////////////////////////
	//------------------------------ContactManager------------------------------//
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * Add Contact to User
	 * @param contact
	 */
	public void addContact(String contact) {
		// spin until in correct state
		while(client.getStatus() != XmppSession.Status.AUTHENTICATED) {
			try { Thread.sleep(50); }
			catch (InterruptedException e) { }
		}

		contactManager.addContact(client, contact);
	}

	/**
	 * Remove Contact from User
	 * @param contact
	 */
	public void removeContact(String contact) {
		contactManager.removeContact(client, contact);
	}

	/**
	 * Blocks the user
	 */
	public void blockUser(String user){
		contactManager.blockUser(client, user);
	}

	public void printBlockList(){
		contactManager.printBlockList(client);
	}

	/**
	 * List of Contacts
	 * @return
	 */
	public LinkedList<String> getContacts() {
		// spin until in correct state
		while(client.getStatus() != XmppSession.Status.AUTHENTICATED) {
			try { Thread.sleep(50); }
			catch (InterruptedException e) { }
		}

		return contactManager.getContacts(client);
	}

	/**
	 * Gets a list of contacts, as AppUsers rather than Strings
	 * @return LinkedList of AppUser which represents the logged in users contacts
	 */
	public LinkedList<AppUser> getContactsAsAppUsers() {
		// spin until in correct state
		while(client.getStatus() != XmppSession.Status.AUTHENTICATED) {
			try { Thread.sleep(50); }
			catch (InterruptedException e) { }
		}

		return contactManager.getContactsAsAppUsers(client);
	}

	/**
	 * List of ContactGroups
	 * @return
	 */
	public LinkedList<String> getGroups() {
		return contactManager.getContactGroups(client);
	}

	public void setOnStatusEvent(StatusEvent statusEvent){ // TODO: is this ever called?
		this.statusEvent = statusEvent;
	}


	public void requestSubsription(String jid, String message) {
    	client.getManager(PresenceManager.class).requestSubscription(Jid.of(jid), message);
	}

	//////////////////////////////////////////////////////////////////////////////
	//------------------------------MucManager------------------------------//
	//////////////////////////////////////////////////////////////////////////////

	/*public void getEnteredRooms(AppJid appJid){
		Jid jid = JidUtilities.jidFromAppJid(appJid);
		System.out.println(jid);
		List<Item> enteredRooms = mucManager.getEnteredRooms(client, jid);
		if(enteredRooms != null){
		for(Item i : enteredRooms){
			System.out.println("entered room( id: " + i.getId() + "  name: " + i.getName() + " )\n" );
		}
		}
	}*/

//	public void getMembers(String roomID, String nick){
//		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
//		mucManager.getMembers(client, roomJid, nick);
//	}

	/**
	 * Gets a LinkedList of Occupants of the chat room, that is people currently in the chat room.
	 * assumed "conference.teamorange.space" for the  service
	 * @param roomID the id of the room
	 * @return a LinkedList of AppOccupant representing each person int the room
	 */
	public LinkedList<AppOccupant> getOccupants(String roomID){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		Collection<Occupant> occupants = mucManager.getOccupants(client, roomJid);
		LinkedList<AppOccupant> appOccupants = new LinkedList();
		for(Occupant o : occupants){
			AppOccupant ao = new AppOccupant(o.getNick());
			appOccupants.add(ao);
		}
		return appOccupants;
	}

	/**
	 * Creates a room with the given id, if it doesnt already exist.
	 * Enters the room with the given nickname
	 * Sets up a message listener and an occupant listener to be connected to the AppMuc object.
	 * Adds the occupants of the room to the AppMuc object.
	 * assumed "conference.teamorange.space" for the  service
	 * @param roomID the id of the room
	 * @param nickname the nickname to enter with
	 * @return an AppMuc object that represents the group chat
	 */
	public AppMuc createAndOrEnterRoom(String roomID, String nickname){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		AppMuc muc = mucManager.createAndOrEnterRoom(client, this, roomJid, nickname);
		return muc;
	}

	/**
	 * Sends a group message to the room with the given id
	 * assumed "conference.teamorange.space" for the  service
	 * @param roomID the id of the room
	 * @param message the message to send
	 */
	public void sendGroupMessage(String roomID, String message){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		mucManager.sendMessage(client, roomJid, message);
	}

	/**
	 * Leaves the room with the given id
	 * assumed "conference.teamorange.space" for the  service
	 * @param roomID the id of the room to leave
	 */
	public void leaveRoom(String roomID){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		mucManager.leaveRoom(client, roomJid);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//------------------------------Listeners (Message, Presence, Roster)------------------------------//
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This function is called when there is a new presence event
	 * handles the event
	 * consumes the event
	 * @param presenceEvent represents the presence event
	 */
	public void newPresence(PresenceEvent presenceEvent){
	    Presence presence = presenceEvent.getPresence();
	    Contact contact = client.getManager(RosterManager.class).getContact(presence.getFrom());

	    // Available
	    if (presence.getType() == null) {
	    	statusEvent.status(new UserStatus(
	    			presence.getFrom().getLocal()+"@"+presence.getFrom().getDomain(),"AVAILABLE"));
	    }

	    // Unavailable
	    if (presence.getType() == Presence.Type.UNAVAILABLE) {
	    	statusEvent.status(new UserStatus(
	    			presence.getFrom().getLocal()+"@"+presence.getFrom().getDomain(),"UNAVAILABLE"));
	    }

	    // Subscribe
	    if (presence.getType() == Presence.Type.SUBSCRIBE) {
	    	client.getManager(PresenceManager.class).approveSubscription(presence.getFrom());
	    }

	    if (contact != null) {
	    	statusEvent.status(new UserStatus(presence.getId(),presence.getStatus()));
	    }

	    presenceEvent.consume();
	}

	/**
	 * This function is called when there is a new message event
	 * Passes it through to messageListener (which is in App) as an AppMessage
	 * Consumes the event
	 * @param messageEvent represents the message event
	 */
	public void newMessage(MessageEvent messageEvent){
		// fired whenever a message is received
		// handle anything that should be handeled here

		// pass it onto App
		messageListener.message(messageManager.inboundMessageEventToAppMessage(messageEvent));
		messageEvent.consume();
	}

	/**
	 * This function is called when there is a new roster event
	 * @param rosterEvent represents the roster event
	 */
	public void newRoster(RosterEvent rosterEvent){
		rosterListener.roster();
	}

}
