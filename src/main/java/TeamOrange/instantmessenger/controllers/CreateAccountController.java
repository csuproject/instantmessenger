package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.CreateUserInvalidFormatException;

/**
 * This handles the flow when the user creates a new account
 *
 */
public class CreateAccountController {

	private AccountScreen accountScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;

	public CreateAccountController(BabblerBase babblerBase, AccountScreen accountScreen){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		accountScreen.setOnCreateAccountEvent( (userName, password)->createAccount(userName, password) );
	}

	/**
	 * This is called by AccountScreen when the onCreateAccountEvent occurs
	 * Tells BabblerBase to create a user with the given username and password
	 * @param userName
	 * @param password
	 */
	public void createAccount(String userName, String password) {
		babblerBase.createUser(userName, password);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
