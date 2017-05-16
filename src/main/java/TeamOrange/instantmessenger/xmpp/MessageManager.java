package TeamOrange.instantmessenger.xmpp;

import java.util.LinkedList;

import TeamOrange.instantmessenger.App;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMessageType;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.chat.ChatManager;
import rocks.xmpp.im.chat.ChatSession;

public class MessageManager {

	public MessageManager(){

	}

	public void requestCreateChatSession(XmppClient client, AppJid to, String thread){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		message.setBody(App.REQUEST_CREATE_CHAT_SESSION);
		Jid toJid = jidFromAppJid(to);
		message.setTo(toJid);
		message.setThread(thread);
		client.sendMessage(message);
	}

	public void requestContactAdd(XmppClient client, AppJid to){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		message.setBody(App.REQUEST_CONTACT_ADD);
		Jid toJid = jidFromAppJid(to);
		message.setTo(toJid);
		client.sendMessage(message);
	}

	public void alertUserOfContactRequestResponse(XmppClient client, AppJid to, boolean accepted){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		if(accepted){
			message.setBody(App.ACCEPT_CONTACT_ADD);
		} else {
			message.setBody(App.DECLINE_CONTACT_ADD);
		}
		Jid toJid = jidFromAppJid(to);
		message.setTo(toJid);
		client.sendMessage(message);
	}

	public ChatSession createChatSession(XmppClient client, AppJid to){
		ChatManager chatManager = client.getManager(ChatManager.class);
		ChatSession chatSession = chatManager.createChatSession( jidFromAppJid(to) );
		return chatSession;
	}

	public ChatSession createChatSessionWithGivenThread(XmppClient client, AppJid to, String thread){
		ChatManager chatManager = client.getManager(ChatManager.class);
		ChatSession chatSession = chatManager.createChatSession( jidFromAppJid(to), thread );
		return chatSession;
	}

	// helpers
	private Jid jidFromAppJid(AppJid appJid){
		Jid jid = Jid.of(appJid.getLocal(), appJid.getDomain(), appJid.getResource());
		return jid;
	}

	private AppJid appJidFromJid(Jid jid){
		AppJid appJid = new AppJid( jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

	//AppJid from, String body, String thread, AppMessageType type
	public AppMessage inboundMessageEventToAppMessage(MessageEvent messageEvent){
		AppJid from = appJidFromJid( messageEvent.getMessage().getFrom() );
		String body = messageEvent.getMessage().getBody();
		String thread = messageEvent.getMessage().getThread();
		AppMessageType type = null;
		if(messageEvent.getMessage().getType() == Message.Type.CHAT){
			type = AppMessageType.CHAT;
		} else if(messageEvent.getMessage().getType() == Message.Type.NORMAL){
			type = AppMessageType.NORMAL;
		}
		AppMessage appMessage = AppMessage.createInboundMessage(from, body, thread, type);
		return appMessage;
	}

}
