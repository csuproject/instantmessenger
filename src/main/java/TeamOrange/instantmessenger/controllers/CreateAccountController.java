package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class CreateAccountController {

	private AccountScreen accountScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;

	public CreateAccountController(BabblerBase babblerBase, AccountScreen accountScreen){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		accountScreen.setOnCreateAccountEvent( (userName, password)->createAccount(userName, password) );
	}

	public void createAccount(String userName, String password){
		babblerBase.createUser(userName, password);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
