package TeamOrange.instantmessenger.xmpp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucMessage;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.muc.ChatRoom;
import rocks.xmpp.extensions.muc.ChatService;
import rocks.xmpp.extensions.muc.MultiUserChatManager;
import rocks.xmpp.extensions.muc.Occupant;
import rocks.xmpp.extensions.muc.OccupantEvent.Type;
import rocks.xmpp.extensions.muc.model.Item;
import rocks.xmpp.extensions.muc.model.Role;
import rocks.xmpp.extensions.muc.model.RoomConfiguration;
import rocks.xmpp.extensions.register.model.Registration;
import rocks.xmpp.util.concurrent.AsyncResult;

public class MucManager {
	MultiUserChatManager manager;

	public MucManager(){
	}

//	public void setupInvitationListener(XmppClient client){
//		manager = client.getManager(MultiUserChatManager.class);
//		manager.addInvitationListener( e->{
//			ChatService chatService = manager.createChatService(Jid.of(e.getRoomAddress().getDomain()));
//			ChatRoom chatRoom = chatService.createRoom(e.getRoomAddress().getLocal());
//			System.out.println("received join request");
//			Registration registration = Registration.builder()
//				     .username("user")
//				     .nickname("Nick Name")
//				     .build();
//			chatRoom.register(registration);
//		});
//	}

//	public List<Item> getMembers(XmppClient client, Jid roomAddress, String nick){
//		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
//		ChatRoom room = manager.createChatRoom(roomAddress);
//		try {
//			List<Item> members = (List<Item>) room.getMembers().getResult();
//			return members;
//		} catch (XmppException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * Gets a Collection of Occupants of the chat room, that is people currently in the chat room.
	 * @param client the XmppClient object used to communicate with the server
	 * @param roomAddress a Jid representing the address of the room
	 * @return a Collection of Occupant representing each person in the room
	 */
	public Collection<Occupant> getOccupants(XmppClient client, Jid roomAddress){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom room = manager.createChatRoom(roomAddress);
		Collection<Occupant> occupants = room.getOccupants();
		return occupants;
	}

	/**
	 * Creates a room with address roomJid, or retreives it if it already exists.
	 * Configures the room.
	 * Enters the room with the given nickname
	 * Creates a AppMuc object to represent the room
	 * Retrieves a list of occupants and puts them in the AppMuc object
	 * Sets up message adn occupant listeners, and connects them to the AppMuc object
	 * returns the AppMuc object
	 * @param client the XmppClient object used to communicate with the server
	 * @param babblerBase
	 * @param roomJid a Jid representing the address of the room
	 * @param nick the nickname to enter with
	 * @return an AppMuc object representing the room
	 */
	public AppMuc createAndOrEnterRoom(XmppClient client, BabblerBase babblerBase, Jid roomJid, String nick){
		// assuming using roomID@conference.teamorange.space
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);
		RoomConfiguration roomConfiguration = RoomConfiguration.builder().persistent(true)
				.rolesThatMayDiscoverRealJids( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR, Role.NONE) )
				.rolesThatMayRetrieveMemberList( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR, Role.NONE) )
				.build();
		chatRoom.configure(roomConfiguration);
		AsyncResult<Presence> enterResult = chatRoom.enter(nick);
		try {
			enterResult.getResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: get collection of AppMucs to do this, so it can return an existing one if one exists
		AppMuc muc = new AppMuc(roomJid.getLocal(), nick, babblerBase);

		Collection<Occupant> occupants = chatRoom.getOccupants();
		for(Occupant o : occupants){
			muc.occupantEntered(o.getNick());
		}
		chatRoom.addInboundMessageListener( me->{
			String body = me.getMessage().getBody();
			String from = me.getMessage().getFrom().getResource();
			AppMucMessage message = new AppMucMessage(body, from);
			muc.inboundMessage(message);
		});
		chatRoom.addOccupantListener( oe->{
			if (!oe.getOccupant().isSelf()) {
		        switch (oe.getType()) {
		            case ENTERED:
		                muc.occupantEntered( oe.getOccupant().getNick() );
		                break;
		            case EXITED:
		            	muc.occupantExited( oe.getOccupant().getNick() );
		                break;
		            case KICKED:
		            	muc.occupantKicked( oe.getOccupant().getNick() );
		                break;
		        }
		    }
		});

		return muc;
	}

	/**
	 * Sends a message to the muc with the given jid
	 * @param client
	 * @param roomJid a Jid representing the address of the room
	 * @param message the message to send
	 */
	public void sendMessage(XmppClient client, Jid roomJid, String message){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);
		chatRoom.sendMessage(message);
	}

	/**
	 * Leaves the muc room
	 * @param client
	 * @param roomJid a Jid representing the address of the room
	 */
	public void leaveRoom(XmppClient client, Jid roomJid){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);
		chatRoom.exit();
	}

}
