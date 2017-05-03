package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.chat.ChatSession;

public class XmppChatSession implements XmppChatSessionInterface {

	private ChatSession chatSession;

	public XmppChatSession(ChatSession chatSession){
		this.chatSession = chatSession;
	}

	@Override
	public AppJid getChatPartner() {
		Jid jid = chatSession.getChatPartner();
		AppJid appJid = new AppJid(jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

	@Override
	public void addChatPartnerListener() {

	}

	//(AppJid to, String body, String thread, boolean inbound
	@Override
	public void addInboundMessageListener(MessageListener messageListener) {
//		chatSession.addInboundMessageListener(e->{
//			AppMessage appMessage = new AppMessage( null, e.getMessage().getBody(), e.getMessage().getThread(), true );
//			messageListener.message(appMessage);
//		});
	}

	@Override
	public void close() {
		chatSession.close();
	}

	@Override
	public void sendMessage(AppMessage appMessage) {
		Message message = new Message();
		message.setBody(appMessage.getBody());
		chatSession.sendMessage(message);
	}

	@Override
	public String getThread(){
		return chatSession.getThread();
	}



}
