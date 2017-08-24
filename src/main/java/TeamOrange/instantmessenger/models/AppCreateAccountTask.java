package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.CreateAccountController;

public class AppCreateAccountTask implements AppTask {
	private CreateAccountController createAccountController;
	private String userName;
	private String password;

	public AppCreateAccountTask(CreateAccountController createAccountController, String userName, String password){
		this.createAccountController = createAccountController;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void complete() {
		createAccountController.actuallyCreateAccount(userName, password);
	}

}
