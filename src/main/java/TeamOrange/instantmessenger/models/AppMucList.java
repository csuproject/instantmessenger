package TeamOrange.instantmessenger.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds and manages a list of AppMuc
 *
 */
public class AppMucList {
	private LinkedList<AppMuc> mucs;
	private LinkedList<AppMucRequest> mucRequests;
	private AppMuc mucInFocus;
	private AppMuc mucInvitingTo;

	public AppMucList(){
		this.mucs = new LinkedList<AppMuc>();
		this.mucRequests = new LinkedList();
		this.mucInFocus = null;
		this.mucInvitingTo = null;
	}

	public void reset(){
		this.mucs.clear();
		this.mucRequests.clear();
		this.mucInFocus = null;
	}

	public void removeMucRequestsWithRoomID(String roomID){
		for(int i = mucRequests.size()-1; i >= 0 ; --i){
			AppMucRequest request = mucRequests.get(i);
			if( request.getRoomID().equals(roomID) ){
				mucRequests.remove(i);
			}
		}
	}

	public AppMuc getMucInFocus(){
		return mucInFocus;
	}

	public List<AppMuc> getMucList(){
		return mucs;
	}

	public List<AppMucRequest> getMucRequests(){
		return mucRequests;
	}

	public boolean mucInFocusIs(AppMuc muc){
		return this.mucInFocus.equals(muc);
	}

	public void setMucInFocus(AppMuc muc){
		this.mucInFocus = muc;
	}

	public void setMucInvitingTo(AppMuc muc){
		this.mucInvitingTo = muc;
	}

	public AppMuc getMucInvitingTo(){
		return mucInvitingTo;
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

	public void addAllMUCS(LinkedList<AppMuc> list){
		if(list != null){
			for(AppMuc muc : list){
				add(muc);
			}
		}
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

	public void removeMuc(AppMuc muc){
		mucs.remove(muc);
	}

	public void addMucRequest(AppMucRequest mucRequest){
		// check an equal muc request doesnt already exist
		for(AppMucRequest request : mucRequests){
			if( (request.getFrom().getBareJid().equals(mucRequest.getFrom().getBareJid())) &&
				(request.getRoomID().equals(mucRequest.getRoomID())) ){
				return;
			}
		}
		//check an equal muc doesnt already exist
		for(AppMuc muc : mucs){
			if(muc.getRoomID().equals(mucRequest.getRoomID())){
				return;
			}
		}

		mucRequests.add(mucRequest);
	}

}
