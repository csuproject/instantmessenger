package TeamOrange.instantmessenger.models;

import java.util.Iterator;
import java.util.LinkedList;

import TeamOrange.instantmessenger.xmpp.XmppChatSession;

public class AppChatSession {
	private XmppChatSession xmppChatSession;
	private LinkedList<AppMessage> messages;

	public AppChatSession(XmppChatSession xmppChatSession){
		this.xmppChatSession = xmppChatSession;
		this.messages = new LinkedList<AppMessage>();
	}

	public AppJid getPartner(){
		return xmppChatSession.getChatPartner();
	}

	public String getThread(){
		return xmppChatSession.getThread();
	}

	public void addMessage(AppMessage message){
		messages.add(message);
	}

	public void sendMessage(String body){
		AppJid to = xmppChatSession.getChatPartner();
		String thread = xmppChatSession.getThread();
		boolean inbound = false;
		AppMessage appMessage = new AppMessage(to, body, thread, inbound);
		xmppChatSession.sendMessage(appMessage);
		messages.add(appMessage);
	}

	public void printMessages(){
		Iterator<AppMessage> i = messages.iterator();
		while(i.hasNext()){
			AppMessage message = i.next();
			if(message.isInbound()){
				System.out.println(getPartner() + " : " + message.getBody());
			} else{
				System.out.println("self : " + message.getBody());
			}
		}
	}

}
