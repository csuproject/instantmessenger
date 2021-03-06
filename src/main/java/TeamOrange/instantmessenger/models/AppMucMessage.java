package TeamOrange.instantmessenger.models;

import javafx.scene.paint.Color;

/**
 * Represents a muc message
 *
 */
public class AppMucMessage {
	private String body;
	private String fromNick;
	private boolean sent;
	private boolean sentFromSelf;
	private Color color;

	private AppMucMessage(String body, String fromNick, boolean sent, boolean sentFromSelf, Color color){
		this.body = body;
		this.fromNick = fromNick;
		this.sent = sent;
		this.sentFromSelf = sentFromSelf;
		this.color = color;
	}

	public Color getColor(){
		return color;
	}

	/**
	 * Creates an outbound message
	 * @param body the body of the message
	 * @param fromNick the nickname of the user that the message is from
	 * @return the created outbound muc message
	 */
	public static AppMucMessage createOutbound(String body, String fromNick){
		return new AppMucMessage(body, fromNick, false, true, Color.GREEN);
	}

	/**
	 * Creates an inbound message
	 * @param body the body of the message
	 * @param fromNick the nickname of the user that the message is from
	 * @return the created inbound muc message
	 */
	public static AppMucMessage createInbound(String body, String fromNick){
		return new AppMucMessage(body, fromNick, true, false, Color.BLUE);
	}

	public void setColor(Color color){
		this.color = color;
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
