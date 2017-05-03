package TeamOrange.instantmessenger;

import java.util.LinkedList;

import TeamOrange.instantmessenger.xmpp.BabblerBase;

public class TestScript_2_2 {

	BabblerBase babblerBase;
	
	TestScript_2_2() {
		
		 babblerBase.createUser("usera", "usera");
		 System.out.println("Created usera");
		 babblerBase.createUser("userb", "userb");
		 System.out.println("Created userb");
		 
		 System.out.println("Login as usera");
		 babblerBase.login("usera", "usera");
		 System.out.println("Assign userb to usera roster");
		 babblerBase.addContact("userb@teamorange.space");

		 LinkedList<String> list = new LinkedList<String>();
		 list =  babblerBase.getContacts();
		 System.out.println("List Contacts assigned to usera roster");
		 for (String l : list) {
			 System.out.println(l);
	 	 } 	
	}
}
