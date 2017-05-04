package TeamOrange.instantmessenger.models;

public class AppUser {
	private AppJid jid;

	public AppUser(AppJid jid){
		this.jid = jid;
	}

	public AppJid getJid(){
		return jid;
	}
}
