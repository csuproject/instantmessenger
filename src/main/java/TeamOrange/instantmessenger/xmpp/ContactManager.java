package TeamOrange.instantmessenger.xmpp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSession;
import rocks.xmpp.core.stanza.PresenceEvent;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.privacy.PrivacyListManager;
import rocks.xmpp.extensions.privacy.model.PrivacyList;
import rocks.xmpp.extensions.privacy.model.PrivacyRule;
import rocks.xmpp.im.roster.RosterManager;
import rocks.xmpp.im.roster.model.Contact;
import rocks.xmpp.im.roster.model.ContactGroup;
import rocks.xmpp.im.subscription.PresenceManager;
import rocks.xmpp.util.concurrent.AsyncResult;

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
	public AppUser addContact(XmppClient client, String contact) {
		Jid jid = Jid.of(contact);
		Presence presence = client.getManager(PresenceManager.class).getPresence(jid);
		client.getManager(RosterManager.class).addContact(new Contact(jid), true, null);
		
		AppJid appJid = JidUtilities.appJidFromJid(jid);
		AppPresence appPresence = new AppPresence(presence);
		return new AppUser(appJid, appPresence);
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
	 * Blocks all communication with the user
	 * @param client
	 * @param user the jid of the user to block
	 */
	public void blockUser(XmppClient client, String user) {
		Jid contactJid = Jid.of(user);
		PrivacyListManager plManager = client.getManager(PrivacyListManager.class);
		// create new rule
		PrivacyRule newRule = PrivacyRule.blockAllCommunicationWith(contactJid, 1);
		
		PrivacyList blockedList = null;
		// retrieve existing blocked list if it exists
		try {
			blockedList = plManager.getPrivacyList("blocked").getResult();
		} catch (Exception e) {
			blockedList = null;
		}
		
		// create new rule list
		Collection<PrivacyRule> newRules =  new LinkedList<PrivacyRule>();
		// add existing rules if they exist
		if(blockedList != null){
			Collection<PrivacyRule> existingRules = blockedList.getPrivacyRules();
			newRules.addAll(existingRules);
		}
		// add new rule
		newRules.add(newRule);

		PrivacyList newBlockedList = new PrivacyList("blocked", newRules);
		plManager.createOrUpdateList(newBlockedList);
//		plManager.setActiveList("blocked");
		plManager.setDefaultList("blocked");
	}

	public void printBlockList(XmppClient client){
		try {
			PrivacyListManager plManager = client.getManager(PrivacyListManager.class);
			PrivacyList currentBlockedList = plManager.getPrivacyList("blocked").getResult();
			Collection<PrivacyRule> rules = currentBlockedList.getPrivacyRules();
			System.out.println("Block List");
			for(PrivacyRule pr : rules){
				pr.getType();
				System.out.println( pr.getType() + " : " + pr.getAction() );
			}
			System.out.println("Block List End");
		} catch (XmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		PresenceManager pm = client.getManager(PresenceManager.class); //.getPresence(arg0)
		for (Contact c : list) {
			Jid jid = c.getJid();
			AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain());
			Presence presence = pm.getPresence(jid);
			AppPresence appPresence = new AppPresence(presence);
		    contacts.add( new AppUser(appJid, appPresence) );
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
