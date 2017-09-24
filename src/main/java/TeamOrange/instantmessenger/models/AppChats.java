package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.views.ChatScreenInput;
import TeamOrange.instantmessenger.xmpp.XmppChatSession;

/**
 * Holds and manages a list of AppChatSession
 *
 */
public class AppChats {
	private LinkedList<AppChatSession> chats;
	private AppChatSession activeChat;

	public AppChats(){
		chats = new LinkedList<AppChatSession>();
	}

	/**
	 * Resets this object.
	 */
	public void reset(){
		chats.clear();
		activeChat = null;
	}

	/**
	 * Adds the given chat to the list of chats
	 * @param appChatSession the chat session to add
	 */
	public void addChat(AppChatSession appChatSession){
		chats.add(appChatSession);
	}

	/**
	 * Removes the chat with the user with the given username, if such a chat exists.
	 * @param userName the username of the user who's chat is to be deleted
	 */
	public void removeChat(String userName){
		for(int i = 0; i < chats.size(); ++i){
			if(chats.get(i).getPartner().getJid().getLocal().equals(userName)){
				chats.remove(i);
				return;
			}
		}
	}

	/**
	 * sets the given chat to the active chat
	 * @param chat the AppChatSession to set to active
	 */
	public void setActiveChat(AppChatSession chat){
		this.activeChat = chat;
	}

	/**
	 * @return the active chat
	 */
	public AppChatSession getActiveChat(){
		return activeChat;
	}

	/**
	 * If the relevant chat exists (one with the same thread) then it adds the message to that chat
	 * @param message the message to be added
	 */
	public void handleMessage(AppMessage message){
		String thread = message.getThread();
		AppChatSession relevantChat = getChatOfThread(thread);
		if(relevantChat != null){
			AppChatSessionMessage m = new AppChatSessionMessage(message.getBody(), message.isInbound());
			relevantChat.addMessage(m);
		}
	}

	/**
	 * Returns the chat with the given thread, or null
	 * @param thread the thread to look for
	 * @return the AppChatSession with the given thread, or null
	 */
	public AppChatSession getChatOfThread(String thread){
		for(AppChatSession chat : chats){
			if(chat.getThread().equals(thread)){
				return chat;
			}
		}
		return null;
	}

	/**
	 * Returns the chat whos partener has the given username
	 * @param username the chat partener username
	 * @return The AppChatSession with the given partener, or null
	 */
	public AppChatSession getChatWithContact(String username){
		for(AppChatSession chat : chats){
			if(chat.getPartner().getJid().getLocal().equals(username)){
				return chat;
			}
		}
		return null;
	}

	/**
	 * returns if the given chat is the active chat
	 * @param chat the chat to check for
	 * @return if the given chat is the active chat
	 */
	public boolean isActiveChat(AppChatSession chat){
		return chat == activeChat;
	}
}
