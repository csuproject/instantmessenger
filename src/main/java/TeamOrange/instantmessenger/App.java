package TeamOrange.instantmessenger;

import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

	private BabblerBase babblerBase;

    public void init(){
    	babblerBase = new BabblerBase("teamorange.space", () -> messageListener(), () -> presenceListener(), () -> rosterListener());

    	babblerBase.setupConnection();
    	babblerBase.connect();
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

    public BabblerBase getClient(){
    	return babblerBase;
    }

    public void login(String username, String password){
    	babblerBase.login(username, password);
    }

    public void createAccount(String username, String password){
    	babblerBase.createUser(username, password);
    }



}
