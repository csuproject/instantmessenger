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
		System.out.println("partner: "+appChatSession.getPartner()+"\nthread: "+appChatSession.getThread());
		chats.add(appChatSession);
	}

	public void setActiveChat(AppChatSession chat){
		this.activeChat = chat;
	}

	public AppChatSession getActiveChat(){
		return activeChat;
	}

	public void incomingMessage(AppMessage message){
//		String thread = message.getThread();
//		AppChatSession relevantChat = getChatOfThread(thread);
//		System.out.println("relevantChat: " + relevantChat + "\nmessage thread: " + thread + "\nactive chat thread: " + getActiveChat().getThread());
//		if(relevantChat != null){
//			relevantChat.addMessage(message);
//		} else {
//			AppChatSession newSession = new AppChatSession();
//		}
		// creating a chat from two different computers does not work because they are different, their threads are different.
		// when receiving a message, if there is no chat with that thread, then a new chatSession with that thread needs to be created
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
