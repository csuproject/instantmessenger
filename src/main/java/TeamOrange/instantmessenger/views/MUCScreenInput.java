package TeamOrange.instantmessenger.views;

import java.util.LinkedList;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;

public class MUCScreenInput implements ScreenInput {
	private AppMucList mucs;

	public MUCScreenInput(AppMucList muc){
		this.mucs = muc;
	}

	public LinkedList<AppMuc> getMUCList(){
		return mucs.getMUCList();
	}
	
}
