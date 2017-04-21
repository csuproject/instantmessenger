package TeamOrange.instantmessenger;


public class App
{
	Client client;

    public static void main( String[] args )
    {
    	App app = new App();
    	app.run();
    }

    public void run(){
    	init();

    	CLI cli = new CLI(this);
    	cli.run();
    }

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


}
