package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class AppMuc {
	private String roomID;
	private String nickname;
	private BabblerBase babblerBase;
	private LinkedList<AppMucMessage> messages;
	private LinkedList<AppOccupant> occupants;

	public AppMuc(String roomID, String nickname, BabblerBase babblerBase){
		this.roomID = roomID;
		this.nickname = nickname;
		this.babblerBase = babblerBase;
		messages = new LinkedList<AppMucMessage>();
		occupants = new LinkedList<AppOccupant>();
	}

	public void inboundMessage(AppMucMessage message){
		messages.add(message);

		System.out.println(message);
	}

	public void sendMessage(String message){
		babblerBase.sendGroupMessage(roomID, message);
	}

	public void leave(){
		babblerBase.leaveRoom(roomID);
	}

	public void enter(){
		babblerBase.createAndOrEnterRoom(roomID, nickname);
	}

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

	public void occupantKicked(String nick){
		AppOccupant occupant = new AppOccupant(nick);
		// TODO: consider multiple versions of the same nickname, or the same person leaving twice
		occupants.remove(occupant);
	}

	public String getRoomID(){
		return roomID;
	}

	public String toString(){
		return "MUC( addr: " + roomID + "  nickname: " + nickname + " )\n";
	}
}
