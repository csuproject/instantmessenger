package TeamOrange.instantmessenger.models;

import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.lambda.StatusEvent;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * This class links the appearance of a simple javafx shape to the presence
 * information which is being sent from the XMPP server to this client
 *
 * @author Muz
 *
 */

public class AppPresenceIndicator {

	// private AppUser appUser;
	private AppJid appJid;
	private AppPresence appPresence;
	private AppUser appUser;

	public AppPresenceIndicator(String contactName, String domain) {

		appJid = new AppJid(contactName, domain);

		appUser = new AppUser(appJid, appPresence);
	}

	public Color setIndicatorColour() {
		String presence;
		appPresence = this.appUser.getPresence();
		try {
		presence = appPresence.sGetType();
		} catch (NullPointerException ex){
			//currently not getting presence info yet, so returns a null pointer error
			presence = "OTHER";
		}
		if (presence == "AVAILABLE"){
			return Color.GREEN;
		}else{
			if (presence == "UNAVAILABLE"){
				return Color.RED;
			}else{
				return Color.LIGHTGREY;
			}
		}

	}

}
