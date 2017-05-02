package TeamOrange.instantmessenger.xmpp;

import exceptions.ConfideAuthenticationException;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.StanzaException;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;

public class AccountManager {

	public Jid login(XmppClient client, String userName, String password) throws ConfideAuthenticationException {
		// TODO: throw exceptions so the different failure reasons can be handled
		try{
    		client.login(userName, password, null);
    	} catch(AuthenticationException e){
    		throw new ConfideAuthenticationException();
    	} catch(StreamErrorException e){

    	} catch(StreamNegotiationException e){

    	} catch(NoResponseException e){

    	} catch(StanzaException e){

    	} catch(XmppException e){

    	}
		return Jid.of(userName, "teamorange.space", null);
	}

	public void logout(XmppClient client){
		try {
			// TODO: not sure if this logs other user out
			client.loginAnonymously();
		} catch(AuthenticationException e){

    	} catch(StreamErrorException e){

    	} catch(StreamNegotiationException e){

    	} catch(NoResponseException e){

    	} catch(StanzaException e){

    	} catch(XmppException e){

    	}
	}

	public void createUser(XmppClient client, String userName, String password) {
		// TODO: how to tell if this failed?
		Registration registration = Registration.builder()
    			.username(userName)
    			.password(password)
    			.build();
    	RegistrationManager registrationManager = client.getManager(RegistrationManager.class);
    	registrationManager.register(registration);
	}

}
