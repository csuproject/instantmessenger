package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.AddContactController;

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
