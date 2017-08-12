package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;
import java.util.Queue;

import TeamOrange.instantmessenger.models.AppConnection;
import TeamOrange.instantmessenger.models.AppCreateAccountTask;
import TeamOrange.instantmessenger.models.AppLoginTask;
import TeamOrange.instantmessenger.models.AppTask;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;

public class ConnectionController {
	private NavigationScreen navigationScreen;
	private BabblerBase babblerBase;
	private AppConnection connection;
	private Queue<AppTask> tasks;

	public ConnectionController(BabblerBase babblerBase, NavigationScreen navigationScreen, AppConnection connection){
		this.babblerBase = babblerBase;
		this.navigationScreen = navigationScreen;
		this.connection = connection;
		this.tasks = new LinkedList<AppTask>();
	}

	public void addLoginTask(LoginController loginController, String userName, String password){
		tasks.add(new AppLoginTask(loginController, userName, password));
	}

	public void addCreateAccountTask(CreateAccountController controller, String userName, String password){
		tasks.add(new AppCreateAccountTask(controller, userName, password));
	}

	public void reset(){
		disconnect();
		setupConnection();
		connect();
	}

	public void setupConnection(){
		babblerBase.setupConnection();
	}

	public void connect(){
		(new Thread(new ConnectThread())).start();

//		boolean connected = false;
//		while(!connected){
//			try {
//				babblerBase.connect();
//				connection.setStatusToConnected();
//				connected = true;
//			} catch (ConfideXmppException e) {
//				connection.setStatusToNotConnected();
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}
	}

	public void disconnect(){
		try {
			babblerBase.close();
			connection.setStatusToNotConnected();
		} catch (ConfideXmppException e) {
			e.printStackTrace();
		}
	}

	public void onConnectionEvent(ConnectionEventEnum type){
		switch(type){
			case DISCONNECTED:
			{
				connection.setStatusToNotConnected();
				System.out.println("Conneciton Disconnected");
			} break;
			case RECONNECTION_SUCCEEDED:
			{
				connection.setStatusToConnected();
				System.out.println("Reconneciton Succeeded");
			} break;
			case RECONNECTION_FAILED:
			{
				System.out.println("Reconneciton Failed");
			} break;
			case RECONNECTION_PENDING:
			{
//				System.out.println("Reconneciton Pending");
			} break;
		}
	}

	public class ConnectThread implements Runnable {

	    public void run() {
	    	boolean connected = false;
			while(!connected){
				try {
					babblerBase.connect();
					connection.setStatusToConnected();
					connected = true;
				} catch (ConfideXmppException e) {
					connection.setStatusToNotConnected();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			while(tasks.peek() != null){
				tasks.poll().complete();
			}

	    }

	}
}
