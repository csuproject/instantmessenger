package TeamOrange.instantmessenger.models;

public class UserStatus {
	
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
