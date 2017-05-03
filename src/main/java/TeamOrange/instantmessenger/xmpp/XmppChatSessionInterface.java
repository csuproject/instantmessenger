package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;

public interface XmppChatSessionInterface {
	public AppJid getChatPartner();
	public void addChatPartnerListener();
	public void addInboundMessageListener(MessageListener messageListener);
	public void close();
	public void sendMessage(AppMessage message);
	public String getThread();
}
