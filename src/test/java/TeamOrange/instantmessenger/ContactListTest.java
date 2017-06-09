package TeamOrange.instantmessenger;

import TeamOrange.instantmessenger.models.AppMessage;import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;

public class ContactListTest {

	public static void main(String[] args) throws ConfideXmppException, InterruptedException{


		BabblerBase client = 
				new BabblerBase("localhost", 
						appMessage->messageListener(appMessage), 
						appPresence->presenceListener(appPresence), 
						() -> rosterListener());
		
		client.setupConnection();
    	try {
			client.connect();
		} catch (ConfideXmppException e) {
			e.printStackTrace();
		}


    	//client.createUser("1", "1");
    	//client.createUser("4", "4");

    	client.login("1", "1");

    	//client.addContact("4");

    	//Thread.sleep(3000);
    	//client.createUser("aaa", "aaa");
    	//Thread.sleep(3000);
    	//client.createUser("bbb", "bbb");
    	///Thread.sleep(3000);
    	//client.login("tim", "tim95bell");
    	//Thread.sleep(3000);

    	//client.addContact("test");
    	//Thread.sleep(9000);

    	System.out.println(client.getContacts());

	}

	private static Object rosterListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private static Object presenceListener(AppPresence appPresence) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Object messageListener(AppMessage appMessage) {
		// TODO Auto-generated method stub
		return null;
	}
}
