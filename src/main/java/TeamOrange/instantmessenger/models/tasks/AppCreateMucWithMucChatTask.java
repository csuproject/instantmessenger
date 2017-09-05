package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.MUCController;
import TeamOrange.instantmessenger.models.MUCChat;

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
