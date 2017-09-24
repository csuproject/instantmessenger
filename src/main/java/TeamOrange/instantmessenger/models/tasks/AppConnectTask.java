package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.ConnectionController.TaskThread;

/**
 * Represents a task to connect.
 *
 */
public class AppConnectTask implements AppTask {
	private TaskThread thread;

	public AppConnectTask(TaskThread thread){
		this.thread = thread;
	}

	@Override
	public void complete() {
		this.thread.connect();
	}


}
