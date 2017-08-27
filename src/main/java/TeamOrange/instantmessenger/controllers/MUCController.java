package TeamOrange.instantmessenger.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.MUCListEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.MUCContactDisplay;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;
import javafx.application.Platform;

public class MUCController {

	private ChangeScreen changeScreen;
	private AppMucList mucs;
	private BabblerBase babblerBase;
	private MUCScreen mucScreen;
	private AppContacts contacts;
	private MUCListEvent mucListEvent;
	private ChatScreen chatScreen;
	private GetMUCEvent getMUCEvent;
	private GetMUCEvent newMessageMUC;
	private ConnectionController connectionController;


	public MUCController(BabblerBase babblerBase, ChatScreen chatScreen,
			AppContacts contacts, AppMucList mucs, MUCScreen mucScreen, CreateMUCScreen createMUCScreen, ConnectionController connectionController) {

		this.babblerBase = babblerBase;
		this.mucScreen = mucScreen;
		this.contacts = contacts;
		this.chatScreen = chatScreen;
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(getMUCEvent->enterMUC(getMUCEvent));
		mucScreen.setOnAddGroupGetMUCEvent(addMUCEvent->createMUC(addMUCEvent));
		mucScreen.setOnAcceptMucRequest( (roomID, from)->acceptMucRequest(roomID, from));
		mucScreen.setOnDeclineMucRequest( (roomID, from)->declineMucRequest(roomID, from));
		createMUCScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnCreateMUCEvent(createMUCEvent->createMUC(createMUCEvent));
		chatScreen.setOnSendMucMessageEvent((muc, message)->sendMUCMessage(muc, message));
		this.mucs = mucs;
		this.connectionController = connectionController;
	}



	/**
	 * Create MUC
	 * @param muc
	 */
	public void actuallyCreateMUC(MUCChat mucChat) {

		// Create MUC
		AppMuc muc;
		try {
			muc = babblerBase.createAndOrEnterRoom(mucChat.getName(), contacts.getSelfName());
			muc.setReference(muc);
			muc.setOnNewMessage(getMUCEvent-> // Set new message notifier
				newMessageMUC.getMUC(getMUCEvent)
			);
			this.mucs.addMuc(muc);
			this.mucScreen.loadNewLater(this.mucs);
//			mucListEvent.getMUCList(this.mucs.getMucList());
			requestMUC(mucChat);
			babblerBase.addChatRoomBookmark(muc.getRoomID(), muc.getNick());
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
			muc.setOnNewMessage(getMUCEvent-> // Set new message notifier
				newMessageMUC.getMUC(getMUCEvent));
			addtoMUCList(muc);
			babblerBase.addChatRoomBookmark(muc.getRoomID(), muc.getNick());
			//requestMUC(roomID);
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

	public void exitMUC(AppMuc appMUC) {
		appMUC.leave();
		removeFromMUCList(appMUC);
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

	/**
	 * Enter MUC
	 * @param mucName
	 */
	public void enterMUC(String mucName) {
		// Enter MUC
		this.mucs.addMuc(muc(mucName));
		mucListEvent.getMUCList(this.mucs.getMucList());
	}

	/**
	 * Enter MUC
	 * @param mucChat
	 */
	public void enterMUC(AppMuc appMUC) {
		// Enter MUC
		getMUCEvent.getMUC(appMUC);
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

	public AppMuc muc(String mucName) {
		try {
			return babblerBase.createAndOrEnterRoom(
					mucName, contacts.getSelfName());
		} catch (ConfideFailedToEnterChatRoomException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
			return null;
		}
	}

	public void acceptMucRequest(String roomID, String from){
		mucs.removeMucRequest(roomID, from);
		createMUC(roomID);
		mucScreen.loadNewLater(mucs);
	}

	public void declineMucRequest(String roomID, String from){
		mucs.removeMucRequest(roomID, from);
		mucScreen.loadNewLater(mucs);
	}

	private void addtoMUCList(AppMuc appMUC) {
		this.mucs.addMuc(appMUC);
		mucScreen.loadNewLater(this.mucs);
//		mucListEvent.getMUCList(this.mucs.getMucList());
	}

	private void removeFromMUCList(AppMuc appMUC) {
		this.mucs.removeMuc(appMUC);
		mucListEvent.getMUCList(this.mucs.getMucList());
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

	public void setOnMUCListEvent(MUCListEvent mucListEvent) {
		this.mucListEvent = mucListEvent;
	}

	public void setOnOpenMUC(GetMUCEvent getMUCEvent) {
		this.getMUCEvent = getMUCEvent;
	}

	public void setOnNewMessage(GetMUCEvent getMUCEvent) {
		this.newMessageMUC = getMUCEvent;
	}
}
