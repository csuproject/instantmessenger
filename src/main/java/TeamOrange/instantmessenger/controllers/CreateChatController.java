package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class CreateChatController {

	private BabblerBase babblerBase;
	private AppChats chats;
	private HomeScreen homeScreen;

	public CreateChatController(AppChats chats, BabblerBase babblerBase, HomeScreen homeScreen){
		this.chats = chats;
		this.babblerBase = babblerBase;
	}

	public void createChat(AppJid with){
		AppChatSession appChatSession = babblerBase.createChat(with);
		chats.addChat(appChatSession);
	}
}
