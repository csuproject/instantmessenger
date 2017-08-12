package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;
import java.util.LinkedList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeScreen extends Screen {

	private TextField addContactWithUsernameInputTextField;
	private Button addContactButton;
	private ScrollPane contacts;
	private VBox contactsContent;
	private ChatWithContactEvent chatWithContactEvent;
	private AddContactEvent addContactEvent;
	private AcceptContactRequestEvent acceptContactRequestEvent;
	private DeclineContactRequestEvent declineContactRequestEvent;

	public HomeScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		// Build contacts
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
		HBox addContactInput = new HBox();
		addContactInput.getChildren().addAll(addContactWithUsername, addContactWithUsernameInputTextField, addContactButton);
		addContactInput.setSpacing(10);

		// Build main VBox
		VBox vbox = new VBox();
		vbox.getChildren().addAll(contacts, addContactInput);
		vbox.setVgrow(contacts, javafx.scene.layout.Priority.ALWAYS);
		this.getChildren().add(vbox);
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
			alert("Contact-add request sent to user( \""+username+"\")", "request sent", AlertType.CONFIRMATION);
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
				load(input);
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
