package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppPresence;

public interface StatusListener {
	public void presence(AppPresence appPresence);
}
