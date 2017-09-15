package TeamOrange.instantmessenger.lambda;

import java.util.List;

import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;

public interface InviteToMucEvent {
	public void invite(List<AppUser> users);
}
