package TeamOrange.instantmessenger.models.tasks;

import TeamOrange.instantmessenger.controllers.AcceptOrDeclineContactRequestController;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;

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
