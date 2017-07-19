package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.CreateChatWithUserNameEvent;
import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;
import java.util.LinkedList;
import TeamOrange.instantmessenger.lambda.AcceptContactRequestEvent;
import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

public class HomeScreen extends Screen {

	private TextField addContactWithUsernameInputTextField;
	private Button addContactButton;
	//chat list
	private ScrollPane contacts;
	private VBox contactsContent;

	//private CreateChatWithUserNameEvent chatWithUserNameEvent;
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

		VBox vbox = new VBox();

		//contacts
		contacts = new ScrollPane();
//		contacts.setPrefHeight(400);
//		contacts.setPrefWidth(400);
		contactsContent = new VBox();
		contactsContent.setPrefHeight(400);
		contactsContent.setPrefWidth(400);
		contacts.setContent(contactsContent);

		// add contact
		Label addContactWithUsername = new Label("Add Contact Via Username: ");
		addContactWithUsernameInputTextField = new TextField();
		addContactButton = new Button("Add");
		addContactButton.setOnAction( e->addContactBtnPress() );
		HBox addContactInput = new HBox();
		addContactInput.getChildren().addAll(addContactWithUsername, addContactWithUsernameInputTextField, addContactButton);
		addContactInput.setSpacing(10);

		// add fake data
		ContactDisplay c = new ContactDisplay(this, "tim");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "shaun");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "murray");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "jim");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "test");
		contactsContent.getChildren().add(c);


		vbox.getChildren().addAll(contacts, addContactInput);
		this.getChildren().add(vbox);
	}

	public void chatButtonPress(String username) {
		System.out.println("Chat with " + username);
		chatWithContactEvent.openChat(username);
	}

	public void addContactBtnPress(){
		String username = addContactWithUsernameInputTextField.getText().trim();
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

//	public void chatWithBtnPress(){
//		String userName = chatWithUserNameInputTextField.getText();
//		chatWithUserNameEvent.chatWithUserName(userName);
//	}
//
//	public void setOnChatWithUserNameEvent(CreateChatWithUserNameEvent chatWithUserNameEvent){
//		this.chatWithUserNameEvent = chatWithUserNameEvent;
//	}

}
