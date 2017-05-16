package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.XmppChatSession;

public class AppChats {
	private LinkedList<AppChatSession> chats;
	private AppChatSession activeChat;

	public AppChats(){
		chats = new LinkedList<AppChatSession>();
	}

	public void addChat(AppChatSession appChatSession){
		chats.add(appChatSession);
		System.out.println(appChatSession.getPartner() + " : " + appChatSession.getThread());
	}

	public void setActiveChat(AppChatSession chat){
		this.activeChat = chat;
	}

	public AppChatSession getActiveChat(){
		return activeChat;
	}

	public AppChatSession incomingChatMessage(AppMessage message){
		String thread = message.getThread();
		AppChatSession relevantChat = getChatOfThread(thread);
		if(relevantChat != null){
			AppChatSessionMessage m = new AppChatSessionMessage(message.getBody(), message.isInbound());
			relevantChat.addMessage(m);
			System.out.println(message.getBody());
		}
		return relevantChat;
	}

	public AppChatSession getChatOfThread(String thread){
		for(AppChatSession chat : chats){
			if(chat.getThread().equals(thread)){
				return chat;
			}
		}
		return null;
	}

	public AppChatSession getChatWithContact(String username){
		for(AppChatSession chat : chats){
			if(chat.getPartner().getLocal().equals(username)){
				return chat;
			}
		}
		return null;
	}

	public boolean isActiveChat(AppChatSession chat){
		return chat == activeChat;
	}
}
