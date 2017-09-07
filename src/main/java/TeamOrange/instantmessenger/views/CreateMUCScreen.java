package TeamOrange.instantmessenger.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.CreateMUCEvent;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
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

public class CreateMUCScreen extends Screen	{

	private ScrollPane contacts;
	private VBox contactsContent, mainVBox;
	private HBox topHbox, inviteHBox;
	private ChangeScreen changeScreen;
	private TextField mucName;
	private LinkedList<MUCContactDisplay> contactDisplayList;
	private List<AppUser> mucList;
	private CreateMUCEvent createMUCEvent,inviteMUCEvent;
	private Image imageMessage;
	private Image imageNewMessage;

	public CreateMUCScreen(GuiBase guiBase){
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
		Button createButton = new Button("Create");
		createButton.setMinWidth(100);
		createButton.setOnAction(e->createMUC());
		createButton.setFocusTraversable(false);
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
				cancelButton,screenNameLabel,createButton);
		topHbox.setAlignment(Pos.CENTER);
		inviteHBox = new HBox(cancelInviteButton,inviteLabel,inviteButton);
		inviteHBox.setAlignment(Pos.CENTER);

		// MCU User Input
		mucName = new TextField();
		mucName.setMinHeight(35);
		mucName.setPromptText("Name this group chat");
		mucName.requestFocus();
		mucName.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
		mucName.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER){
				createMUC();
			} else if(e.getCode() == KeyCode.ESCAPE){
				clear();
			}
		});

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

		mucName.setEditable(true);
		// VBox Container holds all Objects
		mainVBox = new VBox();
		mainVBox.getChildren().addAll(topHbox, mucName, contacts);
		this.getChildren().add(mainVBox);
	}

	public void loadLater(LinkedList<AppUser> contactList){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				load(contactList);
			}
		});
	}

	/**
	 * Load Contacts
	 * @param contacts
	 */
	public void load(LinkedList<AppUser> appUsers){
		mucName.clear();
		contactsContent.getChildren().clear();
		contactDisplayList.clear();
		mucList.clear();

		for(AppUser appUser : appUsers){
			MUCContactDisplay contact = new MUCContactDisplay(appUser);
			contact.setOnSelectAppUser(selectAppUser->selectContact(selectAppUser));
			contactsContent.getChildren().add(contact);
			contactDisplayList.add(contact);
		}
		mainVBox.getChildren().clear();
		mainVBox.getChildren().addAll(topHbox, mucName, contacts);
	}
	
	public void load(String roomID, LinkedList<AppUser> appUsers) {
		mucName.setText(roomID);
		mucName.setEditable(false);
		contactsContent.getChildren().clear();
		contactDisplayList.clear();
		mucList.clear();
		
		for(AppUser appUser : appUsers){
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
		mucName.clear();
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
	 * Create MUC
	 */
	private void createMUC() {
		if (!mucList.isEmpty() && !mucName.getText().isEmpty()) {
			MUCChat muc = new MUCChat(mucName.getText(), mucList);
			createMUCEvent.getMUC(muc);
			clear();
		}
	}
	
	private void invite() {
		if (!mucList.isEmpty() && !mucName.getText().isEmpty()) {
			MUCChat muc = new MUCChat(mucName.getText(), mucList);
			inviteMUCEvent.getMUC(muc);
			clear();
		}
	}

	public void setOnCreateMUCEvent(CreateMUCEvent createMUCEvent) {
		this.createMUCEvent = createMUCEvent;
	}
	
	public void setOnInviteMUCEvent(CreateMUCEvent inviteMUCEvent) {
		this.inviteMUCEvent = inviteMUCEvent;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
