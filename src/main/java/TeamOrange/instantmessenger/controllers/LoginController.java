package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class LoginController {

	private AccountScreen accountScreen;
	BabblerBase babblerBase;

	public LoginController(BabblerBase babblerBase, AccountScreen accountScreen){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		accountScreen.setOnLoginEvent( (userName, password)->login(userName, password) );
	}

	public void login(String userName, String password){
		babblerBase.login(userName, password);
	}
}
