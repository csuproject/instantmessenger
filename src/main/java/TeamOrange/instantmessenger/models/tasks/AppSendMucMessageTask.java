package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.MUCController;
import TeamOrange.instantmessenger.models.AppMuc;

public class AppSendMucMessageTask implements AppTask {

	private MUCController controller;
	private AppMuc muc;
	private String message;

	public AppSendMucMessageTask(MUCController controller, AppMuc muc, String message){
		this.controller = controller;
		this.muc = muc;
		this.message = message;
	}

	@Override
	public void complete() {
		controller.actuallySendMUCMessage(muc, message);
	}

}
