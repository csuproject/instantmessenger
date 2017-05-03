package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import TeamOrange.instantmessenger.models.AppChats;

public class ChatController {

	private ChatScreen chatScreen;
	private BabblerBase babblerBase;
	private ChangeScreen changeScreen;
	private AppChats chats;

	public ChatController(BabblerBase babblerBase, ChatScreen chatScreen, AppChats chats){
		this.babblerBase = babblerBase;
		this.chatScreen = chatScreen;
		chatScreen.setOnSendNewMessageEvent( userName->chat(userName) );
		this.chats = chats;
	}

	public void chat(String message){
//		chats.getActiveChat().sendMessage(message);
//		ChatScreenInput input = new ChatScreenInput(chats.getActiveChat());
//		chatScreen.load(input);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
