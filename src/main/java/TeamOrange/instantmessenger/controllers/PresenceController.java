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

/**
 * Controls the flow when a new presence is received.
 *
 */
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

	/**
	 * This is called when a new presence is recieved.
	 * Updates the presence for the given contact.
	 * @param from the contact that the presence is from
	 * @param type the type of presence
	 */
	public void status(AppJid from, AppPresence.Type type) {
		AppUser contact = contacts.getContactWithUsername(from.getLocal());
		if(contact != null){
			contact.setPresence(type);
		}

	}
}
