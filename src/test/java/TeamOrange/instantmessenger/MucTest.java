package TeamOrange.instantmessenger;

import java.util.Collection;
import java.util.Scanner;

import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;
import rocks.xmpp.extensions.muc.Occupant;

public class MucTest {

	public static void main(String[] args){
		BabblerBase aClient = init();
		BabblerBase bClient = init();
		String roomID = "MucTestRoom";

		AppMuc aMuc = null;
		AppMuc bMuc = null;

		try {
			aClient.login("a", "a");
			aClient.sendGroupMessage(roomID, "dgfd");
			//aMuc = aClient.createAndOrEnterRoom("mucTest", "aNick");
		} catch (ConfideXmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			bClient.login("b", "b");
//			bMuc = bClient.createAndOrEnterRoom("mucTest", "bNick");
//			bMuc.sendMessage("hey its b");
//		} catch (ConfideXmppException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		aMuc.sendMessage("hi this is a");
//
//		bMuc.sendMessage("how are you going a?");
//
//		//aClient.getMembers(roomID, "aNick");

		Scanner in = new Scanner(System.in);
		in.nextInt();
	}

	public static BabblerBase init(){
		BabblerBase client =
				new BabblerBase("teamorange.space",
						appMessage->messageListener(appMessage),
						appPresence->presenceListener(appPresence),
						() -> rosterListener());

		client.setupConnection();
    	try {
			client.connect();
		} catch (ConfideXmppException e) {
			System.out.println("------- exception conecting ---------");
			e.printStackTrace();
		}
    	return client;
	}

	private static void rosterListener() {
		// TODO Auto-generated method stub
	}

	private static void presenceListener(AppPresence appPresence) {
		// TODO Auto-generated method stub
	}

	private static void messageListener(AppMessage appMessage) {
		// TODO Auto-generated method stub
	}
}
