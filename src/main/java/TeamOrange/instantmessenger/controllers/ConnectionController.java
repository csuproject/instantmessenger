package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.models.AppConnection;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;

public class ConnectionController {
	private NavigationScreen navigationScreen;
	private BabblerBase babblerBase;
	private AppConnection connection;

	public ConnectionController(BabblerBase babblerBase, NavigationScreen navigationScreen, AppConnection connection){
		this.babblerBase = babblerBase;
		this.navigationScreen = navigationScreen;
		this.connection = connection;
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
		try {
			babblerBase.connect();
			connection.setStatusToConnected();
		} catch (ConfideXmppException e) {
			connection.setStatusToNotConnected();
			e.printStackTrace();
		}
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
}
