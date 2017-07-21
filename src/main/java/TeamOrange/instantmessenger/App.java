package TeamOrange.instantmessenger;

import java.util.LinkedList;

import TeamOrange.instantmessenger.controllers.AcceptOrDeclineContactRequestController;
import TeamOrange.instantmessenger.controllers.AddContactController;
import TeamOrange.instantmessenger.controllers.ChatController;
import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.OpenChatController;
import TeamOrange.instantmessenger.controllers.PresenceController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.controllers.MUCController;
import TeamOrange.instantmessenger.controllers.NavigationController;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMessageType;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.ContactDisplay;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.GuiBase;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.views.MUCScreenInput;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App {
	// constants for messages
	public static final String REQUEST_CONTACT_ADD = "1";
	public static final String ACCEPT_CONTACT_ADD = "2";
	public static final String DECLINE_CONTACT_ADD = "3";

	// xmpp
	private BabblerBase babblerBase;

	// views
	private GuiBase guiBase;
	private AccountScreen accountScreen;
	private HomeScreen homeScreen;
	private ChatScreen chatScreen;
	private ScreenEnum currentScreen;
	private NavigationScreen navigationScreen;
	private MUCScreen mucScreen;
	private CreateMUCScreen createMUCScreen;

	// Controllers
	private CreateAccountController createAccountController;
	private LoginController loginController;
	private OpenChatController openChatController;
	private ChatController chatController;
	private AddContactController addContactController;
	private AcceptOrDeclineContactRequestController acceptOrDeclineContactRequestController;
<<<<<<< HEAD
	private PresenceController presenceController; // TODO: never used ?
=======
	private PresenceController presenceController;
	private NavigationController naviationController;
	private MUCController mucController;
>>>>>>> refs/remotes/origin/C1-feature-mucscreen

	// models
	AppContacts contacts;
	AppChats chats;

	public App(GuiBase guiBase){
		// views
		this.guiBase = guiBase;
		accountScreen = new AccountScreen();
		homeScreen = new HomeScreen();
		chatScreen = new ChatScreen();
		navigationScreen = new NavigationScreen();
		mucScreen = new MUCScreen();
		createMUCScreen = new CreateMUCScreen();
		setScreen(ScreenEnum.ACCOUNT);

		// models
		contacts = new AppContacts();
		chats = new AppChats();

		// xmpp
		babblerBase = new BabblerBase("teamorange.space",
				appMessage->messageListener(appMessage),
				appPresence->presenceListener(appPresence),
				() -> rosterListener());

    	babblerBase.setupConnection();
    	try {
			babblerBase.connect();
		} catch (ConfideXmppException e) {
			accountScreen.alert("Failed to connect", "connection error", AlertType.ERROR);
			// TODO: fix this, try to connect again, find out if the user wants to exit or keep trying
			System.exit(1);
		}

    	// controllers
		createAccountController = new CreateAccountController(babblerBase, accountScreen);
		createAccountController.setOnChangeScreen( screen->setScreen(screen) );

		loginController = new LoginController(babblerBase, accountScreen, contacts);
		loginController.setOnChangeScreen( screen->setScreen(screen) );

		openChatController = new OpenChatController(chats, contacts, babblerBase, homeScreen);
		openChatController.setOnChangeScreen( screen->setScreen(screen) );

		chatController = new ChatController(babblerBase, chatScreen, chats, contacts);
		chatController.setOnChangeScreen( screen->setScreen(screen) );

		addContactController = new AddContactController(babblerBase, homeScreen, contacts);
		addContactController.setOnChangeScreen( screen->setScreen(screen) );

		acceptOrDeclineContactRequestController =
				new AcceptOrDeclineContactRequestController(babblerBase, homeScreen, contacts);
		acceptOrDeclineContactRequestController.setOnChangeScreen( screen->setScreen(screen) );

		presenceController = new PresenceController(babblerBase, accountScreen, contacts);
		
		naviationController = new NavigationController(navigationScreen);
		naviationController.setOnChangeScreen(screen->setScreen(screen));
		
		mucController = new MUCController(mucScreen,createMUCScreen);
		mucController.setOnChangeScreen(screen->setScreen(screen));
	}


	// Listeners
	/**
	 * This function is called by BabblerBase when there is a new message event
	 * If the message type is NORMAL, then it is handled accordingly
	 * Otherwise if the message type is CHAT, then it is passed to the chatController,
	 * 		along with a variable representing if the current screen is the chat screen.
	 * @param message the new message
	 */
    public void messageListener(AppMessage message){
    	if(message.getType() == AppMessageType.NORMAL){
    		String body = message.getBody();
    		if(body.equals(App.REQUEST_CONTACT_ADD)){
    			AppJid jid = message.getFromJid();
    			contacts.addContactRequest(jid);
    			if(currentScreen == ScreenEnum.HOME){
    				HomeScreenInput homeScreenInput = new HomeScreenInput(contacts);
    				homeScreen.loadLater(homeScreenInput);
    			}
    		}
    		else if(body.equals(App.ACCEPT_CONTACT_ADD)){
    			AppJid jid = message.getFromJid();
    			babblerBase.addContact( jid.getBareJid() );
    			System.out.println(jid.getBareJid());
    			contacts.addContact( new AppUser(jid) );
    			if(currentScreen == ScreenEnum.HOME){
    				HomeScreenInput homeScreenInput = new HomeScreenInput(contacts);
    				homeScreen.loadLater(homeScreenInput);
    			}
    		}
    		else if(body.equals(App.DECLINE_CONTACT_ADD)){

    		}
    	} else if(message.getType() == AppMessageType.CHAT){
    		chatController.incomingChatMessage(message, currentScreen==ScreenEnum.CHAT);
    	}
    }

    /**
     * This is called by BabblerBase when there is a new presence event
     * @param appPresence the new presence
     */
    public void presenceListener(AppPresence appPresence){

    }

    /**
     * This is called by BabblerBase when there is a new roster event
     */
    public void rosterListener(){

    }

    // Screens
    public AccountScreen getAccountScreen(){
    	return accountScreen;
    }

    /**
<<<<<<< HEAD
     * changes the current screen, and updates guiBase accordingly.
     * Certain screens are required to load some input when they are changed to.
=======
     * Set Screen to current View.
>>>>>>> refs/remotes/origin/C1-feature-mucscreen
     * @param screen
     */
    public void setScreen(ScreenEnum screen){
    	
    	this.currentScreen = screen;
    	switch(currentScreen){
			case ACCOUNT:
			{
				guiBase.setScreen(accountScreen);
			} break;
			case HOME:
			{
				HomeScreenInput input = new HomeScreenInput(contacts);
				homeScreen.load(input);
				guiBase.setScreen(homeScreen,navigationScreen);
			} break;
			case MUC:
			{
				MUCScreenInput input = new MUCScreenInput(contacts);
				mucScreen.load(input);
				guiBase.setScreen(mucScreen,navigationScreen);
			} break;
			case CREATEMUC:
			{
				HomeScreenInput input = new HomeScreenInput(contacts);
				createMUCScreen.load(input.getContactList());
				guiBase.setScreen(createMUCScreen,navigationScreen);
			} break;
			case CHAT:
			{
				ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
				chatScreen.load(input);
				guiBase.setScreen(chatScreen,navigationScreen);
			} break;
    	}
    }


}
