package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class ContactManagementController {

	private BabblerBase babblerBase;
	private HomeScreen homeScreen;
	private AppChats chats;
	private AppContacts contacts;
	private ConnectionController connectionController;
	private ChangeScreen changeScreen;

	public ContactManagementController(BabblerBase babblerBase, HomeScreen homeScreen, AppChats chats,
			AppContacts contacts, ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		homeScreen.setOnBlockContactEvent( userName->onBlockContactEvent(userName) );
		homeScreen.setOnDeleteContactEvent( userName->onDeleteContactEvent(userName) );
		this.chats = chats;
		this.contacts = contacts;
		this.connectionController = connectionController;

	}

	public void onBlockContactEvent(String userName){
		babblerBase.blockUser(userName);
		babblerBase.requestDeleteFromContacts(userName);
		deleteContactLocally(userName);
		homeScreen.load(new HomeScreenInput(contacts));
	}

	public void onDeleteContactEvent(String userName){
		babblerBase.removeContact(userName);
		babblerBase.requestDeleteFromContacts(userName);
		deleteContactLocally(userName);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	public void onRequestDeleteFromContacts(String userName){
		deleteContactLocally(userName);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	private void deleteContactLocally(String userName){
		contacts.removeContact(userName);
		chats.removeChat(userName);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
