package TeamOrange.instantmessenger;

import TeamOrange.instantmessenger.controllers.ChatController;
import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.CreateChatController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.GuiBase;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

	private BabblerBase babblerBase;
	private GuiBase guiBase;

	// Screens
	private AccountScreen accountScreen;
	private HomeScreen homeScreen;
	private ChatScreen chatScreen;
	private ScreenEnum currentScreen;

	// Controllers
	private CreateAccountController createAccountController;
	private LoginController loginController;
	private CreateChatController createChatController;
	private ChatController chatController;

	//
	AppContacts contacts;
	AppChats chats;

	public App(GuiBase guiBase){
		this.guiBase = guiBase;
		accountScreen = new AccountScreen();
		homeScreen = new HomeScreen();
		chatScreen = new ChatScreen();
		setScreen(ScreenEnum.ACCOUNT);

		//
		contacts = new AppContacts();
		chats = new AppChats();
		//

		babblerBase = new BabblerBase("teamorange.space", appMessage->messageListener(appMessage), appPresence->presenceListener(appPresence), () -> rosterListener());
    	babblerBase.setupConnection();
    	babblerBase.connect();

		createAccountController = new CreateAccountController(babblerBase, accountScreen);
		createAccountController.setOnChangeScreen( screen->setScreen(screen) );

		loginController = new LoginController(babblerBase, accountScreen, contacts);
		loginController.setOnChangeScreen( screen->setScreen(screen) );

		createChatController = new CreateChatController(chats, contacts, babblerBase, homeScreen);
		createChatController.setOnChangeScreen( screen->setScreen(screen) );

		chatController = new ChatController(babblerBase, chatScreen, chats);
		chatController.setOnChangeScreen( screen->setScreen(screen) );

	}

    public void init(){

    }

    public void messageListener(AppMessage message){

    }

    public void presenceListener(AppPresence appPresence){

    }

    public void rosterListener(){

    }

    // Screens
    public AccountScreen getAccountScreen(){
    	return accountScreen;
    }

    public void setScreen(ScreenEnum screen){
    	this.currentScreen = screen;
    	switch(currentScreen){
			case ACCOUNT:
			{
				guiBase.setScreen(accountScreen);
			} break;
			case HOME:
			{
				HomeScreenInput input = new HomeScreenInput(contacts.getSelf().getJid().getLocal());
				homeScreen.load(input);
				guiBase.setScreen(homeScreen);
			} break;
			case CHAT:
			{
				ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
				chatScreen.load(input);
				guiBase.setScreen(chatScreen);
			} break;
    	}
    }




}
