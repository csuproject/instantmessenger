package TeamOrange.instantmessenger.models;

public class AppMessage {
	private AppJid to;
	private String body;
	private String thread; // the context
	private boolean inbound; // inbound = being received

	public AppMessage(AppJid to, String body, String thread, boolean inbound){
		this.to = to;
		this.body = body;
		this.thread = thread;
		this.inbound = inbound;
	}

	public AppJid getToJid(){
		return to;
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
