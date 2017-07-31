package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;

public interface StatusEvent {
	public void status(AppJid from, AppPresence.Type presenceType);
}
