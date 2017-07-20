package TeamOrange.instantmessenger;

import java.util.Scanner;

import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;

public class MucTestInteractive {

	/**
	 * Made to test Multi user chats,
	 *
	 * @param args
	 */
	public static void main(String[] args){
		BabblerBase client = init();
		String roomID = "MucTestRoom";
		Scanner in = new Scanner(System.in);
		String name = "";
		String password = "";
		AppMuc muc = null;
		boolean running = true;

		try {
			System.out.print("name: ");
			name = in.nextLine();
			System.out.print("password: ");
			password = in.nextLine();
			client.login(name, password);
			muc = client.createAndOrEnterRoom(roomID, name+"Nick");
			while(running){
				System.out.print("Enter a message or exit: ");
				String input = in.nextLine();
				if(input.toUpperCase().equals("EXIT")){
					running = false;
				} else{
					muc.sendMessage(input);
				}
			}
		} catch (ConfideXmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
