package TeamOrange.instantmessenger.views;


import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Set;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.SendMucMessageEvent;
import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import TeamOrange.instantmessenger.models.AppChatSessionMessage;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucMessage;
import TeamOrange.instantmessenger.models.AppUser;
import TeamOrange.instantmessenger.models.MUCChat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ChatScreen extends Screen {

	private TextField newMessageTextField;
	private Button sendNewMessageButton;
	private SendNewMessageEvent sendNewMessageEvent;
	private ChangeScreen changeScreen;
	private ScrollPane scrollPane;
	private VBox scrollPaneContent;
	private ScrollPane mucOccupantsPane;
	private VBox mucOccupantsPaneContent;
	private boolean MUCMODE;
	private Label chatNameLabel;
	private HBox newMessage;
	private VBox screenVBox;
	private HBox mucHbox;
	private HBox partnerDetails;
	private Label partnerName;
	private Button backButton;
	private GetMUCEvent sendMessage;
	private GetMUCEvent destroyMUC;
	private GetMUCEvent exitMUC;
	private AppMuc muc;
	private String userName;
	private SendMucMessageEvent sendMucMessageEvent;

	public ChatScreen(GuiBase guiBase){
		super(guiBase);
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {

		//////////////////////////////////////////////////////////////////////////////
		//------------------------------Chat Display--------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		scrollPaneContent = new VBox();
		scrollPaneContent.setPadding(new Insets(20,20,20,20));
		scrollPane = new ScrollPane();
		scrollPane.setMinHeight(480);
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setFocusTraversable(false);
		scrollPane.setOnMouseClicked((e)->newMessageTextField.requestFocus());
		scrollPane.setStyle("-fx-focus-color: transparent;");

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------MUC Occupant List Display-----------------------//
		//////////////////////////////////////////////////////////////////////////////
		mucOccupantsPane = new ScrollPane();
		mucOccupantsPaneContent = new VBox();
		mucOccupantsPaneContent.setPrefHeight(400);
		mucOccupantsPaneContent.setPrefWidth(400);
		mucOccupantsPane.setContent(mucOccupantsPaneContent);
		mucOccupantsPane.setMaxHeight(200);
		mucOccupantsPane.setMinHeight(200);
		mucOccupantsPane.setFitToWidth(true);
		mucOccupantsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		mucOccupantsPane.setHbarPolicy(ScrollBarPolicy.NEVER);

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------MUC Control Display-----------------------------//
		//////////////////////////////////////////////////////////////////////////////
		chatNameLabel = new Label();
		chatNameLabel.setMinWidth(100);
		chatNameLabel.setMinHeight(25);
		chatNameLabel.setMaxHeight(25);
		chatNameLabel.setAlignment(Pos.CENTER);
		Button destroyButton = new Button("Back");
		destroyButton.setMinWidth(100);
		destroyButton.setOnAction(e->changeScreen.SetScreen(ScreenEnum.MUC));
		destroyButton.setFocusTraversable(false);
		mucHbox = new HBox(
				destroyButton,chatNameLabel);
		mucHbox.setAlignment(Pos.CENTER);

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------Send Message Display----------------------------//
		//////////////////////////////////////////////////////////////////////////////
		newMessageTextField = new TextField();
		newMessageTextField.setOnKeyTyped(e-> { // Send message on Enter Key
			this.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			      if(key.getCode()==KeyCode.ENTER) {sendNewMessageBtnPress();}	});	});
		sendNewMessageButton = new Button("Send");
		sendNewMessageButton.setOnAction( e->sendNewMessageBtnPress() );
		sendNewMessageButton.setFocusTraversable(false);
		scrollPane.setContent(scrollPaneContent);
		sendNewMessageButton.setPrefWidth(50);
		newMessageTextField.setPrefWidth(350-sendNewMessageButton.getWidth());
		newMessage = new HBox();
		newMessage.getChildren().addAll(newMessageTextField, sendNewMessageButton);

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------Partner Detail Display--------------------------//
		//////////////////////////////////////////////////////////////////////////////
		partnerName = new Label();
		partnerName.setFont(new Font(25));
		backButton = new Button("Back");
		backButton.setOnAction(e->exitChatSession(e));
		backButton.setFocusTraversable(false);
		backButton.setFont(new Font(15));
		partnerDetails = new HBox(backButton, partnerName);
		partnerDetails.setMinHeight(25);
		partnerDetails.setMaxHeight(25);
		partnerDetails.setAlignment(Pos.CENTER);
		partnerDetails.setSpacing(50);

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------------Screen------------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		screenVBox = new VBox();
		screenVBox.getChildren().addAll(partnerDetails, scrollPane, newMessage);
		this.getChildren().add(screenVBox);
		this.setMaxHeight(500);
	}

	@Override
	public void load(ScreenInput input){

		ChatScreenInput chatScreenInput = (ChatScreenInput)input;

		//////////////////////////////////////////////////////////////////////////////
		//--------------------------MUC Session-------------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		if(chatScreenInput.isForMuc()){
			setMUCMode(true);
			AppMuc muc = chatScreenInput.getMuc();
			setMUCInFocus(muc);
			LinkedList<AppMucMessage> messages = muc.getMessages();
			scrollPaneContent.getChildren().clear();
			for(AppMucMessage m : messages){
				String username = m.getFromNick();
				MessageDisplay md = new MessageDisplay(
						username, m.getBody(), m.getSent(), m.getSentFromSelf());
				scrollPaneContent.getChildren().add(md);
			}
			scrollChat();
			newMessageTextField.requestFocus();
			chatNameLabel.setText("Group " + muc.getRoomID());
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(mucHbox,scrollPane, newMessage);
		}
		//////////////////////////////////////////////////////////////////////////////
		//--------------------------Chat Session------------------------------------//
		//////////////////////////////////////////////////////////////////////////////
		else if(chatScreenInput.isForChatSession()){
			setMUCMode(false);
			boolean partnerOnline = chatScreenInput.getPartnerOnline();
			userName = chatScreenInput.getPartner();
			partnerName.setText(userName);
			if(partnerOnline){
				partnerName.setTextFill(Color.web("#00ff00"));
			} else {
				partnerName.setTextFill(Color.web("#ff0000"));
			}
			LinkedList<AppChatSessionMessage> messages = chatScreenInput.getMessages();
			scrollPaneContent.getChildren().clear();
			for(AppChatSessionMessage m : messages){
				String username = m.isInbound() ? userName : "Self";
				MessageDisplay md = new MessageDisplay(username, m.getBody(), m.sent(), username.equals("Self"));
				scrollPaneContent.getChildren().add(md);
			}
			scrollChat();
			newMessageTextField.requestFocus();
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(partnerDetails, scrollPane, newMessage);
		}
	}

	private void setMUCInFocus(AppMuc muc) {
		this.muc = muc;
	}

	public AppMuc getMUCInFocus() {
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
			if (!MUCMODE){
				sendNewMessageEvent.send(message, userName);
			}
			else 	{ sendMUCMessage(message); }
		}
		newMessageTextField.clear();
	}

	public void sendMUCMessage(String message) {
		sendMucMessageEvent.send(muc, message);
//		muc.sendMessage(message);
//		load(new ChatScreenInput(muc)); // TODO: load should be called from muc, not here
	}

	/**
	 * Exit the MUC
	 */
	public void exitMUC() {
		changeScreen.SetScreen(ScreenEnum.MUC);
		exitMUC.getMUC(this.muc);
	}

	/**
	 * Set the MUC Occupant List
	 */
	public void setMUCOccupants(LinkedList<AppUser> occupantList) {
		mucOccupantsPaneContent.getChildren().clear();
		for(AppUser appUser : occupantList){
			mucOccupantsPaneContent.getChildren().add(new MUCContactDisplay(appUser));
		}
		screenVBox.getChildren().clear();
		screenVBox.getChildren().addAll(mucOccupantsPane,mucHbox,scrollPane, newMessage);
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

	public void setOnSendMucMessageEvent(SendMucMessageEvent sendMucMessageEvent){
		this.sendMucMessageEvent = sendMucMessageEvent;
	}

	public void exitChatSession(ActionEvent actionEvent){
		changeScreen.SetScreen(ScreenEnum.HOME);
	}
}