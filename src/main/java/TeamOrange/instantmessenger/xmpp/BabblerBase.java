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

	private MessageListener messageListener;
	private StatusListener statusListener;
	private RosterListener rosterListener;

	private StatusEvent statusEvent;

	//public ContactManager contactManager;
	private MessageManager messageManager;
	private AccountManager accountManager;
	private ContactManager contactManager; // TODO: update contact manager to stop using static methods
	private ConnectionManager connectionManager;
	private MucManager mucManager;

	public BabblerBase(String hostName, MessageListener messageListener,
			StatusListener presenceListener, RosterListener rosterListener) {
		this.hostName = hostName;
		this.messageListener = messageListener;
		this.statusListener = statusListener;
		this.rosterListener = rosterListener;
		this.messageManager = new MessageManager();
		this.accountManager = new AccountManager();
		this.contactManager = new ContactManager();
		this.connectionManager = new ConnectionManager();
		this.mucManager = new MucManager();
	}

	// MessageManager
	public void requestCreateChatSession(AppJid to, String thread){
		messageManager.requestCreateChatSession(client, to, thread);
	}

	public void requestContactAdd(AppJid to){
		messageManager.requestContactAdd(client, to);
	}

	public void alertUserOfContactRequestResponse(AppJid jid, boolean accepted){
		messageManager.alertUserOfContactRequestResponse(client, jid, accepted);
	}

	public AppChatSession createChatSession(AppJid to){
		ChatSession chatSession = messageManager.createChatSession(client, to);
		XmppChatSession xmppChatSession = new XmppChatSession(chatSession);
		AppChatSession appChatSession = new AppChatSession(xmppChatSession);
		return appChatSession;
	}

	public AppChatSession createChatSessionWithGivenThread(AppJid to, String thread){
		ChatSession chatSession = messageManager.createChatSessionWithGivenThread(client, to, thread);
		XmppChatSession xmppChatSession = new XmppChatSession(chatSession);
		AppChatSession appChatSession = new AppChatSession(xmppChatSession);
		return appChatSession;
	}

	// ConnectionMannager
	public void setupConnection(){
		client = connectionManager.setupConnection(hostName, this);
	}

	public void connect() throws ConfideXmppException{
		connectionManager.connect(client);
		client.addInboundPresenceListener(presenceListener->newPresence(presenceListener));
	}

	public void close() throws ConfideXmppException{
		connectionManager.close(client);
	}

	// AccountMannager
	public AppJid login(String userName, String password)
			throws ConfideXmppException {
		Jid jid = accountManager.login(client, userName, password);
		AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

	public void logout() throws ConfideXmppException {
		accountManager.logout(client);
	}

	/**
	 * Create User on Server
	 * @param userName
	 * @param password
	 */
	public void createUser(String userName, String password){
		accountManager.createUser(client, userName, password);
	}

	// Contact Manager
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

		ContactManager.addContact(client, contact);
	}

	/**
	 * Remove Contact from User
	 * @param contact
	 */
	public void removeContact(String contact) {
		ContactManager.removeContact(client, contact);
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

		return ContactManager.getContacts(client);
	}

	public LinkedList<AppUser> getContactsAsAppUsers() {
		// spin until in correct state
		while(client.getStatus() != XmppSession.Status.AUTHENTICATED) {
			try { Thread.sleep(50); }
			catch (InterruptedException e) { }
		}

		return ContactManager.getContactsAsAppUsers(client);
	}

	/**
	 * List of ContactGroups
	 * @return
	 */
	public LinkedList<String> getGroups() {
		return ContactManager.getContactGroups(client);
	}

	//listeners
	public void newPresence(PresenceEvent presenceEvent){

	    Presence presence = presenceEvent.getPresence();
	    Contact contact = client.getManager(RosterManager.class).getContact(presence.getFrom());

	    // TODO: added temporarily
	    if(contact == null || contact == null)
	    	return;

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

	public void newMessage(MessageEvent messageEvent){
		// fired whenever a message is received
		// handle anything that should be handeled here

		// pass it onto App
		messageListener.message(messageManager.inboundMessageEventToAppMessage(messageEvent));
		messageEvent.consume();
	}

	public void newRoster(RosterEvent rosterEvent){
		rosterListener.roster();
	}

	public void setOnStatusEvent(StatusEvent statusEvent){
		this.statusEvent = statusEvent;
	}


	public void requestSubsription(String jid, String message) {
    	client.getManager(PresenceManager.class).requestSubscription(Jid.of(jid), message);
	}

	// MucManager
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

	public Collection<Occupant> getOccupants(String roomID){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		Collection<Occupant> occupants = mucManager.getOccupants(client, roomJid);
		return occupants;
	}

	public AppMuc createAndOrEnterRoom(String roomID, String nickname){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		AppMuc muc = mucManager.createAndOrEnterRoom(client, this, roomJid, nickname);
		return muc;
	}

	public void sendGroupMessage(String roomID, String message){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		mucManager.sendMessage(client, roomJid, message);
	}

	public void leaveRoom(String roomID){
		Jid roomJid = Jid.of(roomID + "@conference.teamorange.space");
		mucManager.leaveRoom(client, roomJid);
	}

}
