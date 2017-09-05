package TeamOrange.instantmessenger.models;

/**
 * A simple structure that holds only the necessary data for an AppChatSession message
 *
 */
public class AppChatSessionMessage {
	private String body;
	private boolean inbound;
	private boolean isSent;

	public static AppChatSessionMessage createInbound(String body){
		return new AppChatSessionMessage(body, true);
	}

	public static AppChatSessionMessage createOutbound(String body){
		return new AppChatSessionMessage(body, false);
	}

	public AppChatSessionMessage(String body, boolean inbound){
		this.body = body;
		this.inbound = inbound;
		this.isSent = false;
	}

	public void hasSent(){
		this.isSent = true;
	}

	public boolean sent(){
		return isSent;
	}

	public String getBody(){
		return body;
	}

	public boolean isInbound(){
		return inbound;
	}
}
