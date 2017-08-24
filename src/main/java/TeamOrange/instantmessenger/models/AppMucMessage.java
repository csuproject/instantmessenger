package TeamOrange.instantmessenger.models;

/**
 * Represents a muc message
 *
 */
public class AppMucMessage {
	private String body;
	private String fromNick;
	private boolean sent;
	private boolean sentFromSelf;

	private AppMucMessage(String body, String fromNick, boolean sent, boolean sentFromSelf){
		this.body = body;
		this.fromNick = fromNick;
		this.sent = sent;
		this.sentFromSelf = sentFromSelf;
	}

	public static AppMucMessage createOutbound(String body, String fromNick){
		return new AppMucMessage(body, fromNick, false, true);
	}

	public static AppMucMessage createInbound(String body, String fromNick){
		return new AppMucMessage(body, fromNick, true, false);
	}

	public boolean getSentFromSelf(){
		return sentFromSelf;
	}

	public void setSent(boolean val){
		this.sent = val;
	}

	public boolean getSent(){
		return sent;
	}

	public String getBody(){
		return body;
	}

	public String getFromNick(){
		return fromNick;
	}

	public String toString(){
		return "( " + fromNick + " ): " + body;
	}
}
