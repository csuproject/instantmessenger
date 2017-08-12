package TeamOrange.instantmessenger;

import java.util.LinkedList;

import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import rocks.xmpp.core.XmppException;

public class TestScript_2_3 {

	BabblerBase babblerBase;

	TestScript_2_3() throws XmppException {

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

		 LinkedList<String> list = new LinkedList<String>();
		 list =  babblerBase.getContacts();
		 System.out.println("List Contacts assigned to usera roster");
		 for (String l : list) {
			 System.out.println(l);
		 }

		 System.out.println("Remove userb from roster");
		 babblerBase.removeContact("userb@teamorange.space");

		 LinkedList<String> list2 = new LinkedList<String>();
		 list =  babblerBase.getContacts();
		 System.out.println("List Contacts assigned to usera roster");
		 for (String l2 : list2) {
			 System.out.println(l2);
		 }
	}
}
