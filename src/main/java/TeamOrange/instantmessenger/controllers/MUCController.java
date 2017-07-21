package TeamOrange.instantmessenger.controllers;

import java.util.List;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.MUCContactDisplay;
import TeamOrange.instantmessenger.views.MUCScreen;

public class MUCController {
	
	private ChangeScreen changeScreen;
	
	public MUCController(MUCScreen mucScreen, CreateMUCScreen createMUCScreen) {
		
		
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnCreateMUCEvent(createMUCEvent->print(createMUCEvent));
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
	
	/**
	 * To be removed
	 * @param muc
	 */
	public void print(MUCChat muc) {
		
		List<AppUser> list = muc.getUsers();
		System.out.println("Group Name " + muc.getName());
		
		for(AppUser appUser : list){
			System.out.println(appUser.getJid());
		}
	}
}
