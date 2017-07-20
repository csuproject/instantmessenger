package TeamOrange.instantmessenger.xmpp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSession;
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
	public void addContact(XmppClient client, String contact, String name, String message) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact),
				name), true, message);
	}

	/**
	 * Add Contact to Roster.
	 * @param client
	 * @param contact
	 * @param name
	 */
	public void addContact(XmppClient client, String contact, String name) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact),
				name), true, null);
	}

	/**
	 * Add Contact to Roster.
	 * @param client
	 * @param contact
	 */
	public void addContact(XmppClient client, String contact) {

		client.getManager(RosterManager.class).addContact(new Contact(Jid.of(contact)), true, null);
	}

	/**
	 * Remove Contact from Roster.
	 * @param client
	 * @param contact
	 */
	public void removeContact(XmppClient client, String contact) {

		client.getManager(RosterManager.class).removeContact(Jid.of(contact));
	}

	/**
	 * Get Contacts
	 * @param client
	 * @return
	 */

	public LinkedList<String> getContacts(XmppClient client) {

		LinkedList<String> contacts = new LinkedList<String>();
		Collection<Contact> list = new <Contact>LinkedList();
		list = client.getManager(RosterManager.class).getContacts();
		for (Contact c : list) {
		    contacts.add(String.valueOf(Jid.of(c.getJid())));
		}

		return contacts;
	}

	/**
	 * Gets a LinkedList of AppUser which represents the users contacts
	 * @param client
	 * @return contacts a LinkedList of AppUser representing the users contacts
	 */
	public LinkedList<AppUser> getContactsAsAppUsers(XmppClient client) {

		LinkedList<AppUser> contacts = new LinkedList<AppUser>();
		Collection<Contact> list = new <Contact>LinkedList();
		list = client.getManager(RosterManager.class).getContacts();
		for (Contact c : list) {
			Jid jid = c.getJid();
			AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain());
		    contacts.add( new AppUser(appJid) );
		}

		return contacts;
	}

	/**
	 * Get Groups
	 * @param client
	 * @return
	 */

	public LinkedList<String> getContactGroups(XmppClient client) {

		LinkedList<String> groups = new LinkedList<String>();
		Collection<ContactGroup> list = new <ContactGroup>LinkedList();
		list = client.getManager(RosterManager.class).getContactGroups();
		for (ContactGroup g : list) {
			groups.add(g.getName());
		}

		return groups;
	}

	/**
	 * Add Contact request listener.
	 * @param client
	 * @param event
	 */
	public void addListenerPresence(XmppClient client, Consumer<PresenceEvent> presenceListener) {

		client.addInboundPresenceListener(presenceListener);
	}

	/**
	 * Add Contact request listener.
	 * @param client
	 * @param event
	 */
	public void removeListenerPresence(XmppClient client, Consumer<PresenceEvent> presenceListener) {

		client.removeInboundPresenceListener(presenceListener);
	}

	/**
	 * Returns the Presence of a PresenceEvent.
	 * @param presenceEvent
	 * @return
	 */
	public Presence getPresenceOfEvent(PresenceEvent presenceEvent) {

	    return presenceEvent.getPresence();
	}

	/**
	 * Returns the Contact of a PresenseEvent.
	 * @param presenceEvent
	 * @return
	 */
	public Jid getContactOfEvent(PresenceEvent presenceEvent) {

		Presence presence = presenceEvent.getPresence();

		return presence.getFrom();
	}


	/**
	 * Approve Contact to listen to Presence.
	 */
	public void approveContact(XmppClient client, Jid contact) {
		client.getManager(PresenceManager.class).approveSubscription(contact);
	}

	/**
	 * Deny Contact to listen to Presence.
	 */
	public void denyContact(XmppClient client, Jid contact) {
		client.getManager(PresenceManager.class).denySubscription(contact);
	}


}
