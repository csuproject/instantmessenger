package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.controllers.AcceptOrDeclineContactRequestController;

public class AppReplyToContactRequestTask implements AppTask {
	private AcceptOrDeclineContactRequestController controller;
	private AppUser contact;
	private AppJid jid;
	private boolean accepted;

	public AppReplyToContactRequestTask(AcceptOrDeclineContactRequestController controller, AppUser contact, AppJid jid, boolean accepted){
		this.controller = controller;
		this.contact = contact;
		this.jid = jid;
		this.accepted = accepted;
	}

	@Override
	public void complete() {
		controller.actuallyReplyToContactRequest(contact, jid, accepted);
	}

}
