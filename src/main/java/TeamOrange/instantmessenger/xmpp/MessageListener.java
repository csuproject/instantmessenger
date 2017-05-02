package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppMessage;

public interface MessageListener {
	void message(AppMessage message);
}
