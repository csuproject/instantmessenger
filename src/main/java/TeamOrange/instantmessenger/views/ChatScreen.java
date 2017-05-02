package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ChatScreen extends Screen {

	private Label header;
	private TextField newMessageTextField;
	private Button sendNewMessageButton;
	private SendNewMessageEvent sendNewMessageEvent;


	public ChatScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		// header
		header = new Label("Chat with Unknown");
		header.setFont(Font.font(40));

		// new message
		newMessageTextField = new TextField();
		sendNewMessageButton = new Button("Send");
		sendNewMessageButton.setOnAction( e->sendNewMessageBtnPress() );
		HBox newMessage = new HBox();
		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);

		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.add(header, 0, 0);
		gridPane.add(newMessage, 0, 2);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}

	public void load(ChatScreenInput input){
		header.setText("Chat with " + input.getPartner());
		System.out.println("\n\nMessages: \n");
		input.printMessages();
	}

	public void sendNewMessageBtnPress(){
		String message = newMessageTextField.getText();
		sendNewMessageEvent.send(message);
	}

	public void setOnSendNewMessageEvent(SendNewMessageEvent sendNewMessageEvent){
		this.sendNewMessageEvent = sendNewMessageEvent;
	}
}
