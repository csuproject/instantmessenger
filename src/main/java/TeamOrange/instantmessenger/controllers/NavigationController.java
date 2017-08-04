package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.App;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;

public class NavigationController {

	private NavigationScreen navigationScreen;
	private ChangeScreen changeScreen;
	private App app;
	
	public NavigationController(NavigationScreen navigationScreen, App app) {
		this.navigationScreen = navigationScreen;
		this.navigationScreen.setOnChangeScreen(
				e->changeScreen.SetScreen(e));
		this.navigationScreen.setOnLogoutEvent(()->logout());
		this.app = app;
	}
	
	public void logout(){
		app.reset();
		changeScreen.SetScreen(ScreenEnum.ACCOUNT);
	}
	
	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
