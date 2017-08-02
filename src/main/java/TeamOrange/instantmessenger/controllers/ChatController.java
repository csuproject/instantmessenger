package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import TeamOrange.instantmessenger.models.AppChatSession;
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

	public ChatController(BabblerBase babblerBase, ChatScreen chatScreen, 
			AppChats chats, AppContacts contacts){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		chatScreen.setOnSendNewMessageEvent( userName->sendChatSessionMessage(userName) );
		this.chats = chats;
		this.contacts = contacts;
	}

	/**
	 * This is called by ChatScren when the onSendNewMessageEvent occurs
	 * Sends the message to the active chat, and reloads the chat screen
	 * @param message
	 */
	public void sendChatSessionMessage(String message){
		chats.getActiveChat().sendChatMessage(message);
		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
		chatScreen.load(input);
	}

	/**
	 * Called when there is an incoming chat session message
	 * Retrieves the relevant chat if it exists, or creates one if the relevant user is a contact
	 * passes the message to the relevant chat to handle
	 * if the relevant chat session is the current chat session, and teh current screen is the chat scren,
	 * 		then the chatscreen re loads
	 * @param message
	 * @param currentScreenIsChatScreen
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

}
