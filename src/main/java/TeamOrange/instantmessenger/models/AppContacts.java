package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.views.MUCContactDisplay;
import javafx.application.Platform;

/**
 * Holds a list of contacts, incoming contact-add requests
 * And the AppUser self, representing the logged in user.
 *
 */
public class AppContacts {

	private AppUser self;
	private LinkedList<AppUser> contactList;
	private LinkedList<AppJid> incomingContactRequestList;

	public AppContacts(){
		this.contactList = new LinkedList<AppUser>();
		this.incomingContactRequestList = new LinkedList<AppJid>();
	}

	/**
	 * Resets this object.
	 */
	public void reset(){
		self = null;
		contactList.clear();
		incomingContactRequestList.clear();
	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	/**
	 * Adds a contact to the list.
	 * @param user the user to add to the contact list
	 */
	public void addContact(AppUser user){
		contactList.add(user);
	}

	/**
	 * Removes a contact from the list.
	 * @param userName the username of the contact to remove.
	 */
	public void removeContact(String userName){
		for(int i = 0; i < contactList.size(); ++i){
			if(contactList.get(i).getName().equals(userName)){
				contactList.remove(i);
				return;
			}
		}
	}

	/**
	 * Adds the given list of users to the contact list.
	 * @param list the list of users to add to the contact list.
	 */
	public void addAllContacts(LinkedList<AppUser> list){
		if(list != null){
			for(AppUser au : list){
				addContact(au);
			}
		}
	}

	/**
	 * Adds a contact request to the list, if that request does not already exist, and that user is not already a contact.
	 * @param jid the jid of the user that the request is from.
	 */
	public void addContactRequest(AppJid jid){
		for(AppUser u : contactList){
			if(u.getJid().getBareJid().equals(jid.getBareJid())){
				return;
			}
		}

		for(AppJid j : incomingContactRequestList){
			if(j.getBareJid().equals(j.getBareJid())){
				return;
			}
		}
		incomingContactRequestList.add(jid);
	}

	/**
	 * removes a contact request if it exists.
	 * @param jid the jid of the user who's contact request is to be removed.
	 */
	public void removeContactRequest(AppJid jid){
		String username = jid.getLocal();
		for(int i = 0; i < incomingContactRequestList.size(); ++i){
			if(incomingContactRequestList.get(i).getLocal().equals(username)){
				incomingContactRequestList.remove(i);
				break;
			}
		}
	}

	public AppUser getSelf(){
		return self;
	}

	public String getSelfName() {
		return self.getJid().getLocal();
	}

	public LinkedList<AppUser> getContactList(){
		return contactList;
	}

	public LinkedList<AppJid> getContactRequestList(){
		return incomingContactRequestList;
	}

	public AppUser getContactWithUsername(String username){
		for(AppUser user : contactList){
			if(user.getJid().getLocal().equals(username)){
				return user;
			}
		}
		return null;
	}

	/**
	 * Returns true if the contact list contains a contact with the given bare jid
	 * @param bareJid the bare jid to check for
	 * @return true if that contact exists, false if they dont
	 */
	public boolean containsBareJid(String bareJid){
		for(AppUser user : contactList){
			if(user.getJid().getBareJid().equals(bareJid)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Set the Presence of AppUser
	 * @param username the username of the user to set the presence for
	 * @param presence the type of presence to be set
	 */
	public void setPresence(String username, AppPresence.Type presence) {
		for(AppUser user : contactList) {
			if(user.getName().equals(username)) {
				user.setPresence(presence);
			}
		}
	}

	/**
	 * Sets the notificationfor the given contact
	 * @param username the username of the contact to set the notification for
	 * @param notify the boolean value to set the notification to
	 */
	public void setNotification(String username, boolean notify) {
		for(AppUser user : contactList) {
			if(user.getName().equals(username)) {
				user.setNotification(notify);
			}
		}
	}
}
