package TeamOrange.instantmessenger.models;

public class AppMucRequest {
	private String roomID;
	private AppJid from;

	public AppMucRequest(String roomID, AppJid from){
		this.roomID = roomID;
		this.from = from;
	}

	public String getRoomID(){
		return roomID;
	}

	public AppJid getFrom(){
		return from;
	}
}
