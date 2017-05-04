package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppPresence;

public interface PresenceListener {
	public void presence(AppPresence appPresence);
}
