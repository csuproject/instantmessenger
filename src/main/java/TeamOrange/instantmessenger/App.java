package TeamOrange.instantmessenger;

import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.models.AppMessage;
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

		accountScreen = new AccountScreen();

		createAccountController = new CreateAccountController(babblerBase, accountScreen);
		createAccountController.setOnChangeScreen( screen->guiBase.setScreen(screen) );
	}

    public void init(){
    	babblerBase = new BabblerBase("teamorange.space", appMessage -> messageListener(appMessage), () -> presenceListener(), () -> rosterListener());

    	babblerBase.setupConnection();
    	babblerBase.connect();
    }

    public void messageListener(AppMessage message){

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
