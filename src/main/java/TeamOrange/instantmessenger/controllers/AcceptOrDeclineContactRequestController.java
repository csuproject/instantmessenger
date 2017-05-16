package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

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

	public void onAcceptContactRequestEvent(String username){
		AppUser contact = contacts.getContactWithUsername(username);
		if(contact == null){
			AppJid jid = new AppJid(username, "teamorange.space");
			contacts.removeContactRequest(jid);
			contacts.addContact(new AppUser(jid));
			babblerBase.alertUserOfContactRequestResponse(jid, true);
			homeScreen.loadLater(new HomeScreenInput(contacts));
		}
	}

	public void onDeclineContactRequestEvent(String username){

	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
