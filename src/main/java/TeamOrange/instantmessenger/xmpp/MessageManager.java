package TeamOrange.instantmessenger.xmpp;

import java.util.LinkedList;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.chat.ChatManager;
import rocks.xmpp.im.chat.ChatSession;

public class MessageManager {

	public MessageManager(){

	}

	public void sendMessage(XmppClient client, AppMessage appMessage){
		Message message = messageFromAppMessage(appMessage);
		client.sendMessage(message);
	}

	public ChatSession createChat(XmppClient client, AppJid partner){
		ChatManager chatManager = client.getManager(ChatManager.class);
		Jid chatPartner = Jid.ofLocalAndDomain(partner.getLocal(), partner.getDomain());
		ChatSession chatSession = chatManager.createChatSession(chatPartner);
		return chatSession;
	}

	// helpers

	public AppMessage messageEventToAppMessage(MessageEvent messageEvent){
		Message xmppMessage = messageEvent.getMessage();
		//AppJid from = appJidFromjid(xmppMessage.getFrom());
		AppMessage appMessage = new AppMessage( null, xmppMessage.getBody(), xmppMessage.getThread(), messageEvent.isInbound() );
		return appMessage;
	}

	private Message messageFromAppMessage(AppMessage appMessage){
		Message message = new Message();

		message.setTo( jidFromAppJid(appMessage.getToJid()) );
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
