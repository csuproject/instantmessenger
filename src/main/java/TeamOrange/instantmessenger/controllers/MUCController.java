package TeamOrange.instantmessenger.controllers;

import java.util.ArrayList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.MUCRoomEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.views.MucInviteScreen;
import TeamOrange.instantmessenger.views.MucInviteScreenInput;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.views.MUCScreenInput;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

/**
 * Controlls the flow for muc events.
 * Such as: creating a muc, deleting a muc, inviting contacts to a muc, sending messages to a muc, accepting muc requests, declining muc requests, notifications.
 */
public class MUCController {

	private BabblerBase babblerBase;
	private ScreenEnum currentScreen;
	// screens
	private MUCScreen mucScreen;
	private MucInviteScreen mucInviteScreen;
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
			MUCScreen mucScreen, MucInviteScreen mucInviteScreen, AppContacts contacts, AppMucList mucs,
			ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		this.navigationScreen = navigationScreen;
		this.mucScreen = mucScreen;
		this.mucInviteScreen = mucInviteScreen;
		this.contacts = contacts;
		this.navigationScreen = navigationScreen;
		this.connectionController = connectionController;
		this.mucs = mucs;

		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(openMUCEvent->openChatScreen(openMUCEvent));
		mucScreen.setOnAddMUC(addMUCEvent->actuallyCreateMUC(addMUCEvent));
		mucScreen.setOnAcceptMucRequest( (roomID, from)->acceptMucRequest(roomID, from));
		mucScreen.setOnDeclineMucRequest( (roomID, from)->declineMucRequest(roomID, from));
		mucScreen.setOnOpenInviteMucScreenEvent( muc->openInviteMucScreen(muc) );
		mucScreen.setOnDeleteMUCEvent( muc->mucDeleteButtonPress(muc) );
		mucInviteScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucInviteScreen.setOnInviteToMucEvent( users->inviteUsersToMuc(users) );

		chatScreen.setOnSendMucMessageEvent((muc, message)->sendMUCMessage(muc, message));
	}

	/**
	 * Changes to the MUC_INVITE screen.
	 * Sets the muc that is currently being invited for.
	 * @param muc the muc that is being invited for
	 */
	public void openInviteMucScreen(AppMuc muc){
		mucs.setMucInvitingTo(muc);
		System.out.println("MUCController muc inviting to: " + mucs.getMucInvitingTo());
		changeScreen.SetScreen(ScreenEnum.MUC_INVITE);
//		mucInviteScreen.loadLater(new MucInviteScreenInput(mucs, contacts));
	}

	/**
	 * This is called when a muc is to be deleted.
	 * Deletes the muc locally and removes the bookmark from the server.
	 * @param muc the muc that is to be deleted
	 */
	public void mucDeleteButtonPress(AppMuc muc){
		String roomName = muc.getRoomID();
		String nick = muc.getNick();
		babblerBase.removeChatRoomBookmark(roomName, nick);
		mucs.removeMuc(muc);
		mucScreen.loadLater( new MUCScreenInput(this.mucs) );
	}

	/**
	 * Invites the given list of users to the muc that is set as the current muc to invite to.
	 * @param users the list of users to invite to the muc
	 */
	public void inviteUsersToMuc(List<AppUser> users){
		for(AppUser user : users){
			babblerBase.requestJoinMuc(user.getJid(), mucs.getMucInvitingTo().getRoomID());
		}
	}

	/**
	 * Creates a muc.
	 * This is used by connectionController.
	 * @param roomID the id of the room to create
	 */
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

	/**
	 * This is called when it is requested for a room to be created.
	 * Differs the creation of the room to connectionController, so that it can be ensured that the app has a connection first.
	 * @param roomID the id of the room to create.
	 */
	public void createMUC(String roomID){
		if( mucs.getMucIfExists(roomID) != null ){
			mucScreen.alertLater("Group Chat \""+roomID+"\" already exists", "Group Already Exists", AlertType.INFORMATION);
			return;
		}
		connectionController.addCreateMucWithRoomIDTask(this, roomID);
		connectionController.completeTasks();
	}

	/**
	 * Send message to the given muc.
	 * This is used by connectionController.
	 * @param muc the muc to send the message to
	 * @param message the message to send
	 */
	public void actuallySendMUCMessage(AppMuc muc, String message) {
		muc.sendMessage(message);
	}

	/**
	 * This is called when it is requested for a message to be sent to a muc.
	 * An unsent message is added to the muc.
	 * The sending is differed to conntectionController to ensure that the app has a connection first.
	 * @param muc the muc to send the message to
	 * @param message the message to send
	 */
	public void sendMUCMessage(AppMuc muc, String message){
		muc.addUnsentMessage(message, contacts.getSelf().getJid().getLocal());
		ChatScreenInput input = new ChatScreenInput(muc);
		chatScreen.loadLater(input);

		connectionController.addSendMucMessageTask(this, muc, message);
		connectionController.completeTasks();
	}

	/**
	 * Changes to screen to the MUCCHAT screen, displaying the given muc
	 * @param roomID the id of the muc to display
	 */
	private void openChatScreen(String roomID) {
		AppMuc muc = mucs.getMucIfExists(roomID);
		mucs.setMucInFocus(muc);
		changeScreen.SetScreen(ScreenEnum.MUCCHAT);
	}

	public void setOnNotifcationEvent(GetMUCEvent notifyMUCEvent) {
		this.notifyMUCEvent = notifyMUCEvent;
	}

	/**
	 * Accepts a muc request, creating that muc, and removing the request.
	 * @param roomID the id of the muc to create
	 * @param from the contact that the request is from
	 */
	public void acceptMucRequest(String roomID, String from){
		mucs.removeMucRequestsWithRoomID(roomID);
		createMUC(roomID);
		mucScreen.loadLater( new MUCScreenInput(mucs) );
	}

	/**
	 * Declines a muc request, removing the request.
	 * @param roomID the id of the muc to decline
	 * @param from the contact that the request is from
	 */
	public void declineMucRequest(String roomID, String from){
		mucs.removeMucRequestsWithRoomID(roomID);
		mucScreen.loadLater( new MUCScreenInput(mucs) );
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
