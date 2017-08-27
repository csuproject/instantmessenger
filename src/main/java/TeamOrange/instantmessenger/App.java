package TeamOrange.instantmessenger;

import java.util.ArrayList;
import java.util.List;
import TeamOrange.instantmessenger.controllers.AcceptOrDeclineContactRequestController;
import TeamOrange.instantmessenger.controllers.AddContactController;
import TeamOrange.instantmessenger.controllers.ChatController;
import TeamOrange.instantmessenger.controllers.ConnectionController;
import TeamOrange.instantmessenger.controllers.ConnectionEventEnum;
import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.OpenChatController;
import TeamOrange.instantmessenger.controllers.PresenceController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.controllers.MUCController;
import TeamOrange.instantmessenger.controllers.NavigationController;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppConnection;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMessageType;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.GuiBase;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.views.StatusDisplay;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import javafx.scene.control.Alert.AlertType;

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
	private StatusDisplay statusDisplay;

	// Controllers
	private CreateAccountController createAccountController;
	private LoginController loginController;
	private OpenChatController openChatController;
	private ChatController chatController;
	private AddContactController addContactController;
	private AcceptOrDeclineContactRequestController acceptOrDeclineContactRequestController;
	private PresenceController presenceController;
	private NavigationController naviationController;
	private ConnectionController connectionController;
	private MUCController mucController;


	// models
	AppContacts contacts;
	AppChats chats;
	AppConnection connection;



	public App(GuiBase guiBase){
		this.guiBase = guiBase;

		// views
		accountScreen = new AccountScreen(guiBase);
		homeScreen = new HomeScreen(guiBase);
		chatScreen = new ChatScreen(guiBase);
		navigationScreen = new NavigationScreen(guiBase);
		mucScreen = new MUCScreen(guiBase);
		createMUCScreen = new CreateMUCScreen(guiBase);
		statusDisplay = new StatusDisplay(guiBase);
		//mucList = new ArrayList<AppMuc>();
		//mucinput = new MUCScreenInput();
		guiBase.setScreen(accountScreen);
		setScreen(ScreenEnum.ACCOUNT);

		// models
		contacts = new AppContacts();
		chats = new AppChats();
		connection = new AppConnection( AppConnection.NOT_CONNECTED );

		// xmpp
		babblerBase = new BabblerBase(
				"teamorange.space",
				appMessage->messageListener(appMessage),
				(fromJid, appPresenceType)->presenceListener(fromJid, appPresenceType),
				() -> rosterListener(),
				type->connectionEventListener(type),
				contacts
		);

		connectionController = new ConnectionController(babblerBase, statusDisplay, connection);

		connectionController.setupConnection();
    	connectionController.connect();

    	// controllers
		createAccountController = new CreateAccountController(babblerBase, accountScreen, connectionController);
		createAccountController.setOnChangeScreen( screen->setScreen(screen) );

		loginController = new LoginController(babblerBase, accountScreen, contacts, connectionController);
		loginController.setOnChangeScreen( screen->setScreen(screen) );

		openChatController = new OpenChatController(chats, contacts, babblerBase, homeScreen);
		openChatController.setOnChangeScreen( screen->setScreen(screen) );

		chatController = new ChatController(babblerBase, chatScreen, chats, contacts, connectionController);
		chatController.setOnChangeScreen( screen->setScreen(screen) );
		chatController.setOnExitMUC(exit->mucController.exitMUC(exit));

		addContactController = new AddContactController(babblerBase, homeScreen, contacts, connectionController);
		addContactController.setOnChangeScreen( screen->setScreen(screen) );

		acceptOrDeclineContactRequestController =
				new AcceptOrDeclineContactRequestController(babblerBase, homeScreen, contacts, connectionController);
		acceptOrDeclineContactRequestController.setOnChangeScreen( screen->setScreen(screen) );

		presenceController = new PresenceController(babblerBase, accountScreen, contacts);

		naviationController = new NavigationController(homeScreen, navigationScreen, this);
		naviationController.setOnChangeScreen(screen->setScreen(screen));

		mucController = new MUCController(babblerBase, chatScreen, homeScreen,
				navigationScreen, contacts, mucScreen,
				createMUCScreen, connectionController);
		mucController.setOnChangeScreen(screen->setScreen(screen));
	}

	public void reset(){
		contacts.reset();
		chats.reset();
		connectionController.reset();
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
    			AppUser user = babblerBase.addContact( jid.getBareJid() );
    			contacts.addContact( user );
    			if(currentScreen == ScreenEnum.HOME){
    				HomeScreenInput homeScreenInput = new HomeScreenInput(contacts);
    				homeScreen.loadLater(homeScreenInput);
    			}
    		}
    		else if(body.equals(App.DECLINE_CONTACT_ADD)){

    		}
    	} else if(message.getType() == AppMessageType.CHAT){

    		// Update chats
    		chatController.incomingChatMessage(message, currentScreen==ScreenEnum.CHAT);
    		mucController.loadContactNotifications(message);
    	}
    }

    /**
     * This is called by BabblerBase when there is a new presence event
     * @param appPresence the new presence
     */
    public void presenceListener(AppJid fromJid, AppPresence.Type appPresenceType){
    	presenceController.status(fromJid, appPresenceType);
    	String appUser = fromJid.getLocal();
    	if (appPresenceType == AppPresence.Type.AVAILIBLE) {
    		homeScreen.loadUserOnline(appUser);
    	}
    	if (appPresenceType == AppPresence.Type.UNAVAILIVLE) {
    		homeScreen.loadUserOffline(appUser);
    	}
    }

    /**
     * This is called by BabblerBase when there is a new roster event
     */
    public void rosterListener(){

    }

    public void connectionEventListener(ConnectionEventEnum type){
    	connectionController.onConnectionEvent(type);
    }

    // Screens
    public AccountScreen getAccountScreen(){
    	return accountScreen;
    }

    /**
     * changes the current screen, and updates guiBase accordingly.
     * Certain screens are required to load some input when they are changed to.
	 *
     * Set Screen to current View.
     * @param screen
     */
    public void setScreen(ScreenEnum screen){
    	this.currentScreen = screen;
    	switch(currentScreen){
			case ACCOUNT:
			{
				statusDisplay.setUserNameLater("");
				statusDisplay.setScreenViewLater(ScreenEnum.ACCOUNT);
				guiBase.setScreenLater(statusDisplay, accountScreen);
			} break;
			case HOME:
			{
				mucController.setCurrentScreen(currentScreen);
		    	statusDisplay.setScreenViewLater(currentScreen);
				statusDisplay.setUserNameLater(contacts.getSelfName());
				HomeScreenInput input = new HomeScreenInput(contacts);
				homeScreen.loadLater(input);
				guiBase.setScreenLater(statusDisplay,homeScreen,navigationScreen);
			} break;
			case MUC:
			{
				mucController.setCurrentScreen(currentScreen);
				statusDisplay.setScreenViewLater(currentScreen);
				statusDisplay.setUserNameLater(contacts.getSelfName());
				guiBase.setScreen(statusDisplay,mucScreen,navigationScreen);
			} break;
			case CREATEMUC:
			{
				mucController.setCurrentScreen(currentScreen);
				statusDisplay.setScreenViewLater(currentScreen);
				statusDisplay.setUserNameLater(contacts.getSelfName());
				HomeScreenInput input = new HomeScreenInput(contacts);
				createMUCScreen.load(input.getContactList());
				guiBase.setScreenLater(statusDisplay,createMUCScreen,navigationScreen);
			} break;
			case CHAT:
			{
				mucController.setCurrentScreen(currentScreen);
				statusDisplay.setScreenViewLater(currentScreen);
				statusDisplay.setUserNameLater(contacts.getSelfName());
				ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
				chatScreen.load(input);
				guiBase.setScreenLater(statusDisplay,chatScreen,navigationScreen);
			} break;
				case MUCCHAT:
			{
				mucController.setCurrentScreen(currentScreen);
				statusDisplay.setScreenViewLater(currentScreen);
				statusDisplay.setUserNameLater(contacts.getSelfName());
				chatScreen.loadLater(new ChatScreenInput(mucController.getAppMuc()));
				guiBase.setScreenLater(statusDisplay, chatScreen,navigationScreen);
			} break;
    	}
    }
}
