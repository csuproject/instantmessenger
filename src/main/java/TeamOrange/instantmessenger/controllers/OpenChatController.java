package TeamOrange.instantmessenger.controllers;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppChats;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.views.HomeScreen;
import TeamOrange.instantmessenger.views.ScreenEnum;
import TeamOrange.instantmessenger.xmpp.BabblerBase;

/**
 * This controls the flow when the user opens a chat session
 *
 */
public class OpenChatController {

	private BabblerBase babblerBase;
	private AppChats chats;
	private AppContacts contacts;
	private HomeScreen homeScreen;
	private ChangeScreen changeScreen;

	public OpenChatController(AppChats chats, AppContacts contacts, BabblerBase babblerBase, HomeScreen homeScreen){
		this.chats = chats;
		this.contacts = contacts;
		this.babblerBase = babblerBase;
		this.homeScreen = homeScreen;
		this.homeScreen.setOnChatWithContactEvent(username->chatWithContact(username));
		//this.homeScreen.setOnChatWithUserNameEvent( userName->createChatWithUserName(userName) );
	}

	/**
	 * Opens a chat with the contact who has the given username,
	 * making that chat session the active one, and changing the screen to the chat screen.
	 *
	 * If a chat session already exists then it uses that one, otherwise it creates it and adds it to the chats list.
	 * The thread is always created as {the lexicographically smaller Jid}AND{the lexicographically larger Jid}
	 *
	 * @param username the username of the contact
	 */
	public void chatWithContact(String username){
		AppChatSession chatSession = chats.getChatWithContact(username);
		if(chatSession == null){
			// no chat exists with that contact
			AppJid to = new AppJid(username, "teamorange.space");

			String other = username+"@teamorange.space";
			String self = contacts.getSelf().getJid().getBareJid();
			String thread = null;
			if(other.compareTo(self) < 0){
				// other is smaller
				thread = other + "AND" + self;
			} else {
				// other is larger
				thread = self + "AND" + other;
			}

			chatSession = babblerBase.createChatSessionWithGivenThread(to, thread);
			chats.addChat(chatSession);
		}
		chats.setActiveChat(chatSession);
		changeScreen.SetScreen(ScreenEnum.CHAT);
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}
