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
	private ConnectionController connectionController;

	public AcceptOrDeclineContactRequestController(BabblerBase babblerBase,
			HomeScreen homeScreen, AppContacts contacts, ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		homeScreen.setOnAcceptContactRequestEvent(username->onAcceptContactRequestEvent(username));
		homeScreen.setOnDeclineContactRequestEvent(username->onDeclineContactRequestEvent(username));
		this.contacts = contacts;
		this.connectionController = connectionController;
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

		contacts.removeContactRequest(jid);
		homeScreen.loadLater(new HomeScreenInput(contacts));

		connectionController.addReplyToContactRequestTask(this, contact, jid, true);
		connectionController.completeTasks();
	}

	public void actuallyReplyToContactRequest(AppUser contact, AppJid jid, boolean accepted){
		if(contact == null){
			if(accepted){
				contact = babblerBase.addContact(jid.getBareJid());
				contacts.addContact(contact);
			}
		}
		babblerBase.alertUserOfContactRequestResponse(jid, accepted);
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
		AppUser contact = contacts.getContactWithUsername(username);
		contacts.removeContactRequest(jid);
		homeScreen.loadLater(new HomeScreenInput(contacts));

		connectionController.addReplyToContactRequestTask(this, contact, jid, false);
		connectionController.completeTasks();
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
