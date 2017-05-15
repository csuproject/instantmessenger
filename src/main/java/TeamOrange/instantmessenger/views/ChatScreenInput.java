package TeamOrange.instantmessenger.views;

import java.util.LinkedList;

import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;

public class ChatScreenInput {
	private AppChatSession chatSession;

	public ChatScreenInput(AppChatSession chatSession){
		this.chatSession = chatSession;
	}

	public String getPartner(){
		return chatSession.getPartner().getLocal();
	}

	public String getThread(){
		return chatSession.getThread();
	}

	public LinkedList<AppChatSessionMessage> getMessages(){
		return chatSession.getMessages();
	}

	/////////////////////////////
	public void printMessages(){
		chatSession.printMessages();
	}

}
