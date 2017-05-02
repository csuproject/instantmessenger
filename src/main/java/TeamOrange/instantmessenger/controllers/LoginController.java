package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideAuthenticationException;

public class LoginController {

	private AccountScreen accountScreen;
	private BabblerBase babblerBase;
	private AppContacts contacts;
	private ChangeScreen changeScreen;

	public LoginController(BabblerBase babblerBase, AccountScreen accountScreen, AppContacts contacts){
		this.babblerBase = babblerBase;
		this.accountScreen = accountScreen;
		accountScreen.setOnLoginEvent( (userName, password)->login(userName, password) );
		this.contacts = contacts;
	}

	public void login(String userName, String password){
		AppJid appJid;
		try{
			appJid = babblerBase.login(userName, password);
			AppUser appUser = new AppUser(appJid);
			contacts.setSelf(appUser);
			changeScreen.SetScreen(ScreenEnum.HOME);
		} catch(ConfideAuthenticationException e){
			System.out.println("Authentication Exception");
			accountScreen.authenticationException();
			return;
		}
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
