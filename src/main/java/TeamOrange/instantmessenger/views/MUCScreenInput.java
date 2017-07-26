package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.models.AppMuc;


public class MUCScreenInput {
	private AppMuc muc;

	public MUCScreenInput(AppMuc muc){
		this.muc = muc;
	}
	
	public MUCScreenInput() {
	}
	
	public String getGroupName() {
		
		if (muc == null){
			return "Group";
		} else {
			return muc.getRoomID();
		}
		
	}


}
