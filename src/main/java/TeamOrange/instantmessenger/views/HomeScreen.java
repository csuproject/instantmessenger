package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.AcceptContactRequestEvent;
import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.models.AppJid;
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
	private ScrollPane contacts,contactRequest;
	private VBox contactsContent,contactRequestContent,mainVbox;
	private HBox addContactInput;
	private ChatWithContactEvent chatWithContactEvent;
	private AddContactEvent addContactEvent;
	private AcceptContactRequestEvent acceptContactRequestEvent;
	private DeclineContactRequestEvent declineContactRequestEvent;
	private List<MUCContactDisplay> displayList;
	private List<AppUser> appUserList;
	private List<ContactRequestDisplay> contactRequestDisplayList;
	private Image imageMessage,imageNewMessage,imageOnline,imageOffline;
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

		//////////////////////////////////////////////////////////////////////////////
		//------------------------------Image Resources-----------------------------//
		//////////////////////////////////////////////////////////////////////////////
		imageMessage = new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false);
		imageNewMessage = new Image(getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false);
		imageOnline = new Image(getClass().getResource(
				"/resources/accept-icon.png").toURI().toString(),25,25,false,false);
		imageOffline = new Image(getClass().getResource(
				"/resources/decline-icon.png").toURI().toString(),25,25,false,false);
		
		//////////////////////////////////////////////////////////////////////////////
		//------------------------------Contact Display-----------------------------//
		//////////////////////////////////////////////////////////////////////////////
		contacts = new ScrollPane();
		contacts.setFocusTraversable(false);
		contacts.setOnMouseClicked((e)->addContactWithUsernameInputTextField.requestFocus());
		contacts.setStyle("-fx-focus-color: transparent;");
		contactsContent = new VBox();
		contactsContent.setMaxHeight(500);
		contactsContent.setPrefWidth(500);
		contacts.setContent(contactsContent);
		contacts.setMaxHeight(500);
		contacts.setMinHeight(500);
		contacts.setFitToWidth(true);
		contacts.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		contacts.setHbarPolicy(ScrollBarPolicy.NEVER);

		//////////////////////////////////////////////////////////////////////////////
		//------------------------------Add Contact Display-------------------------//
		//////////////////////////////////////////////////////////////////////////////
		Label addContactWithUsername = new Label("Add Contact: ");
		addContactWithUsername.setFont(new Font(20));
		addContactWithUsernameInputTextField = new TextField();
		addContactWithUsernameInputTextField.setOnKeyPressed(
				keyEvent->addContactInputKeyPressed(keyEvent));
		// Restrict input to lower case
		addContactWithUsernameInputTextField.textProperty().addListener(
		  (observable, oldValue, newValue) -> {
		    ((javafx.beans.property.StringProperty)observable)
		    	.setValue(newValue.toLowerCase());});
		addContactButton = new Button("Add");
		addContactButton.setOnAction( e->addContactBtnPress() );
		addContactButton.setFocusTraversable(false);
		addContactInput = new HBox();
		addContactInput.getChildren().addAll(addContactWithUsername, addContactWithUsernameInputTextField, addContactButton);
		addContactInput.setSpacing(10);

		//////////////////////////////////////////////////////////////////////////////
		//----------------------------------Screen----------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		appUserList = new ArrayList<AppUser>();
		displayList = new ArrayList<MUCContactDisplay>();
		contactRequestDisplayList = new ArrayList<ContactRequestDisplay>();
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
		removeRequest(username);
	}

	public void declineContactRequestButtonPress(String username) {
		declineContactRequestEvent.decline(username);
		removeRequest(username);
	}

	public void loadLater(HomeScreenInput input){
		Platform.runLater(new Runnable(){
			@Override public void run(){ loadNewUsers(input);}	});
	}
	
	public void removeRequest(String username) {
		
		for(ContactRequestDisplay requestDisplay : contactRequestDisplayList){
			if(requestDisplay.getName().equals(username)) {
				
				Platform.runLater(new Runnable(){
					@Override public void run(){ 				
						contactsContent.getChildren().remove(requestDisplay);
						contactRequestDisplayList.remove(requestDisplay);	}	});
			}
		}
	}
	
	/**
	 * Load new AppUsers not in appUserList
	 * @param input
	 */
	public void loadNewUsers(HomeScreenInput input) {
			
		// Get new requests
		LinkedList<AppJid> contactRequestList = input.getContactRequestList();
		for(AppJid appUser : contactRequestList) {
			if (!contactRequestDisplayList.isEmpty()) {
				for(ContactRequestDisplay requestDisplay : contactRequestDisplayList) {
					if(!requestDisplay.getName().equals(appUser.getLocal())) {
						ContactRequestDisplay request = 
								new ContactRequestDisplay(this, appUser.getLocal());
						contactsContent.getChildren().add(request);
						contactRequestDisplayList.add(request);
					}
				}
			} else {
				ContactRequestDisplay request = 
						new ContactRequestDisplay(this, appUser.getLocal());
				contactsContent.getChildren().add(request);
				contactRequestDisplayList.add(request);
			}
		
		}
		
			
		// Get new AppUsers
		LinkedList<AppUser> contactList = input.getContactList();
		for(AppUser appUser : contactList){
			if(!this.appUserList.contains(appUser)) {
				MUCContactDisplay contactDisplay = 
						new MUCContactDisplay(appUser,imageMessage, imageNewMessage,
								imageOnline, imageOffline);
				contactDisplay.setOnSelectAppUser(e-> {
						chatWithContactEvent.openChat(e.getJid().getLocal());
						setContactInFocus(appUser.getName());
				contactDisplay.setOnBlockAppUser(block->deleteUser(block));
				contactDisplay.setOnDeletetAppUser(delete->deleteUser(delete));
				});
				this.appUserList.add(appUser);
				displayList.add(contactDisplay);
				contactsContent.getChildren().add(contactDisplay);
			}
		}
	}
	
	/**
	 * Delete user from screen
	 * @param appUser
	 */
	private void deleteUser(AppUser appUser) {
		
		for(AppUser getUser : appUserList){
			if(getUser.getName().equals(appUser.getName()))  {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						appUserList.remove(getUser);}	});
			}
		}
				
		for(MUCContactDisplay display : displayList){
			if(display.getAppUser().getName().equals(appUser.getName())) {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						contactsContent.getChildren().remove(display);
						displayList.remove(display);}	});
			}
		}
	}
	
	/**
	 * Load new message notification of contact
	 * @param mucList
	 */
	public void loadMessageNotification(String appUser) {
		for(MUCContactDisplay display : displayList) {
			if(display.appUser.getName().equals(appUser)) {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						display.setNewMessageImage();}});
			}
		}
	}

	/**
	 * Load Contact online status
	 * @param mucList
	 */
	public void loadUserOnline(String appUser) {
		for(MUCContactDisplay display : displayList) {
			if(display.appUser.getName().equals(appUser)) {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						display.setOnline();}});					 
			}
		}
	}

	/**
	 * Load Contact offline status
	 * @param mucList
	 */
	public void loadUserOffline(String appUser) {
		for(MUCContactDisplay display : displayList) {
			if(display.appUser.getName().equals(appUser)) {
				Platform.runLater(new Runnable(){
					@Override public void run(){
						display.setOffline();}});					 
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
