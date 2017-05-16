package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class AddContactController {

	private BabblerBase babblerBase;
	private HomeScreen homeScreen;
	private AppContacts contacts;
	private ChangeScreen changeScreen;

	public AddContactController(BabblerBase babblerBase, HomeScreen homeScreen, AppContacts contacts){
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		homeScreen.setOnAddContactEvent(username->addContact(username));
		this.contacts = contacts;
	}

	public void addContact(String username){

	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
