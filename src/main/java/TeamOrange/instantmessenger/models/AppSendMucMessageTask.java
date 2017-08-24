package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.MUCController;

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
