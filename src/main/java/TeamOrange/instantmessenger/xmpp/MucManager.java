package TeamOrange.instantmessenger.xmpp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucMessage;
import exceptions.ConfideFailedToConfigureChatRoomException;
import exceptions.ConfideFailedToEnterChatRoomException;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.stanza.MessageEvent;
import rocks.xmpp.core.stanza.model.IQ;
import rocks.xmpp.core.stanza.model.Message;
import rocks.xmpp.core.stanza.model.Presence;
import rocks.xmpp.extensions.muc.ChatRoom;
import rocks.xmpp.extensions.muc.ChatService;
import rocks.xmpp.extensions.muc.MultiUserChatManager;
import rocks.xmpp.extensions.muc.Occupant;
import rocks.xmpp.extensions.muc.OccupantEvent.Type;
import rocks.xmpp.extensions.muc.model.DiscussionHistory;
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
	 * @throws ConfideFailedToEnterChatRoomException
	 * @throws ConfideFailedToConfigureChatRoomException
	 */
	public AppMuc createAndOrEnterRoom(XmppClient client, BabblerBase babblerBase, Jid roomJid, String nick) throws ConfideFailedToEnterChatRoomException, ConfideFailedToConfigureChatRoomException {
		// assuming using roomID@conference.teamorange.space
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);

		// create AppMuc representatoin of this chat room
		// TODO: get collection of AppMucs to do this, so it can return an existing one if one exists
		AppMuc muc = new AppMuc(roomJid.getLocal(), nick, babblerBase);

		// setup listeners
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

		// enter the room and request discussion history
		DiscussionHistory discussionHistory = DiscussionHistory.forMaxMessages(1000);
		AsyncResult<Presence> enterResult = chatRoom.enter(nick, discussionHistory);
		try {
			enterResult.getResult();
		} catch (XmppException e) {
			throw new ConfideFailedToEnterChatRoomException();
		}

		// configure the room, this will only work if the room hasnt already been created by someone else, in which case the catch block will be entered.
		RoomConfiguration roomConfiguration = RoomConfiguration.builder()
				.persistent(true)
				.rolesThatMayDiscoverRealJids( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR) )
				.rolesThatMayRetrieveMemberList( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR) )
				.rolesThatMayRetrieveMemberList( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR) )
				.rolesThatMaySendPrivateMessages( Arrays.asList(Role.MODERATOR, Role.PARTICIPANT, Role.VISITOR) )
				.maxHistoryMessages(1000)
				.invitesAllowed(true)
				.membersOnly(false)
				.publicRoom(true)
				.loggingEnabled(true)
				.build();
		AsyncResult<IQ> configureResult = chatRoom.configure(roomConfiguration);
		try {
			IQ iq = configureResult.getResult();
		} catch (XmppException e) {
			// failed to configure, probably because it was already configured
		}

		// retrieve the rooms ocupants
		Collection<Occupant> occupants = chatRoom.getOccupants();
		for(Occupant o : occupants){
			muc.occupantEntered(o.getNick());
		}

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
