package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppContacts {

	private AppUser self;
	private LinkedList<AppUser> contactList;
	private LinkedList<AppJid> incomingContactRequestList;
	private LinkedList<AppJid> outgoingContactRequestList;

	public AppContacts(){
		this.contactList = new LinkedList<AppUser>();
		this.incomingContactRequestList = new LinkedList<AppJid>();
	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	public void addContact(AppUser user){
		contactList.add(user);
	}

	public void addAllContacts(LinkedList<AppUser> list){
		System.out.println("addAllContacts");
		System.out.println(list);
		if(list != null){
			for(AppUser au : list){
				addContact(au);
				System.out.println(au.getJid().getBareJid());
			}
		}
	}

	public void addContactRequest(AppJid jid){
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
}
