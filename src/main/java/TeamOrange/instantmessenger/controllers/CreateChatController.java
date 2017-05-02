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
		this.homeScreen.setOnChatWithUserNameEvent( userName->chatWithUserName(userName) );
	}

	public void createChat(AppJid with){
		AppChatSession appChatSession = babblerBase.createChat(with);
		chats.addChat(appChatSession);
	}

	public void chatWithUserName(String userName){
		AppJid to = new AppJid(userName, "teamorange.space");
		AppChatSession appChatSession = babblerBase.createChat(to);
		chats.addChat(appChatSession);
		chats.setActiveChat(appChatSession);
		changeScreen.SetScreen(ScreenEnum.CHAT);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
