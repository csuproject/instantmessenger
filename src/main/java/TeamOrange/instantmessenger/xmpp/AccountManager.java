package TeamOrange.instantmessenger.xmpp;

import exceptions.ConfideAuthenticationException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.sasl.AuthenticationException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.register.RegistrationManager;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.util.concurrent.AsyncResult;

/**
 * Handles the xmpp account management.
 *
 */
public class AccountManager {

	/**
	 * Logs in with the given username and password
	 * @param client
	 * @param userName the username
	 * @param password the password
	 * @return the Jid of the user who just logged in
	 * @throws ConfideXmppException
	 */
	public Jid login(XmppClient client, String userName, String password)
			throws ConfideXmppException {
		try{
			client.login(userName, password, null);
	    	client.sendPresence( new Presence() );
    	} catch(AuthenticationException e){
    		System.out.println("AuthenticationException ------");
    		throw new ConfideAuthenticationException();
    	} catch(NoResponseException e){
    		System.out.println("NoResponseException");
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
			System.out.println("xmppException");
			throw new ConfideXmppException();
    	}
		return Jid.of(userName, "teamorange.space", null);
	}

//	/**
//	 * Logs out by logging in annonymously
//	 * @param client
//	 * @throws ConfideXmppException
//	 */
//	public void logout(XmppClient client) throws ConfideXmppException {
//		try {
//			// TODO: not sure if this logs other user out
//			client.loginAnonymously();
//		} catch(AuthenticationException e){
//			throw new ConfideAuthenticationException();
//    	} catch(NoResponseException e){
//    		throw new ConfideNoResponseException();
//    	}
////		catch(StreamErrorException e){
////
////    	} catch(StreamNegotiationException e){
////
////    	} catch(StanzaException e){
////
////    	}
//		catch(XmppException e){
//			throw new ConfideXmppException();
//    	}
//	}

	/**
	 * Creates a user with the given username and password
	 * @param client
	 * @param userName the username
	 * @param password the password
	 * @throws XmppException
	 */
	public void createUser(XmppClient client, String userName, String password) throws XmppException {
		// TODO: how to tell if this failed?
		Registration registration = Registration.builder()
    			.username(userName)
    			.password(password)
    			.build();

		RegistrationManager registrationManager = client.getManager(RegistrationManager.class);
    	AsyncResult<Void> result = registrationManager.register(registration);
    	result.getResult();
	}

}
