package TeamOrange.instantmessenger.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.CreateMUCEvent;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.InviteToMucEvent;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This is the muc invite screen, in which the user can invite their contacts to a muc.
 *
 */
public class MucInviteScreen extends Screen	{

	private ScrollPane contacts;
	private VBox contactsContent, mainVBox;
	private HBox topHbox, inviteHBox;
	private ChangeScreen changeScreen;
	private Label mucName;
	private LinkedList<MUCContactDisplay> contactDisplayList;
	private List<AppUser> mucList;
	private Image imageMessage;
	private Image imageNewMessage;
	private InviteToMucEvent inviteToMucEvent;

	public MucInviteScreen(GuiBase guiBase){
		super(guiBase);
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create Screen
	 * @throws Exception
	 */
	public void create() throws Exception {

		// MCU create control Top HBox
		Button cancelButton = new Button("Cancel");
		cancelButton.setMinWidth(100);
		cancelButton.setOnAction(e->clear());
		cancelButton.setFocusTraversable(false);
		Button cancelInviteButton = new Button("Cancel");
		cancelInviteButton.setMinWidth(100);
		cancelInviteButton.setOnAction(e->clear());
		cancelInviteButton.setFocusTraversable(false);
		Button inviteButton = new Button("Send");
		inviteButton.setMinWidth(100);
		inviteButton.setOnAction(e->invite());
		inviteButton.setFocusTraversable(false);
		Label screenNameLabel = new Label("New Group");
		screenNameLabel.setMinWidth(100);
		screenNameLabel.setAlignment(Pos.CENTER);
		Label inviteLabel = new Label("Invite Group");
		inviteLabel.setMinWidth(100);
		inviteLabel.setAlignment(Pos.CENTER);
		topHbox = new HBox(
				cancelButton,screenNameLabel);
		topHbox.setAlignment(Pos.CENTER);
		inviteHBox = new HBox(cancelInviteButton,inviteLabel,inviteButton);
		inviteHBox.setAlignment(Pos.CENTER);

		// MCU User Input
		mucName = new Label();
		mucName.setMinHeight(35);
		mucName.setAlignment(Pos.CENTER);

		// Contacts List
		contacts = new ScrollPane();
		contacts.setOnMouseClicked(e->mucName.requestFocus());
		contactsContent = new VBox();
		contactsContent.setPrefHeight(400);
		contactsContent.setPrefWidth(400);
		contacts.setContent(contactsContent);
		contacts.setFitToWidth(true);
		contacts.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		contacts.setMaxHeight(450);
		contacts.setMinHeight(450);
		contactDisplayList = new LinkedList<MUCContactDisplay>();
		mucList = new ArrayList<AppUser>();
		this.setMinHeight(450);
		this.setMaxHeight(450);

		imageMessage = new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false);
		imageNewMessage = new Image(getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false);

		// VBox Container holds all Objects
		mainVBox = new VBox();
		mainVBox.getChildren().addAll(topHbox, mucName, contacts);
		this.getChildren().add(mainVBox);
	}

//	/**
//	 * Load Contacts
//	 * @param contacts
//	 */
//	public void load(LinkedList<AppUser> appUsers){
//		mucName.clear();
//		contactsContent.getChildren().clear();
//		contactDisplayList.clear();
//		mucList.clear();
//
//		for(AppUser appUser : appUsers){
//			MUCContactDisplay contact = new MUCContactDisplay(appUser);
//			contact.setOnSelectAppUser(selectAppUser->selectContact(selectAppUser));
//			contactsContent.getChildren().add(contact);
//			contactDisplayList.add(contact);
//		}
//		mainVBox.getChildren().clear();
//		mainVBox.getChildren().addAll(topHbox, mucName, contacts);
//	}

	@Override
	/**
	 * Loads the screen based on the input.
	 * @param input the input to load the screen based on.
	 */
	public void load(ScreenInput input) {
		MucInviteScreenInput mucInviteScreenInput = (MucInviteScreenInput)input;
		AppMuc muc = mucInviteScreenInput.getMucInvitingTo();
		String roomID = muc.getRoomID();
		List<AppUser> appUsers = mucInviteScreenInput.getContacts();

		mucName.setText("Invite contacts to the group \""+roomID+"\"");
		contactsContent.getChildren().clear();
		contactDisplayList.clear();
		mucList.clear();

		System.out.println("mucInviteScreenInput.getContacts(): " + appUsers.size());
		for(AppUser appUser : appUsers){
			System.out.println("user: " + appUser.getName());
			MUCContactDisplay contact = new MUCContactDisplay(appUser);
			contact.setOnSelectAppUser(selectAppUser->selectContact(selectAppUser));
			contactsContent.getChildren().add(contact);
			contactDisplayList.add(contact);
		}

		mainVBox.getChildren().clear();
		mainVBox.getChildren().addAll(inviteHBox, mucName, contacts);
	}

	/**
	 * Clears create MUC
	 */
	public void clear() {
		changeScreen.SetScreen(ScreenEnum.MUC);
	}

	/**
	 * Add or remove selected contacts to MUC List
	 * @param selectAppUser
	 */
	public void selectContact(AppUser selectAppUser) {
		if (mucList.contains(selectAppUser)) {
			mucList.remove(selectAppUser);
			System.out.println("Removed: " + selectAppUser.getJid());
		} else {
			mucList.add(selectAppUser);
			System.out.println("Added: " + selectAppUser.getJid());
		}
		System.out.println(selectAppUser.getJid());
	}

	/**
	 * Invites the list of selected contacts to the muc.
	 */
	private void invite() {

		inviteToMucEvent.invite(mucList);
		clear();

//		if (!mucList.isEmpty() && !mucName.getText().isEmpty()) {
//			MUCChat muc = new MUCChat(mucName.getText(), mucList);
//			inviteMUCEvent.getMUC(muc);
//			clear();
//		}
	}

	public void setOnInviteToMucEvent(InviteToMucEvent inviteToMucEvent){
		this.inviteToMucEvent = inviteToMucEvent;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
