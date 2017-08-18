package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;
import java.util.Queue;

import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppConnection;
import TeamOrange.instantmessenger.models.AppCreateAccountTask;
import TeamOrange.instantmessenger.models.AppCreateMucWithMucChatTask;
import TeamOrange.instantmessenger.models.AppCreateMucWithRoomIDTask;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppLoginTask;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppReplyToContactRequestTask;
import TeamOrange.instantmessenger.models.AppSendChatSessionMessageTask;
import TeamOrange.instantmessenger.models.AppSendContactRequestTask;
import TeamOrange.instantmessenger.models.AppSendMucMessageTask;
import TeamOrange.instantmessenger.models.AppTask;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideConnectionException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;

public class ConnectionController {
	private NavigationScreen navigationScreen;
	private BabblerBase babblerBase;
	private AppConnection connection;
	private Queue<AppTask> tasks;
	private TaskThread taskThread;

	public ConnectionController(BabblerBase babblerBase, NavigationScreen navigationScreen, AppConnection connection){
		this.babblerBase = babblerBase;
		this.navigationScreen = navigationScreen;
		this.connection = connection;
		this.tasks = new LinkedList<AppTask>();
		this.taskThread = new TaskThread();
	}

	public void addLoginTask(LoginController loginController, String userName, String password){
		tasks.add(new AppLoginTask(loginController, userName, password));
	}

	public void addCreateAccountTask(CreateAccountController controller, String userName, String password){
		tasks.add(new AppCreateAccountTask(controller, userName, password));
	}

	public void addSendContactRequestTask(AddContactController controller, String userName){
		tasks.add(new AppSendContactRequestTask(controller, userName));
	}

	public void addSendChatSessionMessageTask(ChatController controller, AppChatSessionMessage message, String userName){
		tasks.add(new AppSendChatSessionMessageTask(controller, message, userName));
	}

	public void addCreateMucWithMucChatTask(MUCController controller, MUCChat mucChat){
		tasks.add(new AppCreateMucWithMucChatTask(controller, mucChat));
	}

	public void addCreateMucWithRoomIDTask(MUCController controller, String roomID){
		tasks.add(new AppCreateMucWithRoomIDTask(controller, roomID));
	}

	public void addSendMucMessageTask(MUCController controller, AppMuc muc, String message){
		tasks.add(new AppSendMucMessageTask(controller, muc, message));
	}

	public void addReplyToContactRequestTask(AcceptOrDeclineContactRequestController controller, AppUser contact, AppJid jid, boolean accepted){
		tasks.add(new AppReplyToContactRequestTask(controller, contact, jid, accepted));
	}

	public boolean isConnected(){
		return connection.getStatus() == AppConnection.CONNECTED;
	}

	public void reset(){
		disconnect();
		setupConnection();
		connect();
	}

	public void setupConnection(){
		babblerBase.setupConnection();
	}

	public void completeTasks(){
		(new Thread(this.taskThread)).start();
	}

	public void connect(){
		(new Thread(new ConnectThread())).start();
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
				connect();
			} break;
			case RECONNECTION_SUCCEEDED:
			{
				connection.setStatusToConnected();
			} break;
			case RECONNECTION_FAILED:
			{
			} break;
			case RECONNECTION_PENDING:
			{
			} break;
		}
	}

	public class ConnectThread implements Runnable {

	    public void run() {
	    	connection.setStatusToNotConnected();
	    	boolean connected = false;
			while(!connected){
				try {
					babblerBase.connect();
					connected = babblerBase.isConnected();
				} catch (ConfideXmppException e) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			connection.setStatusToConnected();
//			if(!babblerBase.isLoggedIn()){
//				babblerBase.loginAnonymously();
//			}

			while(tasks.peek() != null){
				tasks.poll().complete();
			}

	    }

	}


	public class TaskThread implements Runnable {

	    public void run() {
	    	if(!babblerBase.isConnected()){
	    		connection.setStatusToNotConnected();
		    	boolean connected = false;
				while(!connected){
					try {
						babblerBase.connect();
						connected = babblerBase.isConnected();
					} catch (ConfideXmppException e) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
				connection.setStatusToConnected();
	    	}

			while(tasks.peek() != null){
				tasks.poll().complete();
			}
	    }

	}


}
