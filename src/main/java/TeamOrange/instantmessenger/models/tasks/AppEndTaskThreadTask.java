package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.ConnectionController.TaskThread;

public class AppEndTaskThreadTask implements AppTask {
	private TaskThread thread;

	public AppEndTaskThreadTask(TaskThread thread){
		this.thread = thread;
	}

	@Override
	public void complete() {
		this.thread.end();
	}


}
