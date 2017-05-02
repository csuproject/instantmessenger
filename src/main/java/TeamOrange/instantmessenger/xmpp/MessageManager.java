package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;

public class MessageManager {

	public MessageManager(){

	}

	public AppMessage messageEventToAppMessage(MessageEvent messageEvent){
		Message xmppMessage = messageEvent.getMessage();
		AppJid appJid = appJidFromjid(xmppMessage.getFrom());
		AppMessage appMessage = new AppMessage( appJid, xmppMessage.getBody(), xmppMessage.getThread(), messageEvent.isInbound() );
		return appMessage;
	}

	public void sendMessage(XmppClient client, AppMessage appMessage){
		Message message = messageFromAppMessage(appMessage);
		client.sendMessage(message);
	}

	private Message messageFromAppMessage(AppMessage appMessage){
		Message message = new Message();

		message.setTo( jidFromAppJid(appMessage.getJid()) );
		message.setBody(appMessage.getBody());
		message.setThread(appMessage.getThread());
		return message;
	}

	private Jid jidFromAppJid(AppJid appJid){
		Jid jid = Jid.of(appJid.getLocal(), appJid.getDomain(), appJid.getResource());
		return jid;
	}

	private AppJid appJidFromjid(Jid jid){
		AppJid appJid = new AppJid( jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

}
