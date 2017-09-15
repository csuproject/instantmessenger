package TeamOrange.instantmessenger.views;

import java.util.List;

import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppUser;

public class MucInviteScreenInput implements ScreenInput {
	private AppMucList mucs;
	private AppContacts contacts;

	public MucInviteScreenInput(AppMucList mucs, AppContacts contacts){
		this.mucs = mucs;
		this.contacts = contacts;
	}

	public AppMuc getMucInvitingTo(){
		return mucs.getMucInvitingTo();
	}

	public List<AppUser> getContacts(){
		return contacts.getContactList();
	}
}
