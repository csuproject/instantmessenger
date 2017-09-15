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

	public void reset(){
		self = null;
		contactList.clear();
		incomingContactRequestList.clear();
	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	public void addContact(AppUser user){
		contactList.add(user);
	}

	public void removeContact(String userName){
		for(int i = 0; i < contactList.size(); ++i){
			if(contactList.get(i).getName().equals(userName)){
				contactList.remove(i);
				return;
			}
		}
	}

	public void addAllContacts(LinkedList<AppUser> list){
		if(list != null){
			for(AppUser au : list){
				addContact(au);
			}
		}
	}

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
	 * @param username
	 */
	public void setPresence(String username, AppPresence.Type presence) {
		for(AppUser user : contactList) {
			if(user.getName().equals(username)) {
				user.setPresence(presence);
			}
		}
	}

	public void setNotification(String username, boolean notify) {
		for(AppUser user : contactList) {
			if(user.getName().equals(username)) {
				user.setNotification(notify);
			}
		}
	}
}
