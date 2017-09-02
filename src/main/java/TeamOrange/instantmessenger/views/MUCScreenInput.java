package TeamOrange.instantmessenger.views;

import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;

public class MUCScreenInput implements ScreenInput {
	private AppMucList mucs;

	public MUCScreenInput(AppMucList muc){
		this.mucs = muc;
	}

	public List<AppMuc> getMUCList(){
		return mucs.getMucList();
	}

}
