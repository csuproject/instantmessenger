package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;

public class NavigationController {

	private NavigationScreen navigationScreen;
	private ChangeScreen changeScreen;
	
	public NavigationController(NavigationScreen navigationScreen) {
		this.navigationScreen = navigationScreen;
		this.navigationScreen.setOnChangeScreen(
				e->changeScreen.SetScreen(e));
	}
	
	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
