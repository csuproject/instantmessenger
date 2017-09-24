package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.CreateUserInvalidFormatException;
import javafx.scene.control.Alert.AlertType;
import rocks.xmpp.core.XmppException;

/**
 * This handles the flow when the user creates a new account
 *
 */
public class CreateAccountController {

	private AccountScreen accountScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;
	private ConnectionController connectionController;

	public CreateAccountController(BabblerBase babblerBase, AccountScreen accountScreen, ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		accountScreen.setOnCreateAccountEvent( (userName, password)->createAccount(userName, password) );
		this.connectionController = connectionController;
	}

	/**
	 * Creates an acocunt.
	 * This is used by connectionController.
	 * @param userName the username to create the account with
	 * @param password the password to create the account with
	 */
	public void actuallyCreateAccount(String userName, String password) {
		try {
			babblerBase.createUser(userName, password);
			accountScreen.alertLater("User( \""+userName+"\" ) successfully created", "user created", AlertType.CONFIRMATION);
		} catch (XmppException e) {
			accountScreen.alertLater("User( \""+userName+"\" ) already exists", "user already exists", AlertType.WARNING);
		}
	}

	/**
	 * This is called by AccountScreen when the onCreateAccountEvent occurs
	 * Tells BabblerBase to create a user with the given username and password
	 * The actual work for creating the account is differed to connectionController so that it can be ensured that the app has a connection before creating the account.
	 * @param userName the username to create the account with
	 * @param password the password to create the account with
	 */
	public void createAccount(String userName, String password){
		connectionController.addCreateAccountTask(this, userName, password);
		connectionController.completeTasks();
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
