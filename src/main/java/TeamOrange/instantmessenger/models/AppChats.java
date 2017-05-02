package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppChats {
	LinkedList<AppChatSession> chats;

	public AppChats(){
		chats = new LinkedList<AppChatSession>();
	}

	public void addChat(AppChatSession appChatSession){
		chats.add(appChatSession);
	}
}
