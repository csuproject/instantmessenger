package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.xmpp.XmppChatSession;

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

	public void incomingChatMessage(AppMessage message){
		String thread = message.getThread();
		AppChatSession relevantChat = getChatOfThread(thread);
		if(relevantChat != null){
			AppChatSessionMessage m = new AppChatSessionMessage(message.getBody(), message.isInbound());
			relevantChat.addMessage(m);
			System.out.println(message.getBody());
		}
	}

	public AppChatSession getChatOfThread(String thread){
		for(AppChatSession chat : chats){
			if(chat.getThread().equals(thread)){
				return chat;
			}
		}
		return null;
	}
}
