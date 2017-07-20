package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

public class AppMucList {
	LinkedList<AppMuc> mucs;

	public AppMucList(){
		this.mucs = new LinkedList<AppMuc>();
	}

	public AppMuc getMucIfExists(String roomID){
		for(AppMuc m : mucs){
			if(m.getRoomID().equals(roomID)){
				return m;
			}
		}
		return null;
	}

	public void addMuc(AppMuc muc){
		String roomID = muc.getRoomID();
		if(getMucIfExists(roomID) == null){
			mucs.add(muc);
		}
	}
}
