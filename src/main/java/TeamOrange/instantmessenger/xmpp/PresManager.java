package TeamOrange.instantmessenger.xmpp;

import java.util.function.Consumer;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.PresenceEvent;
import rocks.xmpp.core.stanza.model.Presence;

public class PresManager {

	/**
	 * Add Contact request listener.
	 * @param client
	 * @param event
	 */
	public static void addListenerPresence(XmppClient client, Consumer<PresenceEvent> presenceListener) {
		
		client.addInboundPresenceListener(presenceListener);
	}
	
	/**
	 * Add Contact request listener.
	 * @param client
	 * @param event
	 */
	public static void removeListenerPresence(XmppClient client, Consumer<PresenceEvent> presenceListener) {
		
		client.removeInboundPresenceListener(presenceListener);
	}
		
	/**
	 * Returns the Presence of a PresenceEvent.
	 * @param presenceEvent
	 * @return
	 */
	public static Presence getPresenceOfEvent(PresenceEvent presenceEvent) {
		
	    return presenceEvent.getPresence();
	}
	
	/**
	 * Returns the Contact of a PresenseEvent.
	 * @param presenceEvent
	 * @return
	 */
	public static Jid getContactOfEvent(PresenceEvent presenceEvent) {
		
		Presence presence = presenceEvent.getPresence();
		return presence.getFrom();
	}
}
