package TeamOrange.instantmessenger.models;

import java.util.List;

public class MUCChat {

	private String mucName;
	private List<AppUser> mucList;
	
	public MUCChat(String mucName, List<AppUser> mucList) {
		this.mucName = mucName;
		this.mucList = mucList;
	};
	
	public void setMUC(String mucName, List<AppUser> mucList) {
		this.mucName = mucName;
		this.mucList = mucList;
	}
	
	public List<AppUser> getUsers() {
		return this.mucList;
	}
	
	public String getName() {
		return this.mucName;
	}

}
