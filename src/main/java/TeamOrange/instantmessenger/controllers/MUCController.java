package TeamOrange.instantmessenger.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.MUCListEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMuc;
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
	private List<AppMuc> mucList;
	private BabblerBase babblerBase;
	private MUCScreen mucScreen;
	private AppContacts contacts;
	private MUCListEvent mucListEvent;
	private ChatScreen chatScreen;
	private GetMUCEvent getMUCEvent;
	private GetMUCEvent newMessageMUC;
	private ConnectionController connectionController;


	public MUCController(BabblerBase babblerBase, ChatScreen chatScreen,
			AppContacts contacts, MUCScreen mucScreen, CreateMUCScreen createMUCScreen, ConnectionController connectionController) {

		this.babblerBase = babblerBase;
		this.mucScreen = mucScreen;
		this.contacts = contacts;
		this.chatScreen = chatScreen;
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(getMUCEvent->enterMUC(getMUCEvent));
		mucScreen.setOnAddGroupGetMUCEvent(addMUCEvent->createMUC(addMUCEvent));
		createMUCScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnCreateMUCEvent(createMUCEvent->createMUC(createMUCEvent));
		chatScreen.setOnSendMucMessageEvent((muc, message)->sendMUCMessage(muc, message));
		mucList = new ArrayList<AppMuc>();
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
			mucList.add(muc);
			mucListEvent.getMUCList(mucList);
			requestMUC(mucChat);
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

	}

	/**
	 * Enter MUC
	 * @param mucName
	 */
	public void enterMUC(String mucName) {
		// Enter MUC
		mucList.add(muc(mucName));
		mucListEvent.getMUCList(mucList);
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

	private void addtoMUCList(AppMuc appMUC) {
		mucList.add(appMUC);
		mucListEvent.getMUCList(mucList);
	}

	private void removeFromMUCList(AppMuc appMUC) {
		mucList.remove(appMUC);
		mucListEvent.getMUCList(mucList);
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
