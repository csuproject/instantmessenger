package TeamOrange.instantmessenger.models;

public class AppMessage {
	private AppJid jid;
	private String body;
	private String thread; // the context
	private String id;
	private boolean inbound; // inbound = being received
	private AppMessageType type;

	public static AppMessage createOutboundMessage(AppJid to, String body, String thread, AppMessageType type){
		AppMessage appMessage = new AppMessage(to, body, null, thread, false, type);
		return appMessage;
	}

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
