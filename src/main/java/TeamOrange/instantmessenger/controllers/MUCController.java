package TeamOrange.instantmessenger.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.MUCListEvent;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import TeamOrange.instantmessenger.views.ChatScreen;
import TeamOrange.instantmessenger.views.CreateMUCScreen;
import TeamOrange.instantmessenger.views.MUCContactDisplay;
import TeamOrange.instantmessenger.views.MUCScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.application.Platform;

public class MUCController {
	
	private ChangeScreen changeScreen;
	private List<AppMuc> mucList;
	private BabblerBase babblerBase;
	private MUCScreen mucScreen;
	private AppContacts contacts;
	private MUCListEvent mucListEvent;
	private ChatScreen mucChatScreen;
	GetMUCEvent getMUCEvent;

	
	public MUCController(BabblerBase babblerBase, AppContacts contacts, MUCScreen mucScreen, 
			CreateMUCScreen createMUCScreen) {
		
		this.babblerBase = babblerBase;
		this.mucScreen = mucScreen;
		this.contacts = contacts;
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(getMUCEvent->enterMUC(getMUCEvent));
		createMUCScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		createMUCScreen.setOnCreateMUCEvent(createMUCEvent->createMUC(createMUCEvent));
		mucList = new ArrayList<AppMuc>();
	}


	
	/**
	 * Create MUC
	 * @param muc
	 */
	public void createMUC(MUCChat mucChat) {

		// Create MUC
		AppMuc appMUC = muc(mucChat.getName());
		mucList.add(appMUC);
		mucListEvent.getMUCList(mucList);
		requestMUC(mucChat);
	}
	
	/**
	 * Send message to users for group chat invite
	 * @param createMUC
	 */
	public void requestMUC(MUCChat mucChat) {
		
		List<AppUser> list = mucChat.getUsers();
		System.out.println("Group Name " + mucChat.getName());
		
		for(AppUser appUser : list){
			System.out.println(appUser.getJid());
		}
	}
	
	/**
	 * Enter MUC
	 * @param mucName
	 */
	public void enterMUC(String mucName) {
		// Enter MUC 
		muc(mucName);
	}
	
	/**
	 * Enter MUC
	 * @param mucChat
	 */
	public void enterMUC(AppMuc appMUC) {
		// Enter MUC 
		getMUCEvent.getMUC(muc(appMUC.getRoomID()));
	}
	
	public AppMuc muc(String mucName) {
		
		return babblerBase.createAndOrEnterRoom(
				mucName, contacts.getSelfName());
	}
	
	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
	
	public void setOnMUCListEvent(MUCListEvent mucListEvent) {
		this.mucListEvent = mucListEvent;
	}
	
	public void setOnOpenMUC(GetMUCEvent getMUCEvent) {
		this.getMUCEvent = getMUCEvent;
	}
}
