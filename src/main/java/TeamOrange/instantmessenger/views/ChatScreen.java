package TeamOrange.instantmessenger.views;


import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
		scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setMinHeight(550);
		this.setMaxHeight(550);
	
		//this.scrollPane.setPadding(new Insets(20, 20, 20, 20));

		// Send message
		newMessageTextField = new TextField();
		// Send message on Enter Key
		newMessageTextField.setOnKeyTyped(e-> {
			this.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			      if(key.getCode()==KeyCode.ENTER) {
			    	  sendNewMessageBtnPress();
			      }
			});
		});
		
		sendNewMessageButton = new Button("Send");
		sendNewMessageButton.setOnAction( e->sendNewMessageBtnPress() );		
		scrollPane.setContent(scrollPaneContent);
		sendNewMessageButton.setPrefWidth(50);
		newMessageTextField.setPrefWidth(350-sendNewMessageButton.getWidth());

		HBox newMessage = new HBox();
		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);


		// add elements
		vbox.getChildren().addAll(scrollPane, newMessage);
		this.getChildren().add(vbox);
	}

	public void loadLater(ChatScreenInput input){
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
		scrollChat();

		// TODO: make a method to simply append a single message
	}
	
	/**
	 * Scroll chat pane to bottom.
	 */
	public void scrollChat() {
		    Animation animation = new Timeline(
		        new KeyFrame(Duration.seconds(2),
		            new KeyValue(scrollPane.vvalueProperty(), 1)));
		    animation.play();
		
		scrollPane.setVvalue(1);
	}

	public void sendNewMessageBtnPress(){
		String message = newMessageTextField.getText();
		if (!newMessageTextField.getText().isEmpty()) { 
			sendNewMessageEvent.send(message);
			newMessageTextField.clear();
		}
	}

	public void setOnSendNewMessageEvent(SendNewMessageEvent sendNewMessageEvent){
		this.sendNewMessageEvent = sendNewMessageEvent;
	}
}
