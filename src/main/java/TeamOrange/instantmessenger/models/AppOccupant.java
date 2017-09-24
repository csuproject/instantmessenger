package TeamOrange.instantmessenger.models;

/**
 * Represents an occupant in a muc
 *
 */
public class AppOccupant {
	private String nickname;

	public AppOccupant(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return nickname;
	}

}
