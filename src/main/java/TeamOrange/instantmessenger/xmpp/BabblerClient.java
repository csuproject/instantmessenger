package TeamOrange.instantmessenger.xmpp;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.StanzaException;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.roster.RosterManager;

public class BabblerClient {

	private String hostName;
	private XmppClient client;

	private MessageListener messageListener;
	private PresenceListener presenceListener;
	private RosterListener rosterListener;

	public BabblerClient(String hostName, MessageListener messageListener, PresenceListener presenceListener, RosterListener rosterListener) {
		this.hostName = hostName;
		this.messageListener = messageListener;
		this.presenceListener = presenceListener;
		this.rosterListener = rosterListener;
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

	// where to put login, logout, createUser ???
	public boolean login(String userName, String password) {
		// TODO: throw exceptions so the different failure reasons can be handled
		try{
    		client.login(userName, password, null);
    	} catch(AuthenticationException e){
    		return false;
    	} catch(StreamErrorException e){
    		return false;
    	} catch(StreamNegotiationException e){
    		return false;
    	} catch(NoResponseException e){
    		return false;
    	} catch(StanzaException e){
    		return false;
    	} catch(XmppException e){
    		return false;
    	}
		return true;
	}

	public boolean logout(){
		try {
			// TODO: not sure if this logs other user out
			client.loginAnonymously();
		} catch(AuthenticationException e){
    		return false;
    	} catch(StreamErrorException e){
    		return false;
    	} catch(StreamNegotiationException e){
    		return false;
    	} catch(NoResponseException e){
    		return false;
    	} catch(StanzaException e){
    		return false;
    	} catch(XmppException e){
    		return false;
    	}
		return true;
	}

	public boolean createUser(String userName, String password) {
		// TODO: how to tell if this failed?
		Registration registration = Registration.builder()
    			.username(userName)
    			.password(password)
    			.build();
    	RegistrationManager registrationManager = client.getManager(RegistrationManager.class);
    	registrationManager.register(registration);

    	return true;
	}

	// these will be passed Babbler constructs, do what they need to, and then alert the listeners from App using our own constructs, so that Babbler never goes past this point
	// But maybe these should be moved. e.g. newRoster into RosterManager, newMessage into MessageManager.
	public void newPresence(){
		presenceListener.presence();
	}

	public void newMessage(){
		messageListener.message();
	}

	public void newRoster(){
		rosterListener.roster();
	}

}
