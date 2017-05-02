package TeamOrange.instantmessenger.models;

import java.util.Iterator;
import java.util.LinkedList;

public class AppChatSession {
	private AppJid partner;
	private String thread;
	private LinkedList<AppMessage> messages;

	public AppChatSession(AppJid partner, String thread){
		this.partner = partner;
		this.thread = thread;
		this.messages = new LinkedList<AppMessage>();
	}

	public AppJid getPartner(){
		return partner;
	}

	public String getThread(){
		return thread;
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
