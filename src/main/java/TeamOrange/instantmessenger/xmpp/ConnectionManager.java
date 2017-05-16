package TeamOrange.instantmessenger.xmpp;

import java.util.concurrent.ExecutionException;

import exceptions.ConfideConnectionException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.model.IQ;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.extensions.offline.OfflineMessageManager;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.util.concurrent.AsyncResult;

public class ConnectionManager {

	public XmppClient setupConnection(String hostName, BabblerBase babblerClient){
		TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
    		    .hostname(hostName)
    		    .port(5222)
    		    .secure(false)
    		    .build();

    	XmppClient client = XmppClient.create(hostName, tcpConfiguration);

    	client.addInboundPresenceListener( presenceEvent->babblerClient.newPresence(presenceEvent) );
    	client.addInboundMessageListener( messageEvent->babblerClient.newMessage(messageEvent) );
    	client.getManager(RosterManager.class).addRosterListener( rosterEvent->babblerClient.newRoster(rosterEvent) );

    	// TODO: check if this worked
    	return client;
	}

	public void connect(XmppClient client) throws ConfideXmppException {
		try{
    		client.connect();
    	} catch(ConnectionException e){
    		throw new ConfideConnectionException();
    	}
//		catch(StreamErrorException e){
//
//    	} catch(StreamNegotiationException e){
//
//    	} catch(IllegalStateException e){
//
//    	}
		catch(NoResponseException e){
			throw new ConfideNoResponseException();
    	}
		catch(XmppException e){
			throw new ConfideXmppException();
    	}

	}

	public void close(XmppClient client) throws ConfideXmppException {
		try{
    		client.close();
    	} catch(XmppException e){
    		throw new ConfideXmppException();
    	}

	}


}
