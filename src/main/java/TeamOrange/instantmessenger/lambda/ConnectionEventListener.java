package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.controllers.ConnectionEventEnum;

public interface ConnectionEventListener {
	public void connectionEvent(ConnectionEventEnum type);
}
