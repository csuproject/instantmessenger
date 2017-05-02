package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppUser;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.StanzaException;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.roster.RosterEvent;
import rocks.xmpp.im.roster.RosterManager;

public class BabblerBase {

	private String hostName;
	private XmppClient client;

	private MessageListener messageListener;
	private PresenceListener presenceListener;
	private RosterListener rosterListener;

	private MessageManager messageManager;

	public BabblerBase(String hostName, MessageListener messageListener, PresenceListener presenceListener, RosterListener rosterListener) {
		this.hostName = hostName;
		this.messageListener = messageListener;
		this.presenceListener = presenceListener;
		this.rosterListener = rosterListener;
		this.messageManager = new MessageManager();
	}

	// ConnectionMannager
	public void setupConnection(){
		client = ConnectionManager.setupConnection(hostName, this);
	}

	public void connect(){
		ConnectionManager.connect(client);
	}

	public void close(){
		ConnectionManager.close(client);
	}

	// AccountMannager
	public AppJid login(String userName, String password){
		Jid jid = AccountManager.login(client, userName, password);
		AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

	public void logout(){
		AccountManager.logout(client);
	}

	public void createUser(String userName, String password){
		AccountManager.createUser(client, userName, password);
	}

	//listeners
	// these will be passed Babbler constructs, do what they need to, and then alert the listeners from App using our own constructs, so that Babbler never goes past this point
	// But maybe these should be moved. e.g. newRoster into RosterManager, newMessage into MessageManager.
	public void newPresence(){
		presenceListener.presence();
	}

	public void newMessage(MessageEvent messageEvent){
		// fired whenever a message is received or sent
		// handle anything that should be handeled here

		// pass it onto App
		messageListener.message(messageManager.messageEventToAppMessage(messageEvent));
		messageEvent.consume();
	}

	public void newRoster(RosterEvent rosterEvent){

		rosterListener.roster();
	}

}
