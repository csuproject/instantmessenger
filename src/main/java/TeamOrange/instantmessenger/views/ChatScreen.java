package TeamOrange.instantmessenger.views;


import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatScreen extends Screen {

//	private Label header;
	private TextField newMessageTextField;
	private Button sendNewMessageButton;
	private SendNewMessageEvent sendNewMessageEvent;

	private ScrollPane scrollPane;
	private VBox scrollPaneContent;

	public ChatScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		scrollPaneContent = new VBox();
		scrollPaneContent.setPadding(new Insets(20,20,20,20));
		// vboc
		VBox vbox = new VBox();

		//scrollpane
		this.scrollPane = new ScrollPane();
		//this.scrollPane.setPadding(new Insets(20, 20, 20, 20));

		//send message
		newMessageTextField = new TextField();
		sendNewMessageButton = new Button("Send");
		sendNewMessageButton.setOnAction( e->sendNewMessageBtnPress() );
		sendNewMessageButton.setPrefWidth(200);
		newMessageTextField.setPrefWidth(640-sendNewMessageButton.getWidth());
		HBox newMessage = new HBox();
		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);

		scrollPane.setContent(scrollPaneContent);

		// add elements
		vbox.getChildren().addAll(scrollPane, newMessage);
		this.getChildren().add(vbox);
	}

	public void loadFromDifferentThread(ChatScreenInput input){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				load(input);
			}
		});
	}

	public void load(ChatScreenInput input){
		//header.setText("Chat with " + input.getPartner());
		// TODO: load messages
		String partner = input.getPartner();
		LinkedList<AppChatSessionMessage> messages = input.getMessages();
		scrollPaneContent.getChildren().clear();
		for(AppChatSessionMessage m : messages){
			String username = m.isInbound() ? partner : "Self";
			MessageDisplay md = new MessageDisplay(username, m.getBody());
			scrollPaneContent.getChildren().add(md);
		}
		// TODO: make a method to simply append a single message
	}

	public void sendNewMessageBtnPress(){
		String message = newMessageTextField.getText();
		sendNewMessageEvent.send(message);
	}

	public void setOnSendNewMessageEvent(SendNewMessageEvent sendNewMessageEvent){
		this.sendNewMessageEvent = sendNewMessageEvent;
	}
}
