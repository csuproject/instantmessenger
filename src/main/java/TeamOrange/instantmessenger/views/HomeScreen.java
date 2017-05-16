package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.CreateChatWithUserNameEvent;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.models.AppChatSession;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

public class HomeScreen extends Screen {

	private Label header;
	private TextField chatWithUserNameInputTextField;
	private Button chatWithButton;
	//chat list
	private ScrollPane contacts;
	private VBox contactsContent;

	//private CreateChatWithUserNameEvent chatWithUserNameEvent;
	private ChatWithContactEvent chatWithContactEvent;

	public HomeScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {

		//contacts
		contacts = new ScrollPane();
//		contacts.setPrefHeight(400);
//		contacts.setPrefWidth(400);
		contactsContent = new VBox();
		contactsContent.setPrefHeight(400);
		contactsContent.setPrefWidth(400);
		contacts.setContent(contactsContent);

		// add fake data
		ContactDisplay c = new ContactDisplay(this, "Tim");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "Shaun");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "Murray");
		contactsContent.getChildren().add(c);
		c = new ContactDisplay(this, "Jim");
		contactsContent.getChildren().add(c);



		// header
//		header = new Label("Hello, Unknown");
//		header.setFont(Font.font(60));
//
//		// create chat via username
//		Label chatWithUserNameInputLabel = new Label("Chat With: ");
//		chatWithUserNameInputTextField = new TextField();
//		chatWithButton = new Button("Start Chat");
//		chatWithButton.setOnAction( e->chatWithBtnPress() );
//		HBox chatWithUserNameInput = new HBox();
//		chatWithUserNameInput.getChildren().addAll(chatWithUserNameInputLabel, chatWithUserNameInputTextField, chatWithButton);
//		chatWithUserNameInput.setSpacing(10);

		//Pane
//		GridPane gridPane = new GridPane();
//		gridPane.setVgap(20);
//		gridPane.add(header, 0, 0);
//		gridPane.add(chatWithUserNameInput, 0, 2);
//		gridPane.setAlignment(Pos.CENTER);
//
//		this.getChildren().add(gridPane);


		this.getChildren().add(contacts);
	}

	public void chatButtonPress(String username) {
		System.out.println("Chat with " + username);
		chatWithContactEvent.openChat(username);
	}

	public void load(HomeScreenInput input){
		//header.setText("Hello, " + input.getName());
	}

	public void setOnChatWithContactEvent(ChatWithContactEvent chatWithContactEvent){
		this.chatWithContactEvent = chatWithContactEvent;
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
