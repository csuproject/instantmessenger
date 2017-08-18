package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.AppMuc;

public interface SendMucMessageEvent {
	public void send(AppMuc muc, String message);
}
