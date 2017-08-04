package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.StatusEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.UserStatus;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideAuthenticationException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;

public class PresenceController {

	private AccountScreen accountScreen;
	private BabblerBase babblerBase;
	private AppContacts contacts;
	private ChangeScreen changeScreen;

	public PresenceController(BabblerBase babblerBase, AccountScreen accountScreen,
			AppContacts contacts){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		this.contacts = contacts;
	}

	public void status(AppJid from, AppPresence.Type type) {
//		System.out.println(from.getBareJid() + " is " + AppPresence.typeString(type));
		AppUser contact = contacts.getContactWithUsername(from.getLocal());
		if(contact != null){
			contact.setPresence(type);
		}
		
	}
}
