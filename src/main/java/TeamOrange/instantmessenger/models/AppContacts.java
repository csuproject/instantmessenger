package TeamOrange.instantmessenger.models;

public class AppContacts {
	private AppUser self;

	public AppContacts(){

	}

	public void setSelf(AppUser self){
		this.self = self;
	}

	public AppUser getSelf(){
		return self;
	}
}
