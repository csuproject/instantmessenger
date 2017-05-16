package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppContacts {

	private AppUser self;
	private LinkedList<AppUser> contactList;
	private LinkedList<AppJid> contactRequestList;

	public AppContacts(){

	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	public void addContact(AppUser user){
		contactList.add(user);
	}

	public void addContactRequest(AppJid jid){
		contactRequestList.add(jid);
	}

	public AppUser getSelf(){
		return self;
	}

	public LinkedList<AppUser> getContactList(){
		return contactList;
	}

	public LinkedList<AppJid> getContactRequestList(){
		return contactRequestList;
	}
}
