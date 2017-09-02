package TeamOrange.instantmessenger.models;

import java.util.LinkedList;

/**
 * Holds and manages a list of AppMuc
 *
 */
public class AppMucList {
	LinkedList<AppMuc> mucs;
	private AppMuc mucInFocus;

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
	public void add(AppMuc muc){
		String roomID = muc.getRoomID();
		if(getMucIfExists(roomID) == null){
			mucs.add(muc);
		}
	}
	
	public void reset(){
		mucs.clear();
	}

	public void addMUC(AppMuc user){
		mucs.add(user);
	}

	public void addAllMUCS(LinkedList<AppMuc> list){
		if(list != null){
			for(AppMuc muc : list){
				addMUC(muc);
			}
		}
	}

	public LinkedList<AppMuc> getMUCList(){
		return mucs;
	}

	public void setNotification(String mucname, boolean notify) {
		for(AppMuc muc : mucs) {
			if(muc.getRoomID().equals(mucname)) {
				muc.setNotification(notify);				 
			}
		}
	}
	
	public void setNotification(AppMuc notifyMUC, boolean notify) {
		for(AppMuc muc : mucs) {
			if(muc.equals(notifyMUC)) {
				muc.setNotification(notify);				 
			}
		}
	}
    /**
     * Set the MUC in focus
     * @param muc
     */
    public void setMUCInFocus(AppMuc mucInFocus) {
    	this.mucInFocus = mucInFocus;
    }
    
    /**
     * Get the MUC in focus
     * @param muc
     */
    public AppMuc getMUCInFocus() {
    	return mucInFocus;
    }
}
