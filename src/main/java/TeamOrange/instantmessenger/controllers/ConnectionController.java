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

/**
 * Handles connection, connection loss, reconnection, and disconnection.
 * Tasks that require a connection are added to a queue here, and are then handled.
 * If there is no connection, then connection is re established before the tasks are handled.
 * These tasks are handled on a separate thread.
 */
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

	/**
	 * Adds a task to Login
	 * @param loginController the controller that knows how to perform this task, and will be used to complete the task.
	 * @param userName the username to login with
	 * @param password the password to login with
	 */
	public void addLoginTask(LoginController loginController, String userName, String password){
		tasks.add(new AppLoginTask(loginController, userName, password));
	}

	/**
	 * Adds a task to create an account
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param userName the username to create the account with
	 * @param password the password to create the account with
	 */
	public void addCreateAccountTask(CreateAccountController controller, String userName, String password){
		tasks.add(new AppCreateAccountTask(controller, userName, password));
	}

	/**
	 * Adds a task to send a contact request
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param userName the username of the user to send the contact request to
	 */
	public void addSendContactRequestTask(AddContactController controller, String userName){
		tasks.add(new AppSendContactRequestTask(controller, userName));
	}

	/**
	 * Adds a task to send a chat session (two person) message
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param message the message to send
	 * @param userName the username of the user to send the message to
	 */
	public void addSendChatSessionMessageTask(ChatController controller, AppChatSessionMessage message, String userName){
		tasks.add(new AppSendChatSessionMessageTask(controller, message, userName));
	}

	/**
	 * Adds a task to create a multi user chat room
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param roomID the id of the room
	 */
	public void addCreateMucWithRoomIDTask(MUCController controller, String roomID){
		tasks.add(new AppCreateMucWithRoomIDTask(controller, roomID));
	}

	/**
	 * Adds a task to send a multi user chat message
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param muc the multi user chat that the message is to be sent in
	 * @param message the message to send
	 */
	public void addSendMucMessageTask(MUCController controller, AppMuc muc, String message){
		tasks.add(new AppSendMucMessageTask(controller, muc, message));
	}

	/**
	 * Adds a task to reply to a contact request
	 * @param controller the controller that knows how to perform this task, and will be used to complete the task.
	 * @param contact the contact to respond to
	 * @param jid the jid of the contact to respond to
	 * @param accepted true if the contact request was accepted, false if declined
	 */
	public void addReplyToContactRequestTask(AcceptOrDeclineContactRequestController controller, AppUser contact, AppJid jid, boolean accepted){
		tasks.add(new AppReplyToContactRequestTask(controller, contact, jid, accepted));
	}

	/**
	 * Adds a task to end the task thread
	 */
	public void addEndTaskThreadTask(){
		tasks.add(new AppEndTaskThreadTask(this.taskThread));
	}

	/**
	 * Adds a task to connect (if not already connected)
	 */
	public void addConnectTask(){
		tasks.add(new AppConnectTask(this.taskThread));
	}

	/**
	 * @return true if the app is connected, false if not connected
	 */
	public boolean isConnected(){
		return connection.getStatus() == AppConnection.CONNECTED;
	}

	public void checkConnection(){
		connect();
	}

	/**
	 * Ends the task thread
	 */
	public void endTaskThread(){
		this.taskThread.end();
	}

	/**
	 * Resets the connection
	 * By disconnecting, setting up a new connection, and then connecting with this new connection
	 */
	public void reset(){
		disconnect();
		setupConnection();
		connect();
	}

	/**
	 * Sets up a connection
	 */
	public void setupConnection(){
		babblerBase.setupConnection();
	}

	/**
	 * Tells the task thread to complete all the tasks in its queue
	 */
	public void completeTasks(){
		this.taskThread.completeTasksNow();
	}

	/**
	 * Connects the app.
	 */
	public void connect(){
		addConnectTask();
		completeTasks();
	}

	/**
	 * Disconnects the app.
	 */
	public void disconnect(){
		try {
			babblerBase.close();
			connection.setStatusToNotConnected();
		} catch (ConfideXmppException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is called when the app is detected to become disconnected.
	 */
	public void onDisconnected(){
		connection.setStatusToNotConnected();
		statusDisplay.setConnectionStatusLater(false);
	}

	/**
	 * This is called when the app is detected to become connected.
	 */
	public void onConnected(){
		connection.setStatusToConnected();
		statusDisplay.setConnectionStatusLater(true);
	}

	/**
	 * This is called when any connection event occurs.
	 * @param type the type of connection event
	 */
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


	/**
	 * Implementation of the task thread, which is used to complete tasks on a separate thread,
	 * as well as to ensure that there is always a connection, reconnect, and disconnect.
	 *
	 */
	public class TaskThread extends Thread {
		private volatile boolean running;
		private volatile boolean shouldCompleteTasks;

		public TaskThread(){
			running = true;
			shouldCompleteTasks = false;
		}

		/**
		 * The run method that is called by Java when this thread is started
		 */
		@Override
	    public void run() {
	    	while(running){
	    		if(shouldCompleteTasks){
	    			connect();
	    			completeTasks();
	    		}
	    	}
	    }

		/**
		 * Tells this thread to complete the tasks in the queue
		 */
	    public void completeTasksNow(){
	    	shouldCompleteTasks = true;
	    }

	    /**
	     * Tells this thread to end.
	     */
	    public void end(){
	    	this.running = false;
	    }

	    /**
	     * Continually tries to connect and checks success, until it succeeds.
	     * Sleeping for 5 seconds in between each attempt.
	     */
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

	    /**
	     * Completes tasks in the queue
	     */
	    private void completeTasks(){
	    	shouldCompleteTasks = false;
	    	while(tasks.peek() != null){
				tasks.poll().complete();
			}
	    }

	}


}
