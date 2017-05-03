package TeamOrange.instantmessenger;

import java.util.LinkedList;
import TeamOrange.instantmessenger.xmpp.BabblerBase;


public class TestScript_2_1 {
	
	BabblerBase babblerBase;
		
	TestScript_2_1() {

		 babblerBase.createUser("usera", "usera");
		 System.out.println("Created usera");
		 babblerBase.createUser("userb", "userb");
		 System.out.println("Created userb");
		 babblerBase.login("usera", "usera");
		 
		 babblerBase.addContact("userb@teamorange.space");
	}
}
