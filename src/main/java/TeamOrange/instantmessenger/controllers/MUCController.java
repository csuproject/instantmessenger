package TeamOrange.instantmessenger.controllers;

import java.util.ArrayList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.HomeScreenInput;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.views.MUCScreenInput;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;

public class MUCController {

	private ChangeScreen changeScreen;
	private BabblerBase babblerBase;
	private MUCScreen mucScreen;
	private AppContacts contacts;
	private ChatScreen chatScreen;
	private NavigationScreen navigationScreen;
	private ConnectionController connectionController;
	private ScreenEnum currentScreen;
	private AppMucList mucs;
	private GetMUCEvent notifyMUCEvent;


	public MUCController(BabblerBase babblerBase, ChatScreen chatScreen,
			HomeScreen homeScreen, NavigationScreen navigationScreen, 
			AppContacts contacts, MUCScreen mucScreen, 
			CreateMUCScreen createMUCScreen, 
			ConnectionController connectionController,
			AppMucList mucs) {

		this.babblerBase = babblerBase;
		this.mucScreen = mucScreen;
		this.contacts = contacts;
		this.chatScreen = chatScreen;
		this.navigationScreen = navigationScreen;
		this.connectionController = connectionController;
		this.mucs = mucs;
		
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(openMUCEvent->openChatScreen(openMUCEvent));
		mucScreen.setOnAddMUC(addMUCEvent->actuallyCreateMUC(addMUCEvent));
		createMUCScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnCreateMUCEvent(createMUCEvent->createMUC(createMUCEvent));
		chatScreen.setOnSendMucMessageEvent((muc, message)->sendMUCMessage(muc, message));
	}



	/**
	 * Create MUC
	 * @param muc
	 */
	public void actuallyCreateMUC(MUCChat mucChat) {

		// Create MUC
		AppMuc muc;
		try {
			muc = babblerBase.createAndOrEnterRoom(
					mucChat.getName(), contacts.getSelfName());
			muc.setReference(muc);
			muc.setOnNewMessage(appMUC->notifyMUCEvent.getMUC(appMUC));
			mucs.add(muc);
			loadMUCScreen();
		} catch (ConfideFailedToEnterChatRoomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
		}
	}

	public void createMUC(MUCChat mucChat){
		connectionController.addCreateMucWithMucChatTask(this, mucChat);
		connectionController.completeTasks();
	}

	public void actuallyCreateMUC(String roomID) {

		// Create MUC
		AppMuc muc;
		try {
			muc = babblerBase.createAndOrEnterRoom(roomID, contacts.getSelfName());
			muc.setReference(muc);
			muc.setOnNewMessage(appMUC->notifyMUCEvent.getMUC(appMUC));
			mucs.add(muc);
			loadMUCScreen();
		} catch (ConfideFailedToEnterChatRoomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
		}
	}

	public void createMUC(String roomID){
		connectionController.addCreateMucWithRoomIDTask(this, roomID);
		connectionController.completeTasks();
	}

	/**
	 * Send message to focused MUC
	 * @param message
	 */
	public void actuallySendMUCMessage(AppMuc muc, String message) {
		muc.sendMessage(message);
	}

	public void sendMUCMessage(AppMuc muc, String message){
		muc.addUnsentMessage(message, contacts.getSelf().getJid().getLocal());
		ChatScreenInput input = new ChatScreenInput(muc);
		chatScreen.loadLater(input);

		connectionController.addSendMucMessageTask(this, muc, message);
		connectionController.completeTasks();
	}

	private void openChatScreen(String roomID) {
		AppMuc muc = mucs.getMucIfExists(roomID);
		mucs.setMUCInFocus(muc);
		changeScreen.SetScreen(ScreenEnum.MUCCHAT);
	}
	
	private void loadMUCScreen() {
		mucScreen.loadLater(new MUCScreenInput(mucs));
	}
	
	public void setOnNotifcationEvent(GetMUCEvent notifyMUCEvent) {
		this.notifyMUCEvent = notifyMUCEvent;
	}
	
	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
