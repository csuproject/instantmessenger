package TeamOrange.instantmessenger;

import java.util.LinkedList;
import java.util.Scanner;

import TeamOrange.instantmessenger.controllers.ConnectionEventEnum;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import exceptions.ConfideXmppException;

public class ContactTest {

	public static void main(String[] args){
		BabblerBase client = init();
		Scanner in = new Scanner(System.in);
		String name = "";
		String password = "";
		boolean running = true;

		try {
			System.out.print("name: ");
			name = in.nextLine();
			System.out.print("password: ");
			password = in.nextLine();
			client.login(name, password);
			while(running){
				System.out.print("(add {name} | delete {name} | block {name} | print | exit): ");
				String input = in.nextLine();
				String[] splitInput = input.split(" ");
				if(input.toUpperCase().equals("EXIT")){
					running = false;
				} else if(input.toUpperCase().equals("PRINT")){
					printContacts(client);
				} else if(splitInput.length == 2) {
					if(splitInput[0].toUpperCase().equals("ADD")){
						client.addContact(splitInput[1]);
					} else if(splitInput[0].toUpperCase().equals("DELETE")){
						client.removeContact(splitInput[1]);
					} else if(splitInput[0].toUpperCase().equals("BLOCK")){
						client.blockUser(splitInput[1]);
						client.printBlockList();
					}
				}
			}
		} catch (ConfideXmppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void printContacts(BabblerBase client){
		LinkedList<AppUser> contacts = client.getContactsAsAppUsers();
		System.out.println("----- Contacts -----");
		for(AppUser u : contacts)
			System.out.println( u.getJid().getBareJid() + " : " +
					u.getPresence().sGetType() );
		System.out.println("--------------------");
	}

	public static BabblerBase init(){
		BabblerBase client =
				new BabblerBase("teamorange.space",
						appMessage->messageListener(appMessage),
						(fromJid, appPresenceType)->presenceListener(fromJid, appPresenceType),
						() -> rosterListener(),
						type->connectionEventListener(type));

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

	private static Object presenceListener(AppJid fromJid, AppPresence.Type appPresenceType) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void messageListener(AppMessage appMessage) {
		// TODO Auto-generated method stub
	}

	public static void connectionEventListener(ConnectionEventEnum type){

    }

}
