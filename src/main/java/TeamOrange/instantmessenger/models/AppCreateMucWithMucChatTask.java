package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.MUCController;

public class AppCreateMucWithMucChatTask implements AppTask{

	private MUCController controller;
	private MUCChat mucChat;

	public AppCreateMucWithMucChatTask(MUCController controller, MUCChat mucChat){
		this.controller = controller;
		this.mucChat = mucChat;
	}

	@Override
	public void complete() {
		controller.actuallyCreateMUC(mucChat);
	}

}
