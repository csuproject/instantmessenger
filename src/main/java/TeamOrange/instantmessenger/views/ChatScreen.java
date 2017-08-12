package TeamOrange.instantmessenger.views;


import java.util.LinkedList;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucMessage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ChatScreen extends Screen {

//	private Label header;
	private TextField newMessageTextField;
	private Button sendNewMessageButton;
	private SendNewMessageEvent sendNewMessageEvent;
	private ChangeScreen changeScreen;
	private ScrollPane scrollPane;
	private VBox scrollPaneContent;
	private boolean MUCMODE;
	private Label chatNameLabel;
	private HBox newMessage;
	private VBox screenVBox;
	private HBox mucHbox;
	private HBox partnerDetails;
	private Label partnerName;
	GetMUCEvent sendMessage;
	GetMUCEvent destroyMUC;
	GetMUCEvent exitMUC;
	AppMuc muc;

	public ChatScreen(GuiBase guiBase){
		super(guiBase);
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
		screenVBox = new VBox();

		//scrollpane
		scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setMinHeight(500);
		this.setMaxHeight(500);

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
		sendNewMessageButton.setFocusTraversable(false);
		scrollPane.setContent(scrollPaneContent);
		sendNewMessageButton.setPrefWidth(50);
		newMessageTextField.setPrefWidth(350-sendNewMessageButton.getWidth());

		newMessage = new HBox();
		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);

		// MUC Control Hbox
		Button destroyButton = new Button("Back");
		destroyButton.setMinWidth(100);
		destroyButton.setOnAction(e->changeScreen.SetScreen(ScreenEnum.MUC));
		//destroyButton.setOnAction(e->clear());
		Button exitButton = new Button("Exit");
		exitButton.setMinWidth(100);
		exitButton.setOnAction(e->exitMUC());
		//exitButton.setOnAction(e->createMUC());
		chatNameLabel = new Label();
		chatNameLabel.setMinWidth(100);
		chatNameLabel.setAlignment(Pos.CENTER);
		mucHbox = new HBox(
				destroyButton,chatNameLabel,exitButton);
		mucHbox.setAlignment(Pos.CENTER);

		// partner detail
		partnerName = new Label("partnerName");
		partnerName.setFont(new Font(25));
		partnerDetails = new HBox(partnerName);
		partnerDetails.setMinHeight(50);
		partnerDetails.setMaxHeight(50);
		partnerDetails.setAlignment(Pos.CENTER);

		// add elements
		screenVBox.getChildren().addAll(partnerDetails, scrollPane, newMessage);
		this.getChildren().add(screenVBox);
	}

//	public void loadLater(AppMuc muc){
//		Platform.runLater(new Runnable(){
//			@Override public void run(){
//				load(muc);
//			}
//		});
//	}

	@Override
	public void load(ScreenInput input){
		ChatScreenInput chatScreenInput = (ChatScreenInput)input;
		if(chatScreenInput.isForMuc()){
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(mucHbox,scrollPane, newMessage);

			chatNameLabel.setText("Group " + muc.getRoomID());
			setMUCMode(true);
			setMUCInFocus(muc);
			LinkedList<AppMucMessage> messages = muc.getMessages();
			scrollPaneContent.getChildren().clear();
			for(AppMucMessage m : messages){
				String username = m.getFromNick();
				MessageDisplay md = new MessageDisplay(username, m.getBody());
				scrollPaneContent.getChildren().add(md);
			}
			scrollChat();
		}
		else if(chatScreenInput.isForChatSession()){
			setMUCMode(false);
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(partnerDetails, scrollPane, newMessage);

			String partner = chatScreenInput.getPartner();
			partnerName.setText(partner);
			LinkedList<AppChatSessionMessage> messages = chatScreenInput.getMessages();
			scrollPaneContent.getChildren().clear();
			for(AppChatSessionMessage m : messages){
				String username = m.isInbound() ? partner : "Self";
				MessageDisplay md = new MessageDisplay(username, m.getBody());
				scrollPaneContent.getChildren().add(md);
			}
			scrollChat();
		}

		// TODO: make a method to simply append a single message
	}

//	public void load(AppMuc muc) {
//		screenVBox.getChildren().clear();
//		screenVBox.getChildren().addAll(mucHbox,scrollPane, newMessage);
//
//		chatNameLabel.setText("Group " + muc.getRoomID());
//		setMUCMode(true);
//		setMUCInFocus(muc);
//		LinkedList<AppMucMessage> messages = muc.getMessages();
//		scrollPaneContent.getChildren().clear();
//		for(AppMucMessage m : messages){
//			String username = m.getFromNick();
//			MessageDisplay md = new MessageDisplay(username, m.getBody());
//			scrollPaneContent.getChildren().add(md);
//		}
//		scrollChat();
//	}

	public void loadMessages() {

	}

	private void setMUCInFocus(AppMuc muc) {
		this.muc = muc;
	}

	private AppMuc getMUCInFocus() {
		return this.muc;
	}

	private void setMUCMode(boolean mode) {
		MUCMODE = mode;
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
			if (!MUCMODE)
			{ sendNewMessageEvent.send(message);}
			else 	{ sendMUCMessage(message); }
		}
		newMessageTextField.clear();
	}

	public void sendMUCMessage(String message) {
		muc.sendMessage(message);
		load(new ChatScreenInput(muc)); // TODO: load should be called from muc, not here
	}

	public void destroyMUC() {

	}

	public void exitMUC() {
		changeScreen.SetScreen(ScreenEnum.MUC);
		exitMUC.getMUC(this.muc);
	}

	public void setOnSendNewMessageEvent(SendNewMessageEvent sendNewMessageEvent) {
		this.sendNewMessageEvent = sendNewMessageEvent;
	}

	public void setOnDestroyMUC(GetMUCEvent destroyMUC) {
		this.destroyMUC = destroyMUC;
	}

	public void setOnExitMUC(GetMUCEvent exitMUC) {
		this.exitMUC = exitMUC;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}
}