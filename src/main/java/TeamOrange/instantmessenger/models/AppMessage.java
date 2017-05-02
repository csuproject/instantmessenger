package TeamOrange.instantmessenger.models;

public class AppMessage {
	private AppJid jid;
	private String body;
	private String thread; // the context
	private boolean inbound; // inbound = being received

	public AppMessage(AppJid jid, String body, String thread, boolean inbound){
		this.jid = jid;
		this.body = body;
		this.thread = thread;
		this.inbound = inbound;
	}

	public AppJid getJid(){
		return jid;
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

	@Override
	public String toString(){
		return jid.getBareJid() + " : " + body;
	}

}
