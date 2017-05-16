package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppMessage;

public class ChatController {

	private ChatScreen chatScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;
	private AppChats chats;

	public ChatController(BabblerBase babblerBase, ChatScreen chatScreen, AppChats chats){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		chatScreen.setOnSendNewMessageEvent( userName->sendChatSessionMessage(userName) );
		this.chats = chats;
	}

	public void sendChatSessionMessage(String message){
		chats.getActiveChat().sendChatMessage(message);
		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
		chatScreen.load(input);
	}

	public void incomingChatMessage(AppMessage message, boolean currentScreenIsChatScreen){
		AppChatSession chatSession = chats.incomingChatMessage(message);
		if(chatSession != null){
			if( currentScreenIsChatScreen  &&  chats.isActiveChat(chatSession) ){
				ChatScreenInput input = new ChatScreenInput(chatSession);
				chatScreen.loadFromDifferentThread(input);
			}
		}
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
