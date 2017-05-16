package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppContacts {

	private AppUser self;
	private LinkedList<AppUser> contactList;
	private LinkedList<AppJid> contactRequestList;

	public AppContacts(){
		this.contactList = new LinkedList<AppUser>();
		this.contactRequestList = new LinkedList<AppJid>();
	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	public void addContact(AppUser user){
		contactList.add(user);
	}

	public void addAllContacts(LinkedList<AppUser> list){
		if(list != null){
			for(AppUser au : list){
				addContact(au);
			}
		}
	}

	public void addContactRequest(AppJid jid){
		contactRequestList.add(jid);
	}

	public void removeContactRequest(AppJid jid){
		String username = jid.getLocal();
		for(int i = 0; i < contactRequestList.size(); ++i){
			if(contactRequestList.get(i).getLocal().equals(username)){
				contactRequestList.remove(i);
				break;
			}
		}
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

	public AppUser getContactWithUsername(String username){
		for(AppUser user : contactList){
			if(user.getJid().getLocal().equals(username)){
				return user;
			}
		}
		return null;
	}
}
