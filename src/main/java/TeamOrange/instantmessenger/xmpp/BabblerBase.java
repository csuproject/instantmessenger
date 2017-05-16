package TeamOrange.instantmessenger.xmpp;

//<<<<<<< HEAD
import java.util.LinkedList;

//=======
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppJid;
//>>>>>>> refs/remotes/origin/E2_2
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
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
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.chat.ChatSession;
import rocks.xmpp.im.roster.RosterEvent;
import rocks.xmpp.im.roster.RosterManager;

public class BabblerBase {

	private String hostName;
	private XmppClient client;

	private MessageListener messageListener;
	private PresenceListener presenceListener;
	private RosterListener rosterListener;

	//public ContactManager contactManager;
	private MessageManager messageManager;
	private AccountManager accountManager;
	private ContactManager contactManager; // TODO: update contact manager to stop using static methods
	private PresenceManager presenceManager; // TODO: update presence manager to stop using static methods
	private ConnectionManager connectionManager;

	public BabblerBase(String hostName, MessageListener messageListener,
			PresenceListener presenceListener, RosterListener rosterListener) {
		this.hostName = hostName;
		this.messageListener = messageListener;
		this.presenceListener = presenceListener;
		this.rosterListener = rosterListener;
		this.messageManager = new MessageManager();
//<<<<<<< HEAD
		//this.contactManager = new ContactManager();
//=======
		this.accountManager = new AccountManager();
		this.contactManager = new ContactManager();
		this.presenceManager = new PresenceManager();
		this.connectionManager = new ConnectionManager();
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
//>>>>>>> refs/remotes/origin/E2_2
	}

	// ConnectionMannager
	public void setupConnection(){
		client = connectionManager.setupConnection(hostName, this);
	}

	public void connect() throws ConfideXmppException{
		connectionManager.connect(client);
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
		//this is called when a new PresenceEvent occurs, and passed a PresenceEvent object by babbler
		// first we will handle anything that needs to happen here
		//...

		//then we will pass the information out to the rest of our application,
		//but to hide the babbler dependency, we will construct a AppPresence object with the information we need,
		//and pass that on
		//but first we need to decide what information we need from a Presence/PresenceEvent,
		//and add that to the AppPresence class, then extract it here, create an AppPresence object with it,
		//and then pass it on.
		Presence presence = presenceEvent.getPresence();
		AppPresence appPresence = new AppPresence();
		presenceListener.presence(appPresence); // calling this sends it to the presenceListener function in App
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

}
