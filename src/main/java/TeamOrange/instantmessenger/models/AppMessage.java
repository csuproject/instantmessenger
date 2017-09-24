package TeamOrange.instantmessenger.models;

/**
 * Represents a message.
 *
 */
public class AppMessage {
	private AppJid jid;
	private String body;
	private String thread; // the context
	private String id;
	private boolean inbound; // inbound = being received
	private AppMessageType type;

	/**
	 * Creates an outbound message
	 * @param to the jid of the user the message is addressed to
	 * @param body the body of the message
	 * @param thread the thread of the message
	 * @param type the type of the message
	 * @return the created outbound AppMessage
	 */
	public static AppMessage createOutboundMessage(AppJid to, String body, String thread, AppMessageType type){
		AppMessage appMessage = new AppMessage(to, body, null, thread, false, type);
		return appMessage;
	}

	/**
	 * Creates an inbound message
	 * @param from the jid of the user that the message is from
	 * @param id the id of the message
	 * @param body the body of the message
	 * @param thread the thread of the message
	 * @param type the type of the message
	 * @return the created inbound AppMessage
	 */
	public static AppMessage createInboundMessage(AppJid from, String body, String id, String thread, AppMessageType type){
		AppMessage appMessage = new AppMessage(from, body, id, thread, true, type);
		return appMessage;
	}

	private AppMessage(AppJid jid, String body, String id, String thread, boolean inbound, AppMessageType type){
		this.jid = jid;
		this.body = body;
		this.id = id;
		this.thread = thread;
		this.inbound = inbound;
		this.type = type;
	}

	public AppJid getToJid(){
		if(!inbound){
			return jid;
		}
		return null;
	}

	public AppJid getFromJid(){
		if(inbound){
			return jid;
		}
		return null;
	}

	public String getBody(){
		return body;
	}

	public String getThread(){
		return thread;
	}

	public boolean isInbound(){
		return inbound;
	}

	public AppMessageType getType(){
		return type;
	}

	public String getID(){
		return id;
	}

}
