package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.controllers.ChatController;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppJid;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.SendTask;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.chat.ChatSession;

public class XmppChatSession {

	private ChatSession chatSession;

	public XmppChatSession(ChatSession chatSession){
		this.chatSession = chatSession;
	}

	public AppJid getChatPartner() {
		Jid jid = chatSession.getChatPartner();
		AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

//	public void addChatPartnerListener() {
//
//	}

	public void close() {
		chatSession.close();
	}

	public void sendMessage(ChatController chatController, AppChatSessionMessage appMessage) {
		Message message = new Message();
		message.setBody(appMessage.getBody());
		message.setType(Message.Type.CHAT);
		SendTask<Message> result = chatSession.sendMessage(message);
		result.onSent(m->chatController.messageSent(appMessage));
//		result.onAcknowledge(m->chatController.messageSent(appMessage));
//		result.onFailed((throwable,m)->failed(throwable, m));
	}

//	public void failed(Throwable throwable, Message message){
//		System.out.println("failed to send");
//	}

	public String getThread(){
		return chatSession.getThread();
	}

}
