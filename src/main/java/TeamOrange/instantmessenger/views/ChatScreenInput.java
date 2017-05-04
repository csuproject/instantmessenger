package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.models.AppChatSession;

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
	
	/////////////////////////////
	public void printMessages(){
		chatSession.printMessages();
	}

}
