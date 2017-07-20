package TeamOrange.instantmessenger.models;

/**
 * Represents a muc message
 *
 */
public class AppMucMessage {
	private String body;
	private String fromNick;

	public AppMucMessage(String body, String fromNick){
		this.body = body;
		this.fromNick = fromNick;
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
