package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.CreateAccountController;

/**
 * Represents a task to create an account
 *
 */
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
