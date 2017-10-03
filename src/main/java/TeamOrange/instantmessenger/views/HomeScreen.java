package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.AcceptContactRequestEvent;
import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresence;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.application.Platform;
import javafx.geometry.Insets;
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

/**
 * This is the homescreen.
 * It is the screen the user is taken to once they login.
 * Within the app it is called the Contacts screen.
 * It contains contacts, contact reqeusts, and chat sessions with contacts.
 *
 */
public class HomeScreen extends Screen {

	private TextField addContactWithUsernameInputTextField;
	private Button addContactButton;
	private ScrollPane contacts;
	private VBox contactsContent,mainVbox;
	private HBox addContactInput;
	private ChatWithContactEvent chatWithContactEvent;
	private AddContactEvent addContactEvent, deleteContactEvent, blockContactEvent;
	private AcceptContactRequestEvent acceptContactRequestEvent;
	private DeclineContactRequestEvent declineContactRequestEvent;
	private List<MUCContactDisplay> displayList;
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

	/**
	 * Creates the screen.
	 * @throws Exception
	 */
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
		contactsContent.setPrefWidth(680);
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
		addContactWithUsername.setPadding(new Insets(0, 0, 0, 20));
		addContactWithUsernameInputTextField = new TextField();
		addContactWithUsernameInputTextField.setOnKeyPressed(
				keyEvent->addContactInputKeyPressed(keyEvent));
		// Restrict input to lower case
		addContactWithUsernameInputTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
		addContactWithUsernameInputTextField.setPrefWidth(450);
		addContactButton = new Button("Add");
		addContactButton.setOnAction( e->addContactBtnPress() );
		addContactButton.setFocusTraversable(false);
		addContactInput = new HBox();
		addContactInput.getChildren().addAll(addContactWithUsername, addContactWithUsernameInputTextField, addContactButton);
		addContactInput.setSpacing(10);

		//////////////////////////////////////////////////////////////////////////////
		//----------------------------------Screen----------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		displayList = new ArrayList<MUCContactDisplay>();
		mainVbox = new VBox();
		mainVbox.getChildren().addAll(contacts, addContactInput);
		mainVbox.setVgrow(contacts, javafx.scene.layout.Priority.ALWAYS);
		this.getChildren().add(mainVbox);
		this.setPrefHeight(600-50);
	}

	public void loadLater(HomeScreenInput input){
		Platform.runLater(new Runnable(){
			@Override public void run(){ load(input);}	});
	}

	/**
	 * Loads the screen, updating its contents based on the input.
	 * @param input the input to load the screen based on
	 */
	public void load(HomeScreenInput input){
		contactsContent.getChildren().clear();
		displayList.clear();

		LinkedList<AppJid> contactRequestList = input.getContactRequestList();
		for(AppJid jid : contactRequestList){
			ContactRequestDisplay request = new ContactRequestDisplay(this, jid.getLocal());
			contactsContent.getChildren().add(request);
		}

		LinkedList<AppUser> contactList = input.getContactList();
		for(AppUser user : contactList){

			MUCContactDisplay contactDisplay =
					new MUCContactDisplay(user,imageMessage, imageNewMessage,
							imageOnline, imageOffline);
			contactDisplay.setOnSelectAppUser(e-> {
					chatWithContactEvent.openChat(e.getJid().getLocal());
					setContactInFocus(user.getName());
			});
			contactDisplay.setOnBlockAppUser( userName->blockContactEvent.add(userName) );
			contactDisplay.setOnDeletetAppUser( userName->deleteContactEvent.add(userName) );

			// Set Notification
			if (user.getNotification())
				contactDisplay.setNewMessageImage();
			else
				contactDisplay.setMessageImage();
			// Set Presence
			AppPresence presence = user.getPresence();
			if (AppPresence.Type.AVAILIBLE == presence.getType())
				contactDisplay.setOnline();
			else
				contactDisplay.setOffline();
			// Add to HomeScreen List
			displayList.add(contactDisplay);
			contactsContent.getChildren().add(contactDisplay);
		}
	}

	/**
	 * This is called when a key is pressed while the add contact text field is focused (which is always).
	 * If the button is enter, it sends the add contact event.
	 * @param keyEvent
	 */
	public void addContactInputKeyPressed(javafx.scene.input.KeyEvent keyEvent){
		if( keyEvent.getCode().equals(javafx.scene.input.KeyCode.ENTER) ){
			addContactBtnPress();
		}
	}

	/**
	 * This is called when a contact is clicked on, in order to chat with them.
	 * Sends a chat with contact event.
	 * @param username the username of the  contact to chat with
	 */
	public void chatButtonPress(String username) {
		chatWithContactEvent.openChat(username);
	}

	/**
	 * This is called when the add contact button is pressed.
	 * Sends an add contact event, if the input is valid.
	 */
	public void addContactBtnPress(){
		String username = addContactWithUsernameInputTextField.getText().trim();
		addContactWithUsernameInputTextField.clear();
		if(username.isEmpty()){
			alert("Username field must not be empty.", "empty username field", AlertType.WARNING);
		} else {
			this.addContactEvent.add(username);
		}
	}

	/**
	 * This is called when the accept contact request button is pressed.
	 * Sends a accept contact request event.
	 * @param username the username of the contact whos contact request is being accepted.
	 */
	public void acceptContactRequestButtonPress(String username) {
		acceptContactRequestEvent.accept(username);
	}

	/**
	 * This is called when the decline contact request button is pressed.
	 * Sends a decline contact request event.
	 * @param username the username of the contact whos contact request is being declined.
	 */
	public void declineContactRequestButtonPress(String username) {
		declineContactRequestEvent.decline(username);
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

	public void setOnDeleteContactEvent(AddContactEvent deleteContactEvent){
		this.deleteContactEvent = deleteContactEvent;
	}

	public void setOnBlockContactEvent(AddContactEvent blockContactEvent){
		this.blockContactEvent = blockContactEvent;
	}

}
