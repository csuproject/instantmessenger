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

	public AddContactController(BabblerBase babblerBase, HomeScreen homeScreen, AppContacts contacts){
		this.babblerBase = babblerBase;
		babblerBase.setOnRequestContactAddSent(to->contactAddRequestSent(to));
		this.homeScreen = homeScreen;
		homeScreen.setOnAddContactEvent(username->addContact(username));
		this.contacts = contacts;
	}

	/**
	 * This is called by HomeScreen when the onAddContactEvent occurs
	 * sends a contact-add request to the given user
	 * @param username
	 */
	public void addContact(String username){
		AppJid jid = new AppJid(username, "teamorange.space");
		babblerBase.requestContactAdd(jid);
		//babblerBase.requestSubsription(jid.getBareJid(), "Hello, I would like to to chat!?");
	}

	public void contactAddRequestSent(AppJid to){
    	homeScreen.alertLater("Contact add request sent to "+to.getBareJid(), "Contact Add Request Sent", AlertType.CONFIRMATION);
    }

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
