package TeamOrange.instantmessenger.views;

import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppMucRequest;

public class MUCScreenInput implements ScreenInput {
	private AppMucList mucs;

	public MUCScreenInput(AppMucList muc){
		this.mucs = muc;
	}

	public List<AppMuc> getMUCList(){
		return mucs.getMucList();
	}

	public List<AppMucRequest> getMucRequests(){
		return mucs.getMucRequests();
	}

}
