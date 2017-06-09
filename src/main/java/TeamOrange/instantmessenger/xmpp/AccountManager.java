package TeamOrange.instantmessenger.xmpp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import exceptions.ConfideAuthenticationException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.StanzaException;
import rocks.xmpp.core.stanza.model.IQ;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.offline.OfflineMessageManager;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.util.concurrent.AsyncResult;

public class AccountManager {

	public Jid login(XmppClient client, String userName, String password)
			throws ConfideXmppException {
		try{
    		client.login(userName, password, null);
    	} catch(AuthenticationException e){
    		throw new ConfideAuthenticationException();
    	} catch(NoResponseException e){
    		throw new ConfideNoResponseException();
    	}
//		catch(StreamErrorException e){
//
//    	} catch(StreamNegotiationException e){
//
//    	} catch(StanzaException e){
//
//    	}
		catch(XmppException e){
			throw new ConfideXmppException();
    	}
		return Jid.of(userName, "teamorange.space", null);
	}

	public void logout(XmppClient client) throws ConfideXmppException {
		try {
			// TODO: not sure if this logs other user out
			client.loginAnonymously();
		} catch(AuthenticationException e){
			throw new ConfideAuthenticationException();
    	} catch(NoResponseException e){
    		throw new ConfideNoResponseException();
    	}
//		catch(StreamErrorException e){
//
//    	} catch(StreamNegotiationException e){
//
//    	} catch(StanzaException e){
//
//    	}
		catch(XmppException e){
			throw new ConfideXmppException();
    	}
	}

	public void createUser(XmppClient client, String userName, String password) {
		// TODO: how to tell if this failed?
		Registration registration = Registration.builder()
    			.username(userName)
    			.password(password)
    			.build();
//		if( registration.isRegistered() ){
//			System.out.println("already registered");
//		}

		RegistrationManager registrationManager = client.getManager(RegistrationManager.class);
    	registrationManager.register(registration);
	}

}
