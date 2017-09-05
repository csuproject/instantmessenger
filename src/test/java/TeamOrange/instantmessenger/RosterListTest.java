package TeamOrange.instantmessenger;

import java.util.LinkedList;

import TeamOrange.instantmessenger.controllers.ConnectionEventEnum;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import rocks.xmpp.core.XmppException;

public class RosterListTest {

	public static void main(String[] args) throws XmppException{
		RosterListTest t = new RosterListTest();
	}

	BabblerBase babblerBase;

	RosterListTest() throws XmppException {
		babblerBase = new BabblerBase("teamorange.space", appMessage->messageListener(appMessage), (fromJid, appPresenceType)->presenceListener(fromJid, appPresenceType), () -> rosterListener(),
				type->connectionEventListener(type));

		babblerBase.setupConnection();
    	try {
			babblerBase.connect();
		} catch (ConfideXmppException e) {
			e.printStackTrace();
		}

		 babblerBase.createUser("userc", "userc");
		 System.out.println("Created userc");
		 babblerBase.createUser("userd", "userd");
		 System.out.println("Created userd");

		 System.out.println("Login as userc");
		 try {
			babblerBase.login("userc", "userc");
		} catch (ConfideXmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("Assign userd to userc roster");
		 babblerBase.addContact("userd@teamorange.space");

		 LinkedList<String> list = new LinkedList<String>();
		 list =  babblerBase.getContacts();
		 System.out.println("List Contacts assigned to userc roster");
		 for (String l : list) {
			 System.out.println(l);
	 	 }
	}

	private Object rosterListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private static Object presenceListener(AppJid fromJid, AppPresence.Type appPresenceType) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object messageListener(AppMessage appMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void connectionEventListener(ConnectionEventEnum type){

    }
}