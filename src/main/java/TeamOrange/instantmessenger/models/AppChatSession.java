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

	public void printMessages(){
		Iterator<AppMessage> i = messages.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
	}

}
