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

	public AppMucList(){
		this.mucs = new LinkedList<AppMuc>();
		this.mucRequests = new LinkedList();
		this.mucInFocus = null;
	}

	public void reset(){
		this.mucs.clear();
		this.mucRequests.clear();
		this.mucInFocus = null;
	}

	public void removeMucRequest(String roomID, String from){
		for(int i = 0; i < mucRequests.size(); ++i){
			AppMucRequest request = mucRequests.get(i);
			if( request.getFrom().equals(from) && request.getRoomID().equals(roomID) ){
				mucRequests.remove(i);
				return;
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

	public void setMucs(List<AppMuc> newMucs){
		mucs.clear();
		for(AppMuc muc : newMucs){
			this.mucs.add(muc);
		}
	}

	public void setMucInFocus(AppMuc muc){
		this.mucInFocus = muc;
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

	public void removeMuc(AppMuc muc){
		mucs.remove(muc);
	}

	public void addMucRequest(AppMucRequest mucRequest){
		for(AppMucRequest request : mucRequests){
			if( (request.getFrom() == mucRequest.getFrom()) &&
				(request.getRoomID() == mucRequest.getRoomID()) ){
				return;
			}
		}
		mucRequests.add(mucRequest);
	}
}
