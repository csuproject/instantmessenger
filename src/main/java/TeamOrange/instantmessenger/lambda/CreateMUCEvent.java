package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.MUCChat;

public interface CreateMUCEvent {
	public void getMUC(MUCChat mucChat);
}
