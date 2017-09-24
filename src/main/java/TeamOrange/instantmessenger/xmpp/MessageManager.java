package TeamOrange.instantmessenger.xmpp;

import java.util.LinkedList;

import TeamOrange.instantmessenger.App;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMessageType;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.SendTask;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.im.chat.ChatManager;
import rocks.xmpp.im.chat.ChatSession;

/**
 * Handles xmpp message management
 *
 */
public class MessageManager {

	private BabblerBase babblerBase;

	public MessageManager(BabblerBase babblerBase){
		this.babblerBase = babblerBase;
	}

	/**
	 * Sends a contact add request to the given user
	 * @param client
	 * @param to the jid of the user to send the reqeust to
	 */
	public void requestContactAdd(XmppClient client, AppJid to){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		message.setBody(App.REQUEST_CONTACT_ADD);
		Jid toJid = JidUtilities.jidFromAppJid(to);
		message.setTo(toJid);
		SendTask<Message> result = client.sendMessage(message);
		result.onSent(m->babblerBase.onRequestContactAddSent(m));
//		result.onAcknowledge(m->onRequestContactAddAcknowledge(m));// Consumer<Message>
	}

	/**
	 * Sends a muc request to the given user
	 * @param client
	 * @param to the jid of the user to send the request to
	 * @param roomID the id of the room
	 */
	public void requestJoinMuc(XmppClient client, AppJid to, String roomID){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		message.setBody(App.REQUEST_JOIN_MUC);
		message.setId(roomID);
		Jid toJid = JidUtilities.jidFromAppJid(to);
		message.setTo(toJid);
		System.out.println("MessageMannager requestJoinMuc toJid: " + toJid + "  roomID: " + roomID);
		SendTask<Message> result = client.sendMessage(message);
//		result.onSent(m->babblerBase.onRequestContactAddSent(m));
	}

	/**
	 * Sends a delete from contacts request to the given user
	 * @param client
	 * @param toAppJid the jid of the user to send the request to
	 */
	public void requestDeleteFromContacts(XmppClient client, AppJid toAppJid){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		message.setBody(App.REQUEST_DELETE_FROM_CONTACTS);
		Jid toJid = JidUtilities.jidFromAppJid(toAppJid);
		message.setTo(toJid);
		SendTask<Message> result = client.sendMessage(message);
	}

//	// TODO: messages never seem to be acknowledged
//	public void onRequestContactAddAcknowledge(Message message){
//		// contact add requested to message.getTo()
//		System.out.println("(acknowledged) contact add request sent to: " +message.getTo() );
//	}

	/**
	 * Alerts the given user of this users response to a contact add request
	 * @param client
	 * @param to the jid of the user to alert
	 * @param accepted true if the contact add request has been accepted, false if declined
	 */
	public void alertUserOfContactRequestResponse(XmppClient client, AppJid to, boolean accepted){
		Message message = new Message();
		message.setType(Message.Type.NORMAL);
		if(accepted){
			message.setBody(App.ACCEPT_CONTACT_ADD);
		} else {
			message.setBody(App.DECLINE_CONTACT_ADD);
		}
		Jid toJid = JidUtilities.jidFromAppJid(to);
		message.setTo(toJid);
		client.sendMessage(message);
	}

	/**
	 * Creates a new  chat session with the given user
	 * @param client
	 * @param to the jid of the user
	 * @return the created chat session
	 */
	public ChatSession createChatSession(XmppClient client, AppJid to){
		ChatManager chatManager = client.getManager(ChatManager.class);
		ChatSession chatSession = chatManager.createChatSession( JidUtilities.jidFromAppJid(to) );
		return chatSession;
	}

	/**
	 * Creates a new  chat session with the given user, and with the given thread
	 * @param client
	 * @param to the jid of the user
	 * @param thread the thread of the chat session
	 * @return the created chat session
	 */
	public ChatSession createChatSessionWithGivenThread(XmppClient client, AppJid to, String thread){
		ChatManager chatManager = client.getManager(ChatManager.class);
		ChatSession chatSession = chatManager.createChatSession( JidUtilities.jidFromAppJid(to), thread );
		return chatSession;
	}

	/**
	 * This turns a MessagEvent to an AppMessage
	 * @param messageEvent the message event to convert
	 * @return the AppMessage that has been created
	 */
	public AppMessage inboundMessageEventToAppMessage(MessageEvent messageEvent){
		AppJid from = null;
		if(messageEvent.getMessage().getFrom() != null){
			from = JidUtilities.appJidFromJid( messageEvent.getMessage().getFrom() );
		}
		String body = messageEvent.getMessage().getBody();
		String thread = messageEvent.getMessage().getThread();
		String id = messageEvent.getMessage().getId();
		AppMessageType type = null;
		if(messageEvent.getMessage().getType() == Message.Type.CHAT){
			type = AppMessageType.CHAT;
		} else if(messageEvent.getMessage().getType() == Message.Type.NORMAL){
			type = AppMessageType.NORMAL;
		}
		AppMessage appMessage = AppMessage.createInboundMessage(from, body, id, thread, type);
		return appMessage;
	}

}
