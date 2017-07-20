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

	public Collection<Occupant> getOccupants(XmppClient client, Jid roomAddress){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom room = manager.createChatRoom(roomAddress);
		Collection<Occupant> occupants = room.getOccupants();
		return occupants;
	}

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

	public void sendMessage(XmppClient client, Jid roomJid, String message){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);
		chatRoom.sendMessage(message);
	}

	public void leaveRoom(XmppClient client, Jid roomJid){
		MultiUserChatManager manager = client.getManager(MultiUserChatManager.class);
		ChatRoom chatRoom = manager.createChatRoom(roomJid);
		chatRoom.exit();
	}

	// discover ocupants
	// enter and leave
	// invite
	// accept and deny invitation
	// send messages
	// recieve messages and other events



}
