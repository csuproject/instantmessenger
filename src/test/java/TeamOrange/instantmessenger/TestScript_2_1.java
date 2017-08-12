package TeamOrange.instantmessenger;

import java.util.LinkedList;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import rocks.xmpp.core.XmppException;


public class TestScript_2_1 {

	BabblerBase babblerBase;

	TestScript_2_1() throws XmppException {

		 babblerBase.createUser("usera", "usera");
		 System.out.println("Created usera");
		 babblerBase.createUser("userb", "userb");
		 System.out.println("Created userb");
		 try {
			babblerBase.login("usera", "usera");
		} catch (ConfideXmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 babblerBase.addContact("userb@teamorange.space");
	}
}
