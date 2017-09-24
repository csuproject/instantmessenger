package TeamOrange.instantmessenger.models;


/**
 * Represents a user.
 *
 */
public class AppUser {
	private AppJid jid;
	private AppPresence presence;
	private boolean notify;

	public AppUser(AppJid jid, AppPresence presence){
		this.jid = jid;
		this.presence = presence;
	}

	public AppJid getJid(){
		return jid;
	}

	public String getName() {
		return jid.getLocal();
	}

	public AppPresence getPresence(){
		return presence;
	}

	public void setPresence(AppPresence.Type type){
		presence.set(type);
	}

	public void setNotification(boolean notify) {
		this.notify = notify;
	}

	public boolean getNotification() {
		return notify;
	}
}
