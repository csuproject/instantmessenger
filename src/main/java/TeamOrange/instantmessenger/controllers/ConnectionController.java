package TeamOrange.instantmessenger.controllers;

import java.util.LinkedList;
import java.util.Queue;

import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppConnection;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.models.tasks.AppConnectTask;
import TeamOrange.instantmessenger.models.tasks.AppCreateAccountTask;
import TeamOrange.instantmessenger.models.tasks.AppCreateMucWithRoomIDTask;
import TeamOrange.instantmessenger.models.tasks.AppEndTaskThreadTask;
import TeamOrange.instantmessenger.models.tasks.AppLoginTask;
import TeamOrange.instantmessenger.models.tasks.AppReplyToContactRequestTask;
import TeamOrange.instantmessenger.models.tasks.AppSendChatSessionMessageTask;
import TeamOrange.instantmessenger.models.tasks.AppSendContactRequestTask;
import TeamOrange.instantmessenger.models.tasks.AppSendMucMessageTask;
import TeamOrange.instantmessenger.models.tasks.AppTask;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.StatusDisplay;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideConnectionException;
import exceptions.ConfideNoResponseException;
import exceptions.ConfideXmppException;

public class ConnectionController {
	private StatusDisplay statusDisplay;
	private BabblerBase babblerBase;
	private AppConnection connection;
	private Queue<AppTask> tasks;
	private TaskThread taskThread;

	public ConnectionController(BabblerBase babblerBase, StatusDisplay statusDisplay, AppConnection connection){
		this.babblerBase = babblerBase;
		this.statusDisplay = statusDisplay;
		statusDisplay.setOnCheckConnectionEvent(()->checkConnection());
		this.connection = connection;
		this.tasks = new LinkedList<AppTask>();
		this.taskThread = new TaskThread();
		this.taskThread.start();
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

	public void addCreateMucWithRoomIDTask(MUCController controller, String roomID){
		tasks.add(new AppCreateMucWithRoomIDTask(controller, roomID));
	}

	public void addSendMucMessageTask(MUCController controller, AppMuc muc, String message){
		tasks.add(new AppSendMucMessageTask(controller, muc, message));
	}

	public void addReplyToContactRequestTask(AcceptOrDeclineContactRequestController controller, AppUser contact, AppJid jid, boolean accepted){
		tasks.add(new AppReplyToContactRequestTask(controller, contact, jid, accepted));
	}

	public void addEndTaskThreadTask(){
		tasks.add(new AppEndTaskThreadTask(this.taskThread));
	}

	public void addConnectTask(){
		tasks.add(new AppConnectTask(this.taskThread));
	}

	public boolean isConnected(){
		return connection.getStatus() == AppConnection.CONNECTED;
	}

	public void checkConnection(){
		connect();
	}

	public void endTaskThread(){
		this.taskThread.end();
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
		this.taskThread.completeTasksNow();
	}

	public void connect(){
		addConnectTask();
		completeTasks();
	}

	public void disconnect(){
		try {
			babblerBase.close();
			connection.setStatusToNotConnected();
		} catch (ConfideXmppException e) {
			e.printStackTrace();
		}
	}

	public void onDisconnected(){
		connection.setStatusToNotConnected();
		statusDisplay.setConnectionStatusLater(false);
	}

	public void onConnected(){
		connection.setStatusToConnected();
		statusDisplay.setConnectionStatusLater(true);
	}

	public void onConnectionEvent(ConnectionEventEnum type){
		switch(type){
			case DISCONNECTED:
			{
				onDisconnected();
				connect();
			} break;
			case RECONNECTION_SUCCEEDED:
			{
				onConnected();
			} break;
			case RECONNECTION_FAILED:
			{
			} break;
			case RECONNECTION_PENDING:
			{
			} break;
		}
	}

//	public class ConnectThread implements Runnable {
//
//	    public void run() {
//	    	boolean connected = false;
//			while(!connected){
//				try {
//					connected = babblerBase.isConnected();
//					if(!connected){
//						onDisconnected();
//					}
//					babblerBase.connect();
//				} catch (ConfideXmppException e) {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
//				}
//			}
//			onConnected();
//
//			while(tasks.peek() != null){
//				tasks.poll().complete();
//			}
//
//	    }
//
//	}


	public class TaskThread extends Thread {
		private volatile boolean running;
		private volatile boolean shouldCompleteTasks;

		public TaskThread(){
			running = true;
			shouldCompleteTasks = false;
		}

	    public void run() {
	    	while(running){
	    		if(shouldCompleteTasks){
	    			connect();
	    			completeTasks();
	    		}
	    	}
	    }

	    public void completeTasksNow(){
	    	shouldCompleteTasks = true;
	    }

	    public void end(){
	    	this.running = false;
	    }

	    public void connect(){
	    	if(!babblerBase.isConnected()){
	    		onDisconnected();
		    	boolean connected = false;
				while(!connected && running){
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
				onConnected();
	    	}
	    }

	    private void completeTasks(){
	    	shouldCompleteTasks = false;
	    	while(tasks.peek() != null){
				tasks.poll().complete();
			}
	    }

	}


}
