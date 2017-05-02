package TeamOrange.instantmessenger.xmpp;

import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.ConnectionException;
import rocks.xmpp.core.session.NoResponseException;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stream.StreamErrorException;
import rocks.xmpp.core.stream.StreamNegotiationException;
import rocks.xmpp.im.roster.RosterManager;

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

	public void connect(XmppClient client) {
		try{
    		client.connect();
    	} catch(ConnectionException e){

    	} catch(StreamErrorException e){

    	} catch(StreamNegotiationException e){

    	} catch(NoResponseException e){

    	} catch(XmppException e){

    	} catch(IllegalStateException e){

    	}

	}

	public void close(XmppClient client) {
		try{
    		client.close();
    	} catch(XmppException e){

    	}

	}


}
