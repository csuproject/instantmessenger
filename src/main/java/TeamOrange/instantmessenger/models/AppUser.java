package TeamOrange.instantmessenger.models;

public class AppUser {
	private AppJid jid;
	private AppPresence presence;

	public AppUser(AppJid jid, AppPresence presence){
		this.jid = jid;
		this.presence = presence;
	}

	public AppJid getJid(){
		return jid;
	}
	
	public AppPresence getPresence(){
		return presence;
	}
	
	public void setPresence(AppPresence.Type type){
		presence.set(type);
	}
}
