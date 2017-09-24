package TeamOrange.instantmessenger.views;

import java.util.LinkedList;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;

/**
 * Represents the input for the home screen.
 *
 */
public class HomeScreenInput implements ScreenInput {
	private AppContacts contacts;

	public HomeScreenInput(AppContacts contacts){
		this.contacts = contacts;
	}

	public String getUserName(){
		return contacts.getSelf().getJid().getLocal();
	}

	public LinkedList<AppUser> getContactList(){
		return contacts.getContactList();
	}

	public LinkedList<AppJid> getContactRequestList(){
		return contacts.getContactRequestList();
	}
}
