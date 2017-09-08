package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.AppMuc;

public interface DeleteMucEvent {
	public void delete(AppMuc muc);
}
