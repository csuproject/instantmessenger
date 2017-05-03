package TeamOrange.instantmessenger.models;

public class AppMessage {
	private AppJid jid;
	private String body;
	private String thread; // the context
	private boolean inbound; // inbound = being received

	public static AppMessage createOutboundMessage(AppJid to, String body, String thread){
		AppMessage appMessage = new AppMessage(to, body, thread, false);
		return appMessage;
	}

	public static AppMessage createInboundMessage(AppJid from, String body, String thread){
		AppMessage appMessage = new AppMessage(from, body, thread, true);
		return appMessage;
	}

	private AppMessage(AppJid jid, String body, String thread, boolean inbound){
		this.jid = jid;
		this.body = body;
		this.thread = thread;
		this.inbound = inbound;
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

//	@Override
//	public String toString(){
//		return from.getBareJid() + " : " + body;
//	}

}
