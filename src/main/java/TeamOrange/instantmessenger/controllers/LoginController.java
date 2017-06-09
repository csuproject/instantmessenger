package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideAuthenticationException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;
import javafx.scene.control.Alert.AlertType;

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
			LinkedList<AppUser> contactsFromServer = babblerBase.getContactsAsAppUsers();
			contacts.addAllContacts(contactsFromServer);
			changeScreen.SetScreen(ScreenEnum.HOME);
		} catch(ConfideAuthenticationException e){
			accountScreen.alert("The username or password that you entered was incorrect. Please try again, or create a new account if you dont already have one.", "login error", AlertType.WARNING);
			return;
		} catch(ConfideNoResponseException e){
			accountScreen.alert("No response was recieved from the server.", "login error", AlertType.WARNING);
		} catch(ConfideXmppException e){
			accountScreen.alert("Something went wrong.", "login error", AlertType.WARNING);
		}
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
