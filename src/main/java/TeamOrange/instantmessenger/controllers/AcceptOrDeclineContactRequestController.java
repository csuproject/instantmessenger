package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

/**
 * controls flow when the user accepts or declines a contact-add request
 *
 */
public class AcceptOrDeclineContactRequestController {

	private BabblerBase babblerBase;
	private HomeScreen homeScreen;
	private AppContacts contacts;
	private ChangeScreen changeScreen;

	public AcceptOrDeclineContactRequestController(BabblerBase babblerBase, HomeScreen homeScreen, AppContacts contacts){
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		homeScreen.setOnAcceptContactRequestEvent(username->onAcceptContactRequestEvent(username));
		homeScreen.setOnDeclineContactRequestEvent(username->onDeclineContactRequestEvent(username));
		this.contacts = contacts;
	}

	/**
	 * This is called by HomeScreen when the onAcceptContactRequestEvent occurs
	 * If the contact is not already in the contacts list,
	 * 		then it adds the contact to the contact list,
	 * 		and tells the server to add the contact.
	 * Removes the contact-add request
	 * Alerts the user of the response
	 * Tells homescreen to reload
	 * @param username the username of the contact whos contact-add request has been accepted
	 */
	public void onAcceptContactRequestEvent(String username){
		AppUser contact = contacts.getContactWithUsername(username);
		AppJid jid = new AppJid(username, "teamorange.space");
		if(contact == null){
			babblerBase.addContact(jid.getBareJid());
			contacts.addContact(new AppUser(jid));
		}
		contacts.removeContactRequest(jid);
		babblerBase.alertUserOfContactRequestResponse(jid, true);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	/**
	 * This is called by HomeScreen when the onDeclineContactRequestEvent occurs
	 * Removes the contact-add request
	 * Alerts the user of the response
	 * Tells homescreen to reload
	 * @param username the username of the contact whos contact-add request has been declined
	 */
	public void onDeclineContactRequestEvent(String username){
		AppJid jid = new AppJid(username, "teamorange.space");
		contacts.removeContactRequest(jid);
		babblerBase.alertUserOfContactRequestResponse(jid, false);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
