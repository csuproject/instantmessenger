package TeamOrange.instantmessenger.lambda;

import TeamOrange.instantmessenger.models.AppJid;

public interface RequestContactAddSentListener {
	public void contactAddRequestSent(AppJid to);
}
