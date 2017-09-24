package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.App;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.NavigationScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;

/**
 * Controls the flow for the logout event
 *
 */
public class NavigationController {

	private NavigationScreen navigationScreen;
	private HomeScreen homeScreen;
	private ChangeScreen changeScreen;
	private App app;


	public NavigationController(HomeScreen homeScreen,
			NavigationScreen navigationScreen, App app) {
		this.homeScreen = homeScreen;
		this.navigationScreen = navigationScreen;
		this.navigationScreen.setOnChangeScreen(
				e->changeScreen.SetScreen(e));
		this.navigationScreen.setOnLogoutEvent(()->logout());
		this.app = app;
	}

	/**
	 * Logs out of the application.
	 */
	public void logout(){
		navigationScreen.setSelectedToLogout();
		app.reset();
		changeScreen.SetScreen(ScreenEnum.ACCOUNT);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
