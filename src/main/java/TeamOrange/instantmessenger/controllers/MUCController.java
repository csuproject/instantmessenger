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
	private ChatScreen chatScreen;
	private GetMUCEvent getMUCEvent;
	private GetMUCEvent newMessageMUC;

	
	public MUCController(BabblerBase babblerBase, ChatScreen chatScreen, 
			AppContacts contacts, MUCScreen mucScreen, CreateMUCScreen createMUCScreen) {
		
		this.babblerBase = babblerBase;
		this.mucScreen = mucScreen;
		this.contacts = contacts;
		this.chatScreen = chatScreen;
		mucScreen.setOnChangeScreen(screen->changeScreen.SetScreen(screen));
		mucScreen.setOnOpenMUC(getMUCEvent->enterMUC(getMUCEvent));
		mucScreen.setOnAddGroupGetMUCEvent(addMUCEvent->enterMUC(addMUCEvent));
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
		AppMuc muc = babblerBase.createAndOrEnterRoom(mucChat.getName(), "nickname");
		muc.setReference(muc);
		muc.setOnNewMessage(getMUCEvent-> // Set new message notifier
			newMessageMUC.getMUC(getMUCEvent));
		mucList.add(muc);
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
		mucList.add(muc(mucName));
		mucListEvent.getMUCList(mucList);
	}
	
	/**
	 * Enter MUC
	 * @param mucChat
	 */
	public void enterMUC(AppMuc appMUC) {
		// Enter MUC 
		getMUCEvent.getMUC(appMUC);
	}
	
	/**
	 * Send message to focused MUC
	 * @param message
	 */
	public void sendMUCMessage(AppMuc muc, String message) {
		muc.sendMessage(message);
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
	
	public void setOnNewMessage(GetMUCEvent getMUCEvent) {
		this.newMessageMUC = getMUCEvent;
	}
}
