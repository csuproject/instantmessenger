package TeamOrange.instantmessenger.models;

public class UserStatus {

	// TODO: - is this presence info?
	//		 - use an enum
	//		 - change to be named AppUserStatus or AppPresence

	private String user;
	private String status;

	public UserStatus(String user, String status) {
		this.user = user;
		this.status = status;
	};

	public void setStatus(String user, String status) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	public String getStatus() {
		return this.status;
	}

}
