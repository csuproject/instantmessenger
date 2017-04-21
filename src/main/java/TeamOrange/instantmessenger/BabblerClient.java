package TeamOrange.instantmessenger;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.im.roster.RosterManager;

public class BabblerClient extends Client {

	private XmppClient client;

	public BabblerClient(String hostName, MessageListener messageListener, PresenceListener presenceListener, RosterListener rosterListener) {
		super(hostName, messageListener, presenceListener, rosterListener);
	}

	@Override
	public boolean setupConnection(){
		TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
    		    .hostname(hostName)
    		    .port(5222)
    		    .secure(false)
    		    .build();

    	client = XmppClient.create(hostName, tcpConfiguration);

    	client.addInboundPresenceListener(e -> newPresence() );
    	client.addInboundMessageListener(e -> newMessage() );
    	client.getManager(RosterManager.class).addRosterListener(e -> newRoster() );

    	// TODO: check if this worked
    	return true;
	}

	@Override
	public boolean connect() {
		try{
    		client.connect();
    	} catch(XmppException e){
    		return false;
    	}
		return true;
	}

	@Override
	public boolean login(String userName, String password) {
		// TODO: throw exceptions so the different failure reasons can be handled
		try{
    		client.login(userName, password, null);
    	} catch(AuthenticationException e){
    		return false;
    	} catch(XmppException e){
    		return false;
    	}
		return true;
	}

	@Override
	public boolean logout(){
		try {
			// TODO: not sure if this logs other user out
			client.loginAnonymously();
		} catch (XmppException e) {
			return false;
		}
		return true;
	}

	@Override
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

	@Override
	public boolean close() {
		try{
    		client.close();
    	} catch(XmppException e){
    		return false;
    	}
		return true;
	}

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
