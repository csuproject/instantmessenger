package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.ChatWithUserNameEvent;
import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.models.AppJid;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class HomeScreen extends Screen {

	private Label header;
	private TextField chatWithUserNameInputTextField;
	private Button chatWithButton;

	private ChatWithUserNameEvent chatWithUserNameEvent;

	public HomeScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {

		// header
		header = new Label("Hello, Unknown");
		header.setFont(Font.font(60));

		Label chatWithUserNameInputLabel = new Label("Chat With: ");
		chatWithUserNameInputTextField = new TextField();
		chatWithButton = new Button("Start Chat");
		chatWithButton.setOnAction( e->chatWithBtnPress() );
		HBox chatWithUserNameInput = new HBox();
		chatWithUserNameInput.getChildren().addAll(chatWithUserNameInputLabel, chatWithUserNameInputTextField, chatWithButton);
		chatWithUserNameInput.setSpacing(10);

		//Pane
		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.add(header, 0, 0);
		gridPane.add(chatWithUserNameInput, 0, 1);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}

	public void load(HomeScreenInput input){
		header.setText("Hello, " + input.getName());
	}

	public void chatWithBtnPress(){
		String userName = chatWithUserNameInputTextField.getText();
		chatWithUserNameEvent.chatWithUserName(userName);
	}

	public void setOnChatWithUserNameEvent(ChatWithUserNameEvent chatWithUserNameEvent){
		this.chatWithUserNameEvent = chatWithUserNameEvent;
	}

}
