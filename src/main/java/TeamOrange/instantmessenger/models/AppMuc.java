package TeamOrange.instantmessenger.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Represents a multi user chat (chat room)
 *
 */
public class AppMuc {
	private String roomID;
	private String nickname;
	private BabblerBase babblerBase;
	private LinkedList<AppMucMessage> messages;
	private LinkedList<AppOccupant> occupants;
	GetMUCEvent messageEvent;
	AppMuc muc;
	boolean messageInit;
	private boolean notify;
	private int colorsIndex;
	private ArrayList<Color> colors = new ArrayList();

	public AppMuc(String roomID, String nickname, BabblerBase babblerBase, GetMUCEvent messageEvent){
		this.roomID = roomID;
		this.nickname = nickname;
		this.babblerBase = babblerBase;
		messages = new LinkedList<AppMucMessage>();
		occupants = new LinkedList<AppOccupant>();
		this.messageEvent = messageEvent;

		colors.add( Color.rgb(244, 92, 66) );
		colors.add( Color.rgb(65, 244, 241) );
		colors.add( Color.rgb(244, 160, 65) );
		colors.add( Color.rgb(244, 92, 66) );
		colors.add( Color.rgb(208, 65, 244) );
		colors.add( Color.rgb(65, 244, 178) );
		colors.add( Color.rgb(65, 74, 244) );
		colors.add( Color.rgb(65, 166, 244) );
		colors.add( Color.rgb(244, 65, 131) );
		colors.add( Color.rgb(244, 241, 65) );
		colorsIndex = 0;
	}

	/**
	 * Sends the given message to the group chat
	 * @param message the message to send
	 */
	public void sendMessage(String message){
		babblerBase.sendGroupMessage(roomID, message);
	}

	/**
	 * leaves this group chat
	 */
	public void leave(){
		babblerBase.leaveRoom(roomID);
	}

	/**
	 * Adds an unsent message to this group chat
	 * @param message the messsage to add
	 * @param from the user that the message is from
	 */
	public void addUnsentMessage(String message, String from){
		AppMucMessage appMessage = AppMucMessage.createOutbound(message, from);
		messages.add(appMessage);
	}

	/**
	 * enters this group chat
	 */
	public void enter(){
		try {
			babblerBase.createAndOrEnterRoom(roomID, nickname, messageEvent);
		} catch (ConfideFailedToEnterChatRoomException e1) {
			e1.printStackTrace();
		} catch (ConfideFailedToConfigureChatRoomException e2){
			e2.printStackTrace();
		}
	}

	public String getRoomID(){
		return roomID;
	}

	public String getNick(){
		return nickname;
	}

	public String toString(){
		return "MUC( addr: " + roomID + "  nickname: " + nickname + " )\n";
	}

	//----------------- Listeners ------------------//

	/**
	 * This function is called whenever a new message is received for this muc
	 * If the message is sent from self, then the message is located and sent is set to true.
	 * Otherwise the message is just added.
	 * @param message the received message
	 */
	public void inboundMessage(AppMucMessage message){
		if(message.getSentFromSelf()){
			AppMucMessage outboundMessage = getOutboundMessage(message);
			if(outboundMessage != null){
				outboundMessage.setSent(true);
			} else{
				messages.add(message);
			}
		} else{
			int index = addOccupantOrGetIndexIfExists(message.getFromNick());
			if(index < 0){
				System.out.println("occupant not found");
				index = 0;
			}
			Color color = colors.get(index % colors.size());
			message.setColor(color);
			messages.add(message);
		}

		messageEvent.getMUC(this);
	}

	/**
	 * Returns the index of the occupant with the given nick, or adds an occupant if it doesnt already exist
	 * @param nick the nickname to look for
	 * @return the index of that occupant.
	 */
	public int addOccupantOrGetIndexIfExists(String nick){
		for(int i = 0; i < occupants.size(); ++i){
			if(occupants.get(i).getNickname().equals(nick)){
				return i;
			}
		}
		int index = occupants.size();
		occupants.addLast(new AppOccupant(nick));
		return index;
	}

	/**
	 * Retrieves an outbound message
	 * @param message the message to retrieve
	 * @return the retrieved message, or null if none exists
	 */
	public AppMucMessage getOutboundMessage(AppMucMessage message){
		String body = message.getBody();
		AppMucMessage answer = null;
		for(AppMucMessage m : messages){
			if(m.getSentFromSelf() && m.getSent() == false){
				if(m.getBody().equals(body)){
					answer = m;
				}
			}
		}
		return answer;
	}

	/**
	 * Get List of Messages
	 * @return
	 */
	public LinkedList<AppMucMessage> getMessages() {
		return messages;

	}

	/**
	 * This function is called whenever a new user enters the muc
	 * @param nick the nickname of the user who entered
	 */
	public void occupantEntered(String nick){
		AppOccupant occupant = new AppOccupant(nick);
		// TODO: consider multiple versions of the same nickname, or the same person entering twice
		occupants.add(occupant);
	}

	/**
	 * This function is called whenever a user exits the muc
	 * @param nick the nickname of the user who exited
	 */
	public void occupantExited(String nick){
		AppOccupant occupant = new AppOccupant(nick);
		// TODO: consider multiple versions of the same nickname, or the same person leaving twice
		occupants.remove(occupant);
	}

	/**
	 * This function is called whenever a user is kicked from the muc
	 * @param nick the nickname of the user who was kicked
	 */
	public void occupantKicked(String nick){
		AppOccupant occupant = new AppOccupant(nick);
		// TODO: consider multiple versions of the same nickname, or the same person leaving twice
		occupants.remove(occupant);
	}

//	public void setOnNewMessage(GetMUCEvent getMUCEvent) {
//		this.messageEvent = getMUCEvent;
//	}


	public void setReference(AppMuc muc) {
		this.muc = muc;
	}

	  @Override
	  public boolean equals(Object otherObject) {
	    // check for reference equality.
	    if (this == otherObject) {
	      return true;
	    } else {
	    	  return false;
	    }
	  }

	public void setNotification(boolean notify) {
		this.notify = notify;
	}

	public boolean getNotification() {
		return notify;
	}

}
