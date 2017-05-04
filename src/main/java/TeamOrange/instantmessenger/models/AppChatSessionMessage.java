package TeamOrange.instantmessenger.models;

public class AppChatSessionMessage {
	String body;
	boolean inbound;

	public static AppChatSessionMessage createInbound(String body){
		return new AppChatSessionMessage(body, true);
	}

	public static AppChatSessionMessage createOutbound(String body){
		return new AppChatSessionMessage(body, false);
	}

	public AppChatSessionMessage(String body, boolean inbound){
		this.body = body;
		this.inbound = inbound;
	}

	public String getBody(){
		return body;
	}

	public boolean isInbound(){
		return inbound;
	}
}
