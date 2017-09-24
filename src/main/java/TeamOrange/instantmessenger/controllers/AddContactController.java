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
	 * @param username the username of the contact to send a contact-add request to.
	 */
	public void actuallyAddContact(String username){
		AppJid jid = new AppJid(username, "teamorange.space");
		babblerBase.requestContactAdd(jid);
		//babblerBase.requestSubsription(jid.getBareJid(), "Hello, I would like to to chat!?");
	}

	/**
	 * This actually sends the contact request.
	 * It is handled by contactController so that it can be ensured that there is a connection first.
	 * @param username the username of the user to send the request to.
	 */
	public void addContact(String username){
		connectionController.addSendContactRequestTask(this, username);
		connectionController.completeTasks();
	}

	/**
	 * This function is called when the contact-add request has been successfully sent.
	 * It then updates the screen accordingly.
	 * @param to the jid of the user that the contact request has been sent to.
	 */
	public void contactAddRequestSent(AppJid to){
    	homeScreen.alertLater("Contact add request sent to "+to.getBareJid(), "Contact Add Request Sent", AlertType.CONFIRMATION);
    }

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
