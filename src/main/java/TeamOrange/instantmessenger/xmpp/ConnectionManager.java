package TeamOrange.instantmessenger.xmpp;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import TeamOrange.instantmessenger.models.AppConnection;
import exceptions.ConfideConnectionException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.ReconnectionStrategy;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSessionConfiguration;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.IQ;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.core.stream.model.StreamElement;
import rocks.xmpp.extensions.offline.OfflineMessageManager;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.subscription.PresenceManager;
import rocks.xmpp.util.concurrent.AsyncResult;

public class ConnectionManager {

	/**
	 * Configures and creates the connection.
	 * Adds presence, message, and roster listeners
	 * @param hostName
	 * @param babblerBase a reference back to BabblerBase so that the listeners can be linked back to it
	 * @return an XmppClient object, used to communicate witht the server
	 */
	public XmppClient setupConnection(String hostName, BabblerBase babblerBase){
		TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
    		    .hostname(hostName)
    		    .port(5222)
    		    .secure(false)
    		    .build();

		XmppSessionConfiguration configuration = XmppSessionConfiguration.builder()
//				.reconnectionStrategy(ReconnectionStrategy.alwaysAfter(Duration.ofSeconds(5)))
				.reconnectionStrategy(ReconnectionStrategy.none())
				.build();

    	XmppClient client = XmppClient.create(hostName, configuration, tcpConfiguration);

    	client.addInboundPresenceListener( presenceEvent->babblerBase.newPresence(presenceEvent) );
    	client.addInboundMessageListener( messageEvent->babblerBase.newMessage(messageEvent) );
    	client.getManager(RosterManager.class).addRosterListener( rosterEvent->babblerBase.newRoster(rosterEvent) );
    	client.addConnectionListener( connectionEvent->babblerBase.newConnectionEvent(connectionEvent) );

    	return client;
	}

	/**
	 * Connects to the server
	 * @param client
	 * @throws ConfideXmppException
	 */
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

	/**
	 * Closes the connection
	 * @param client
	 * @throws ConfideXmppException
	 */
	public void close(XmppClient client) throws ConfideXmppException {
		try{
    		client.close();
    	} catch(XmppException e){
    		throw new ConfideXmppException();
    	}

	}

	public int getConnectionState(XmppClient client){
		int answer = AppConnection.NOT_CONNECTED;
		if(client.isConnected()){
			answer = AppConnection.CONNECTED;
		}
		return answer;
	}


}
