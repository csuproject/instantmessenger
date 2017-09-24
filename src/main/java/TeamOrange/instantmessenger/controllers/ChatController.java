package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMessage;

/**
 * controls the flow when the user is engaging in a chat session
 *
 */
public class ChatController {

	private ChatScreen chatScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;
	private AppChats chats;
	private AppContacts contacts;
	GetMUCEvent exitMUC;
	private ConnectionController connectionController;

	public ChatController(BabblerBase babblerBase, ChatScreen chatScreen,
			AppChats chats, AppContacts contacts, ConnectionController connectionController){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		chatScreen.setOnSendNewMessageEvent( (message, userName)->sendChatSessionMessage(message, userName) );
		chatScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		chatScreen.setOnExitMUC(exit->exitMUC.getMUC(exit));
		this.chats = chats;
		this.contacts = contacts;
		this.connectionController = connectionController;
	}

	/**
	 * This is called by ChatScren when the onSendNewMessageEvent occurs
	 * Sends the message to the active chat, and reloads the chat screen
	 * @param message the message to send
	 * @param userName the username of the user to send the message to.
	 */
	public void sendChatSessionMessage(String message, String userName){
		// add unsent messge to messages and update screen
		AppChatSessionMessage appMessage = chats.getChatWithContact(userName).addUnsentMessage(message);
		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
		chatScreen.loadLater(input);
		// send message when  connected
		connectionController.addSendChatSessionMessageTask(this, appMessage, userName);
		connectionController.completeTasks();

	}

	/**
	 * This actually sends the message.
	 * It is handled by contactController so that it can be ensured that there is a connection first.
	 * @param message the message to send
	 * @param userName the username of the user to send the message to
	 */
	public void actuallySendChatSessionMessage(AppChatSessionMessage message, String userName){
		chats.getChatWithContact(userName).sendChatMessage(this, message);
//		reload to see the sent message
		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
		chatScreen.loadLater(input);
	}

	/**
	 * This is called when a message is successfully sent, so that the view can update according to this information.
	 * @param message the message that has been sent
	 */
	public void messageSent(AppChatSessionMessage message){
		message.hasSent();
		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
		chatScreen.loadLater(input);
	}

	/**
	 * Called when there is an incoming chat session message
	 * Retrieves the relevant chat if it exists, or creates one if the relevant user is a contact
	 * passes the message to the relevant chat to handle
	 * if the relevant chat session is the current chat session, and teh current screen is the chat scren,
	 * 		then the chatscreen re loads
	 * @param message the message that has been recieved
	 * @param currentScreenIsChatScreen this is true if the current screen is the chat screen, or false otherwise
	 */
	public void incomingChatMessage(AppMessage message, boolean currentScreenIsChatScreen){
		AppChatSession chatSession = chats.getChatOfThread(message.getThread());
		if(chatSession == null){
			if(contacts.containsBareJid(message.getFromJid().getBareJid())){
				// no chat session existed, but the user is in our contacts, so we will create a chat session
				chatSession = babblerBase.createChatSessionWithGivenThread(message.getFromJid(), message.getThread());
				chats.addChat(chatSession);
			}
		}

		if(chatSession != null){
			// chat session already existed or was created
			// handle message
			chats.handleMessage(message);
			if( currentScreenIsChatScreen  &&  chats.isActiveChat(chatSession) ){
				ChatScreenInput input = new ChatScreenInput(chatSession);
				chatScreen.loadLater(input);
			}
		}
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

	public void setOnExitMUC(GetMUCEvent exitMUC) {
		this.exitMUC = exitMUC;
	}

}
