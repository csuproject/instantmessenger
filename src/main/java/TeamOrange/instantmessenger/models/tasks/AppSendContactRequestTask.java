package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.AddContactController;


/**
 * Represents a task to send a contact request.
 *
 */
public class AppSendContactRequestTask implements AppTask {

	private AddContactController controller;
	private String userName;

	public AppSendContactRequestTask(AddContactController controller, String userName){
		this.controller = controller;
		this.userName = userName;
	}

	@Override
	public void complete() {
		controller.actuallyAddContact(userName);
	}


}
