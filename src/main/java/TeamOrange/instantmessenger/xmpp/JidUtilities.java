package TeamOrange.instantmessenger.xmpp;

import TeamOrange.instantmessenger.models.AppJid;
import rocks.xmpp.addr.Jid;

public class JidUtilities {

	/**
	 * Takes an AppJid and returns an equivalent Jid
	 * @param appJid the AppJid to convert
	 * @return jid the Jid that has been converted from an AppJid
	 */
	public static Jid jidFromAppJid(AppJid appJid){
		Jid jid = Jid.of(appJid.getLocal(), appJid.getDomain(), appJid.getResource());
		return jid;
	}

	/**
	 * Takes a Jid and returns an equivalent AppJid
	 * @param jid the Jid to convert
	 * @return appJid the AppJid that has been converted from an Jid
	 */
	public static AppJid appJidFromJid(Jid jid){
		AppJid appJid = new AppJid( jid.getLocal(), jid.getDomain(), jid.getResource());
		return appJid;
	}

}
