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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateMUCScreen extends Screen	{

	private ScrollPane contacts;
	private VBox contactsContent;
	private ChangeScreen changeScreen;
	private TextField mucName;
	private LinkedList<MUCContactDisplay> contactDisplayList;
	private List<AppUser> mucList;
	MUCChat mucChat;
	CreateMUCEvent createMUCEvent;



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
		Button createButton = new Button("Create");
		createButton.setMinWidth(100);
		createButton.setOnAction(e->createMUC());
		Label screenNameLabel = new Label("New Group");
		screenNameLabel.setMinWidth(100);
		screenNameLabel.setAlignment(Pos.CENTER);
		HBox topHbox = new HBox(
				cancelButton,screenNameLabel,createButton);
		topHbox.setAlignment(Pos.CENTER);

		// MCU User Input
		mucName = new TextField();
		mucName.setMinHeight(35);
		mucName.setPromptText("Name this group chat");

		// Contacts List
		contacts = new ScrollPane();
		contactsContent = new VBox();
		contactsContent.setPrefHeight(400);
		contactsContent.setPrefWidth(400);
		contacts.setContent(contactsContent);
		contacts.setFitToWidth(true);
		contacts.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		contactDisplayList = new LinkedList<MUCContactDisplay>();
		mucList = new ArrayList<AppUser>();

		// VBox Container holds all Objects
		VBox vbox = new VBox();
		vbox.getChildren().addAll(topHbox, mucName, contacts);
		this.getChildren().add(vbox);
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
	public void load(LinkedList<AppUser> contacts){
		mucName.clear();
		contactsContent.getChildren().clear();
		contactDisplayList.clear();
		mucList.clear();

		for(AppUser appUser : contacts){
			MUCContactDisplay contact = new MUCContactDisplay(appUser);
			contact.setOnSelectAppUser(selectAppUser->selectContact(selectAppUser));
			contactsContent.getChildren().add(contact);
			contactDisplayList.add(contact);
		}
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
	public void createMUC() {
		if (!mucList.isEmpty() && !mucName.getText().isEmpty()) {
			MUCChat muc = new MUCChat(mucName.getText(), mucList);
			createMUCEvent.getMUC(muc);
			clear();
		}
	}

	public void setOnCreateMUCEvent(CreateMUCEvent createMUCEvent) {
		this.createMUCEvent = createMUCEvent;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
