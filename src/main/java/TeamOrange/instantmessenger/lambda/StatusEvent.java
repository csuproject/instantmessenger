package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.UserStatus;

public interface StatusEvent {
	public void status(UserStatus userStatus);
}
