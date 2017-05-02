package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppChats {
	LinkedList<AppChatSession> chats;
	AppChatSession activeChat;

	public AppChats(){
		chats = new LinkedList<AppChatSession>();
	}

	public void addChat(AppChatSession appChatSession){
		chats.add(appChatSession);
	}

	public void setActiveChat(AppChatSession chat){
		this.activeChat = chat;
	}

	public AppChatSession getActiveChat(){
		return activeChat;
	}
}
