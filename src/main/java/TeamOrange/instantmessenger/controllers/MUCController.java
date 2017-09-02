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
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.views.MUCScreenInput;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;


public class MUCController {

	private BabblerBase babblerBase;
	private ScreenEnum currentScreen;
	// screens
	private MUCScreen mucScreen;
	private CreateMUCScreen createMUCScreen;
	private ChatScreen chatScreen;
	private NavigationScreen navigationScreen;
	// models
	private AppMucList mucs;
	private AppContacts contacts;
	// controllers
	private ConnectionController connectionController;
	// lambdas
	private ChangeScreen changeScreen;
	private GetMUCEvent notifyMUCEvent;

	public MUCController(BabblerBase babblerBase, ChatScreen chatScreen, NavigationScreen navigationScreen,
			MUCScreen mucScreen, CreateMUCScreen createMUCScreen, AppContacts contacts, AppMucList mucs,
			ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		this.navigationScreen = navigationScreen;
		this.mucScreen = mucScreen;
		this.createMUCScreen = createMUCScreen;
		this.contacts = contacts;
		this.navigationScreen = navigationScreen;
		this.connectionController = connectionController;
		this.mucs = mucs;

		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(openMUCEvent->openChatScreen(openMUCEvent));
		mucScreen.setOnAddMUC(addMUCEvent->actuallyCreateMUC(addMUCEvent));
		mucScreen.setOnAcceptMucRequest( (roomID, from)->acceptMucRequest(roomID, from));
		mucScreen.setOnDeclineMucRequest( (roomID, from)->declineMucRequest(roomID, from));

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
					mucChat.getName(), contacts.getSelfName(), appMUC->notifyMUCEvent.getMUC(appMUC));
			muc.setReference(muc);
			mucs.add(muc);
			this.mucScreen.loadLater( new MUCScreenInput(this.mucs) );
			babblerBase.addChatRoomBookmark(muc.getRoomID(), muc.getNick());
			requestMUC(mucChat);
		} catch (ConfideFailedToEnterChatRoomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
		}
	}

	public void createMUC(MUCChat mucChat){
		if( mucs.getMucIfExists(mucChat.getName()) != null ){
			mucScreen.alertLater("Group Chat \""+mucChat.getName()+"\" already exists", "Group Already Exists", AlertType.INFORMATION);
			return;
		}
		connectionController.addCreateMucWithMucChatTask(this, mucChat);
		connectionController.completeTasks();
	}

	public void actuallyCreateMUC(String roomID) {

		// Create MUC
		AppMuc muc;
		try {
			muc = babblerBase.createAndOrEnterRoom(roomID, contacts.getSelfName(), appMUC->notifyMUCEvent.getMUC(appMUC));
			muc.setReference(muc);
			mucs.add(muc);
			babblerBase.addChatRoomBookmark(muc.getRoomID(), muc.getNick());
			this.mucScreen.loadLater( new MUCScreenInput(this.mucs) );
		} catch (ConfideFailedToEnterChatRoomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
		}
	}

	public void createMUC(String roomID){
		if( mucs.getMucIfExists(roomID) != null ){
			mucScreen.alertLater("Group Chat \""+roomID+"\" already exists", "Group Already Exists", AlertType.INFORMATION);
			return;
		}
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

	/**
	 * Send message to users for group chat invite
	 * @param createMUC
	 */
	public void requestMUC(MUCChat mucChat) {

		List<AppUser> list = mucChat.getUsers();
		for(AppUser user : list){
			babblerBase.requestJoinMuc(user.getJid(), mucChat.getName());
		}

	}

	private void openChatScreen(String roomID) {
		AppMuc muc = mucs.getMucIfExists(roomID);
		mucs.setMucInFocus(muc);
		changeScreen.SetScreen(ScreenEnum.MUCCHAT);
	}

	public void setOnNotifcationEvent(GetMUCEvent notifyMUCEvent) {
		this.notifyMUCEvent = notifyMUCEvent;
	}

	public void acceptMucRequest(String roomID, String from){
		mucs.removeMucRequestsWithRoomID(roomID);
		createMUC(roomID);
		mucScreen.loadLater( new MUCScreenInput(mucs) );
	}

	public void declineMucRequest(String roomID, String from){
		mucs.removeMucRequestsWithRoomID(roomID);
		mucScreen.loadLater( new MUCScreenInput(mucs) );
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
