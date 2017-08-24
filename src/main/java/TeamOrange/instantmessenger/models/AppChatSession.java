package TeamOrange.instantmessenger.models;

import java.util.Iterator;
import java.util.LinkedList;

import TeamOrange.instantmessenger.controllers.ChatController;
import TeamOrange.instantmessenger.xmpp.MessageListener;
import TeamOrange.instantmessenger.xmpp.XmppChatSession;

/**
 * A chat between two users.
 * Holds and manages a list of messages.
 *
 */
public class AppChatSession {
	private XmppChatSession xmppChatSession;
	private LinkedList<AppChatSessionMessage> messages;

	public AppChatSession(XmppChatSession xmppChatSession){
		this.xmppChatSession = xmppChatSession;
		this.messages = new LinkedList<AppChatSessionMessage>();
	}

	public AppJid getPartner(){
		return xmppChatSession.getChatPartner();
	}

	public String getThread(){
		return xmppChatSession.getThread();
	}

	public void addMessage(AppChatSessionMessage message){
		messages.add(message);
	}

	/**
	 * Sends a message with the given body,
	 * Adds the message to the message list
	 * @param body the body of the message to send
	 */
	public void sendChatMessage(ChatController controller, AppChatSessionMessage message){
//		AppChatSessionMessage message = AppChatSessionMessage.createOutbound(body);
//		messages.add(message);
		xmppChatSession.sendMessage(controller, message);
	}

	public AppChatSessionMessage addUnsentMessage(String message){
		AppChatSessionMessage appMessage = AppChatSessionMessage.createOutbound(message);
		messages.add(appMessage);
		return appMessage;
	}

	public void printMessages(){
		Iterator<AppChatSessionMessage> i = messages.iterator();
		while(i.hasNext()){
			AppChatSessionMessage message = i.next();
			if(message.isInbound()){
				System.out.println(getPartner() + " : " + message.getBody());
			} else{
				System.out.println("self : " + message.getBody());
			}
		}
	}

	public LinkedList<AppChatSessionMessage> getMessages(){
		return messages;
	}

}
