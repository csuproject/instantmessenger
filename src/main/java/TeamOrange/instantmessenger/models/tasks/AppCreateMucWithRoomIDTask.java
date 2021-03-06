package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.MUCController;

/**
 * Represents a task to create a muc
 *
 */
public class AppCreateMucWithRoomIDTask implements AppTask {

	private MUCController controller;
	private String roomID;

	public AppCreateMucWithRoomIDTask(MUCController controller, String roomID){
		this.controller = controller;
		this.roomID = roomID;
	}

	@Override
	public void complete() {
		controller.actuallyCreateMUC(roomID);
	}

}
