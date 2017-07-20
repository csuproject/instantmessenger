package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

/**
 * Holds and manages a list of AppMuc
 *
 */
public class AppMucList {
	LinkedList<AppMuc> mucs;

	public AppMucList(){
		this.mucs = new LinkedList<AppMuc>();
	}

	/**
	 * Gets the muc with the given id, if it exists, else null
	 * @param roomID the id of the room to look for
	 * @return the room with the given id, or null if it doesnt exist
	 */
	public AppMuc getMucIfExists(String roomID){
		for(AppMuc m : mucs){
			if(m.getRoomID().equals(roomID)){
				return m;
			}
		}
		return null;
	}

	/**
	 * Adds the given muc, if it isnt already in the list
	 * @param muc the muc to add
	 */
	public void addMuc(AppMuc muc){
		String roomID = muc.getRoomID();
		if(getMucIfExists(roomID) == null){
			mucs.add(muc);
		}
	}
}
