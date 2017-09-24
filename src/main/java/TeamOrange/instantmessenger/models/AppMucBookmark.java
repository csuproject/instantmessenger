package TeamOrange.instantmessenger.models;

/**
 * Represents the bookmark to a muc.
 * This is how we load mucs upon login.
 *
 */
public class AppMucBookmark {
	private String nick;
	private AppJid room;

	public AppMucBookmark(String nick, AppJid room){
		this.nick = nick;
		this.room = room;
	}

	public String getNick(){
		return nick;
	}

	public AppJid getRoom(){
		return room;
	}
}
