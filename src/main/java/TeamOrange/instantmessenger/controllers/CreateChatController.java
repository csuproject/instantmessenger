package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class CreateChatController {

	private BabblerBase babblerBase;
	private AppChats chats;
	private HomeScreen homeScreen;
	private ChangeScreen changeScreen;

	public CreateChatController(AppChats chats, AppContacts contacts, BabblerBase babblerBase, HomeScreen homeScreen){
		this.chats = chats;
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		this.homeScreen.setOnChatWithUserNameEvent( userName->createChatWithUserName(userName) );
	}

	public void createChatWithUserName(String userName){
		//create a chat (ChatSession) with this user
		//request for them to create their version of it
		AppJid to = new AppJid(userName, "teamorange.space");
		AppChatSession appChatSession = babblerBase.createChatSession(to);
		chats.addChat(appChatSession);
		babblerBase.requestCreateChatSession(to, appChatSession.getThread());
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
