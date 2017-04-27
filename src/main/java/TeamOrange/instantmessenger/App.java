package TeamOrange.instantmessenger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

	private Client client;

    public void init(){
    	client = new BabblerClient("teamorange.space", () -> messageListener(), () -> presenceListener(), () -> rosterListener());

    	client.setupConnection();
    	client.connect();
    }

    public void messageListener(){

    }

    public void presenceListener(){

    }

    public void rosterListener(){

    }

/////////////////////////////////////////////////////
// These probably shouldnt be here
/////////////////////////////////////////////////////

    public Client getClient(){
    	return client;
    }

    public boolean login(String username, String password){
    	return client.login(username, password);
    }

    public void createAccount(String username, String password){
    	client.createUser(username, password);
    }



}
