package TeamOrange.instantmessenger.xmpp;

import java.awt.TrayIcon.MessageType;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
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

	public void addChatPartnerListener() {

	}

	public void addInboundMessageListener(MessageListener messageListener) {
//		chatSession.addInboundMessageListener(e->{
//			AppMessage appMessage = new AppMessage( null, e.getMessage().getBody(), e.getMessage().getThread(), true );
//			messageListener.message(appMessage);
//		});
	}

	public void close() {
		chatSession.close();
	}

	public void sendMessage(String body) {
		Message message = new Message();
		message.setBody(body);
		message.setType(Message.Type.CHAT);
		// TODO: assuming that it automatically adds in thread and to
		chatSession.sendMessage(message);
	}

	public String getThread(){
		return chatSession.getThread();
	}

}
