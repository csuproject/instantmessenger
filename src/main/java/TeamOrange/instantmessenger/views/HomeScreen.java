package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.AcceptContactRequestEvent;
import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeScreen extends Screen {

	private TextField addContactWithUsernameInputTextField;
	private Button addContactButton;
	private ScrollPane contacts;
	private VBox contactsContent;
	private ScrollPane contactRequest;
	private VBox contactRequestContent;
	private VBox mainVbox;
	private HBox addContactInput;
	private ChatWithContactEvent chatWithContactEvent;
	private AddContactEvent addContactEvent;
	private AcceptContactRequestEvent acceptContactRequestEvent;
	private DeclineContactRequestEvent declineContactRequestEvent;
	
	private List<MUCContactDisplay> displayList;
	private List<AppUser> appUserList;
	private Image imageMessage;
	private Image imageNewMessage;
	private String contactInFocus;

	public HomeScreen(GuiBase guiBase){
		super(guiBase);
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		
		appUserList = new ArrayList<AppUser>();
		displayList = new ArrayList<MUCContactDisplay>();
		
		// Chat status images
		imageMessage = new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false);
		imageNewMessage = new Image(getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false);
		
		// Build contacts display
		contacts = new ScrollPane();
		contactsContent = new VBox();
		contactsContent.setPrefHeight(500);
		contactsContent.setPrefWidth(500);
		contacts.setContent(contactsContent);
		contacts.setMaxHeight(500);
		contacts.setMinHeight(500);
		contacts.setFitToWidth(true);
		contacts.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		contacts.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		// Build contact request
		contactRequest = new ScrollPane();
		contactRequestContent = new VBox();
		contactRequestContent.setPrefHeight(500);
		contactRequestContent.setPrefWidth(500);
		contactRequest.setContent(contactRequestContent);
		contactRequest.setMaxHeight(200);
		//contactRequest.setMinHeight(500);
		contactRequest.setFitToWidth(true);
		contactRequest.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		contactRequest.setHbarPolicy(ScrollBarPolicy.NEVER);

		// add contact
		Label addContactWithUsername = new Label("Add Contact: ");
		addContactWithUsername.setFont(new Font(20));
		addContactWithUsernameInputTextField = new TextField();
		addContactWithUsernameInputTextField.setOnKeyPressed(keyEvent->addContactInputKeyPressed(keyEvent));
		// restrict input to lower case
		addContactWithUsernameInputTextField.textProperty().addListener(
		  (observable, oldValue, newValue) -> {
		    ((javafx.beans.property.StringProperty)observable).setValue(newValue.toLowerCase());
		  }
		);
		addContactButton = new Button("Add");
		addContactButton.setOnAction( e->addContactBtnPress() );
		addContactButton.setFocusTraversable(false);
		addContactInput = new HBox();
		addContactInput.getChildren().addAll(addContactWithUsername, addContactWithUsernameInputTextField, addContactButton);
		addContactInput.setSpacing(10);

		// Build main VBox
		mainVbox = new VBox();
		mainVbox.getChildren().addAll(contacts, addContactInput);
		mainVbox.setVgrow(contacts, javafx.scene.layout.Priority.ALWAYS);
		this.getChildren().add(mainVbox);
		this.setPrefHeight(600-50);
	}

	public void addContactInputKeyPressed(javafx.scene.input.KeyEvent keyEvent){
		if( keyEvent.getCode().equals(javafx.scene.input.KeyCode.ENTER) ){
			addContactBtnPress();
		}
	}

	public void chatButtonPress(String username) {
		System.out.println("Chat with " + username);
		chatWithContactEvent.openChat(username);
	}

	public void addContactBtnPress(){
		String username = addContactWithUsernameInputTextField.getText().trim();
		addContactWithUsernameInputTextField.clear();
		if(username.isEmpty()){
			alert("Username field must not be empty.", "empty username field", AlertType.WARNING);
		} else {
			this.addContactEvent.add(username);
		}
	}

	public void acceptContactRequestButtonPress(String username) {
		acceptContactRequestEvent.accept(username);
	}

	public void declineContactRequestButtonPress(String username) {
		declineContactRequestEvent.decline(username);
	}

	public void loadLater(HomeScreenInput input){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				loadNew(input);
			}
		});
	}

	public void load(HomeScreenInput input){
		contactsContent.getChildren().clear();

		LinkedList<AppJid> contactRequestList = input.getContactRequestList();
		for(AppJid jid : contactRequestList){
			ContactRequestDisplay request = new ContactRequestDisplay(this, jid.getLocal());
			contactsContent.getChildren().add(request);
		}

		LinkedList<AppUser> contactList = input.getContactList();
		for(AppUser user : contactList){
			ContactDisplay contact = new ContactDisplay(this, user.getJid().getLocal());
			contactsContent.getChildren().add(contact);
		}
	}
	
	/**
	 * Load new contacts
	 * @param mucList
	 */
	public void loadNew(HomeScreenInput input) {
		
		// Pass contact requests
		contactRequestContent.getChildren().clear();
		LinkedList<AppJid> contactRequestList = input.getContactRequestList();
		if(!contactRequestList.isEmpty()) {
			mainVbox.getChildren().clear();
			mainVbox.getChildren().addAll(contactRequest, contacts, addContactInput);
			for(AppJid jid : contactRequestList){
				ContactRequestDisplay request = new ContactRequestDisplay(this, jid.getLocal());
				contactRequestContent.getChildren().add(request);
			}
		} else {
			mainVbox.getChildren().clear();
			mainVbox.getChildren().addAll(contacts, addContactInput);
		}

		// Pass new AppUsers
		LinkedList<AppUser> contactList = input.getContactList();
		for(AppUser appUser : contactList){
			if(!this.appUserList.contains(appUser)) {
				MUCContactDisplay contactDisplay = 
						new MUCContactDisplay(appUser,imageMessage, imageNewMessage);
				contactDisplay.setOnSelectAppUser(e-> {
						chatWithContactEvent.openChat(e.getJid().getLocal());
						setContactInFocus(appUser.getName());
				});
				this.appUserList.add(appUser);
				displayList.add(contactDisplay);
				contactsContent.getChildren().add(contactDisplay);
			}
		}
	}

	/**
	 * Load new message notifications
	 * @param mucList
	 */
	public void loadNewMessage(String appUser) {

		// Find MUC index
		for(MUCContactDisplay display : displayList) {
			if(display.appUser.getName().equals(appUser)) {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						display.setNewMessageImage();}});					 
			}
		}
	}
	
	/**
	 * Set contact in display focus
	 * @param contactInFocus
	 */
	private void setContactInFocus(String contactInFocus) {
		this.contactInFocus = contactInFocus;
	}
	
	/**
	 * Get contact in display focus
	 * @return
	 */
	public String getContactInFocus() {
		return contactInFocus;
	}

	public void setOnChatWithContactEvent(ChatWithContactEvent chatWithContactEvent){
		this.chatWithContactEvent = chatWithContactEvent;
	}

	public void setOnAddContactEvent(AddContactEvent addContactEvent){
		this.addContactEvent = addContactEvent;
	}

	public void setOnAcceptContactRequestEvent(AcceptContactRequestEvent acceptContactRequestEvent){
		this.acceptContactRequestEvent = acceptContactRequestEvent;
	}

	public void setOnDeclineContactRequestEvent(DeclineContactRequestEvent declineContactRequestEvent){
		this.declineContactRequestEvent = declineContactRequestEvent;
	}

	public void alert(String message, String title, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
