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
		addContactWithUsernameInputTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
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
			contactDisplay.setOnBlockAppUser(block->blockContactEvent.add(block));
			contactDisplay.setOnDeletetAppUser(delete->deleteContactEvent.add(delete));

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
	}

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

	public void alert(String message, String title, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
