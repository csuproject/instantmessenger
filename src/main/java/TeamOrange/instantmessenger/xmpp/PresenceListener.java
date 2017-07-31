package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;

public interface PresenceListener {
	public void presence(AppJid fromJid, AppPresence.Type appPresenceType);
}
