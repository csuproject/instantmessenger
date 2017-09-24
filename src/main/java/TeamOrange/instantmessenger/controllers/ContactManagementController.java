package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

/**
 * Controls the flow for contact management events.
 * These include block and delete
 */
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

	/**
	 * This is called when a contact is to be blocked.
	 * Requests the given contact to delete this user from their contacts.
	 * Blocks the given contact.
	 * Deletes the contact locally.
	 * Re loads the screen to reflect these changes.
	 * @param userName the username of the contact to block
	 */
	public void onBlockContactEvent(String userName){
		babblerBase.requestDeleteFromContacts(userName);
		babblerBase.blockUser(userName);
		deleteContactLocally(userName);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	/**
	 * This is called when a contact is to be deleted.
	 * Deletes the contact.
	 * Requests the contact to delete this user from their contacts.
	 * Deletes the contact locally.
	 * Re loads the screen to reflect these changes.
	 * @param userName the username of the contact to delete
	 */
	public void onDeleteContactEvent(String userName){
		babblerBase.removeContact(userName);
		babblerBase.requestDeleteFromContacts(userName);
		deleteContactLocally(userName);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	/**
	 * This is called when this user is requested by another user to remove that user from their contacts.
	 * The delete will happen on the server regardless. So the next time this user runs the app, they will not have that contact.
	 * This is only to ensure that if they are logged in at the time, their local version of things, reflects the state of their contacts on the server.
	 * @param userName the username of the contact that is requesting the delete.
	 */
	public void onRequestDeleteFromContacts(String userName){
		deleteContactLocally(userName);
		homeScreen.loadLater(new HomeScreenInput(contacts));
	}

	/**
	 * Deletes a contact locally.
	 * @param userName the username of the contact that is deleted locally
	 */
	private void deleteContactLocally(String userName){
		contacts.removeContact(userName);
		chats.removeChat(userName);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
