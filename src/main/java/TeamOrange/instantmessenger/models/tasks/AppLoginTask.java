package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.LoginController;

/**
 * Represents a task to login
 *
 */
public class AppLoginTask implements AppTask{
	private String userName;
	private String password;
	private LoginController loginController;

	public AppLoginTask(LoginController loginController, String userName, String password){
		this.loginController = loginController;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void complete(){
		loginController.actuallyLogin(userName, password);
	}
}
