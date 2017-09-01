package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.scene.control.Alert.AlertType;

/**
 * controls the flow when the user sends a contact-add request
 *
 */
public class AddContactController {

	private BabblerBase babblerBase;
	private HomeScreen homeScreen;
	private AppContacts contacts;
	private ChangeScreen changeScreen;
	private ConnectionController connectionController;

	public AddContactController(BabblerBase babblerBase, HomeScreen homeScreen, AppContacts contacts, ConnectionController connectionController){
		this.babblerBase = babblerBase;
		babblerBase.setOnRequestContactAddSent(to->contactAddRequestSent(to));
		this.homeScreen = homeScreen;
		homeScreen.setOnAddContactEvent(username->addContact(username));
		this.contacts = contacts;
		this.connectionController = connectionController;
	}

	/**
	 * This is called by HomeScreen when the onAddContactEvent occurs
	 * sends a contact-add request to the given user
	 * @param username
	 */
	public void actuallyAddContact(String username){
		AppJid jid = new AppJid(username, "teamorange.space");
		babblerBase.requestContactAdd(jid);
		//babblerBase.requestSubsription(jid.getBareJid(), "Hello, I would like to to chat!?");
	}

	public void addContact(String username){
		if(contacts.getContactWithUsername(username) != null){
			homeScreen.alertLater("You are already contacts with \""+username+"\"", "Contact Already Exists", AlertType.INFORMATION);
			return;
		}
		connectionController.addSendContactRequestTask(this, username);
		connectionController.completeTasks();
	}

	public void contactAddRequestSent(AppJid to){
    	homeScreen.alertLater("Contact add request sent to "+to.getBareJid(), "Contact Add Request Sent", AlertType.CONFIRMATION);
    }

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
