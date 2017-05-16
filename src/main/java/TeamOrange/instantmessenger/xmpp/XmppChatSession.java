package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import rocks.xmpp.addr.Jid;
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

	public void sendMessage(String body) {
		Message message = new Message();
		message.setBody(body);
		message.setType(Message.Type.CHAT);
		chatSession.sendMessage(message);
	}

	public String getThread(){
		return chatSession.getThread();
	}

}
