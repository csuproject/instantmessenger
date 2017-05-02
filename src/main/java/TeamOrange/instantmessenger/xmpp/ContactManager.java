package TeamOrange.instantmessenger.xmpp;

import java.util.Collection;
import java.util.function.Consumer;

import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.PresenceEvent;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;
import rocks.xmpp.im.roster.model.ContactGroup;
import rocks.xmpp.im.subscription.PresenceManager;

public class ContactManager {

	/**
	 * Add Contact to Roster.
	 * @param client
	 * @param contact
	 * @param name
	 * @param message
	 */
	public static void addContact(XmppClient client, String contact, String name, String message) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact),
				name), true, message);
	}

	/**
	 * Add Contact to Roster.
	 * @param client
	 * @param contact
	 * @param name
	 */
	public static void addContact(XmppClient client, String contact, String name) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact),
				name), true, null);
	}

	/**
	 * Add Contact to Roster.
	 * @param client
	 * @param contact
	 */
	public static void addContact(XmppClient client, String contact) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact)), true, null);
	}

	/**
	 * Remove Contact from Roster.
	 * @param client
	 * @param contact
	 */
	public static void removeContact(XmppClient client, String contact) {

		client.getManager(RosterManager.class).removeContact(Jid.of(contact));
	}

	/**
	 * Get Contacts
	 * @param client
	 * @return
	 */
	public static Collection<Contact> getContacts(XmppClient client) {

		return client.getManager(RosterManager.class).getContacts();
	}

	/**
	 * Get Groups
	 * @param client
	 * @return
	 */
	public static Collection<ContactGroup> getGroups(XmppClient client) {

		return client.getManager(RosterManager.class).getContactGroups();
	}

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

	/**
	 * Approve Contact to listen to Presence.
	 */
	public static void approveContact(XmppClient client, Jid contact) {
		client.getManager(PresenceManager.class).approveSubscription(contact);
	}

	/**
	 * Deny Contact to listen to Presence.
	 */
	public static void denyContact(XmppClient client, Jid contact) {
		client.getManager(PresenceManager.class).denySubscription(contact);
	}

}
