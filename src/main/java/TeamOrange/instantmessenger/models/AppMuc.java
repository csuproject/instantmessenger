package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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

	public AppMuc(String roomID, String nickname, BabblerBase babblerBase){
		this.roomID = roomID;
		this.nickname = nickname;
		this.babblerBase = babblerBase;
		messages = new LinkedList<AppMucMessage>();
		occupants = new LinkedList<AppOccupant>();
		//this.muc = this;
	}

	/**
	 * Sends the given message to the group chat
	 * @param message
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
	 * enters this group chat
	 */
	public void enter(){
		babblerBase.createAndOrEnterRoom(roomID, nickname);
	}

	public String getRoomID(){
		return roomID;
	}

	public String toString(){
		return "MUC( addr: " + roomID + "  nickname: " + nickname + " )\n";
	}

	//----------------- Listeners ------------------//

	/**
	 * This function is called whenever a new message is received for this muc
	 * @param message
	 */
	public void inboundMessage(AppMucMessage message){
		messages.add(message);
		messageEvent.getMUC(this);
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
		System.out.println("--------------------");
		for(AppOccupant o : occupants){
			System.out.println("occupant: " + o.getNickname());
		}
		System.out.println("--------------------");
	}

	/**
	 * This function is called whenever a user exits the muc
	 * @param nick the nickname of the user who exited
	 */
	public void occupantExited(String nick){
		AppOccupant occupant = new AppOccupant(nick);
		// TODO: consider multiple versions of the same nickname, or the same person leaving twice
		occupants.remove(occupant);
		System.out.println("--------------------");
		for(AppOccupant o : occupants){
			System.out.println("occupant: " + o.getNickname());
		}
		System.out.println("--------------------");
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
	
	public void setOnNewMessage(GetMUCEvent getMUCEvent) {
		this.messageEvent = getMUCEvent;
	}
	
	
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
	
}