package TeamOrange.instantmessenger.views;


import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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

		// add fake data
		MessageDisplay m = new MessageDisplay("Tim", "Hello");
		scrollPaneContent.getChildren().add(m);
		scrollPaneContent.setPadding(new Insets(20,20,20,20));

		m = new MessageDisplay("Self", "Hey Mate");
		scrollPaneContent.getChildren().add(m);

		m = new MessageDisplay("Tim", "A Control that provides a scrolled, clipped viewport of its contents. It allows the user to scroll the content around either directly (panning) or by using scroll bars. The ScrollPane allows specification of the scroll bar policy, which determines when scroll bars are displayed: always, never, or only when they are needed. The scroll bar policy can be specified independently for the horizontal and vertical scroll bars.");
		scrollPaneContent.getChildren().add(m);

		m = new MessageDisplay("Self", "Wow");
		scrollPaneContent.getChildren().add(m);

		m = new MessageDisplay("Self", "That was a long message");
		scrollPaneContent.getChildren().add(m);

		scrollPane.setContent(scrollPaneContent);

		// add elements
		vbox.getChildren().addAll(scrollPane, newMessage);
		this.getChildren().add(vbox);





		// header
//		header = new Label("Chat with Unknown");
//		header.setFont(Font.font(40));
//
		// new message
//		newMessageTextField = new TextField();
//		sendNewMessageButton = new Button("Send");
//		sendNewMessageButton.setOnAction( e->sendNewMessageBtnPress() );
//		HBox newMessage = new HBox();
//		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);
//
//		GridPane gridPane = new GridPane();
//		gridPane.setVgap(20);
//		gridPane.add(header, 0, 0);
//		gridPane.add(newMessage, 0, 2);
//		gridPane.setAlignment(Pos.CENTER);



		//this.getChildren().add(gridPane);
	}

	public void load(ChatScreenInput input){
		//header.setText("Chat with " + input.getPartner());
		System.out.println("\n\nMessages: \n");
		input.printMessages();
		// TODO: load messages
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
