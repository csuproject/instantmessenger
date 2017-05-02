package TeamOrange.instantmessenger;

import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.GuiBase;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

	private BabblerBase babblerBase;

	// Screens
	private AccountScreen accountScreen;

	// Controllers
	private CreateAccountController createAccountController;
	private LoginController loginController;

	public App(GuiBase guiBase){

		accountScreen = new AccountScreen(guiBase);

		createAccountController = new CreateAccountController(babblerBase, accountScreen);
	}

    public void init(){
    	babblerBase = new BabblerBase("teamorange.space", () -> messageListener(), () -> presenceListener(), () -> rosterListener());

    	babblerBase.setupConnection();
    	babblerBase.connect();
    }

    public void messageListener(){

    }

    public void presenceListener(){

    }

    public void rosterListener(){

    }

    // Screens
    public AccountScreen getAccountScreen(){
    	return accountScreen;
    }

/////////////////////////////////////////////////////
// These probably shouldnt be here
/////////////////////////////////////////////////////

//    public BabblerBase getClient(){
//    	return babblerBase;
//    }




}
