package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.LoginController;

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
		loginController.login(userName, password);
	}
}
