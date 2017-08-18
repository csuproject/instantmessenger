package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.ChatController;

public class AppSendChatSessionMessageTask implements AppTask{

	private ChatController controller;
	private AppChatSessionMessage message;
	private String userName;

	public AppSendChatSessionMessageTask(ChatController controller, AppChatSessionMessage message, String userName){
		this.controller = controller;
		this.message = message;
		this.userName = userName;
	}

	@Override
	public void complete() {
		controller.actuallySendChatSessionMessage(message, userName);
	}

}
