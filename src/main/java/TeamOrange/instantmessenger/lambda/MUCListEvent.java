package TeamOrange.instantmessenger.lambda;

import java.util.List;

import TeamOrange.instantmessenger.models.AppMuc;

public interface MUCListEvent {
	public void getMUCList(List<AppMuc> mucList);
}
