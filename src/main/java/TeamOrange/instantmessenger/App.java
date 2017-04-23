package TeamOrange.instantmessenger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{
	private Client client;
	private Stage stage;

//    public static void main( String[] args )
//    {
//    	App app = new App();
//    	app.run();
//    }

//    public void run(){
//    	init();
//
////    	CLI cli = new CLI(this);
////    	cli.run();
//    	AccountScreen accountScreen = new AccountScreen(this);
//    	accountScreen.launch(null);
//    	//Application.launch(AccountScreen.class);
//    }

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

    public Client getClient(){
    	return client;
    }

    public boolean login(String username, String password){
    	return client.login(username, password);
    }

    public void createAccount(String username, String password){
    	client.createUser(username, password);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		init();

		this.stage = primaryStage;
		stage.setTitle("Confide");

		Scene scene = new Scene(new AccountScreen(this), 640, 480);
		stage.setScene(scene);
		stage.show();
	}

}
