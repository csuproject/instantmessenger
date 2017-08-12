package TeamOrange.instantmessenger.views;

import java.util.LinkedList;

import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppMuc;

public class ChatScreenInput implements ScreenInput {
	private AppChatSession chatSession;
	private AppMuc muc;
	private boolean forMuc;

	public ChatScreenInput(AppChatSession chatSession){
		this.chatSession = chatSession;
		this.muc = null;
		this.forMuc = false;
	}

	public ChatScreenInput(AppMuc muc){
		this.chatSession = null;
		this.muc = muc;
		this.forMuc = true;
	}

	public AppMuc getMuc(){
		return muc;
	}

	public boolean isForMuc(){
		return forMuc;
	}

	public boolean isForChatSession(){
		return !forMuc;
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
