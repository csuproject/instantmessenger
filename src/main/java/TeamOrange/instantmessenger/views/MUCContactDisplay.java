package TeamOrange.instantmessenger.views;

import java.net.URISyntaxException;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.SelectAppUser;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MUCContactDisplay extends HBox {

	private String username;
	private Label usernameLabel;
	private Label onlineLabel, offlineLabel;
	private boolean selected;
	private SelectAppUser openAppUser;
	private SelectAppUser deleteAppUser;
	private SelectAppUser blockAppUser;
	private GetMUCEvent getMUCEvent;
	private Button deleteButton, blockButton;
	public AppUser appUser;
	public AppMuc appMUC;
	private Image imageMessage, imageNewMessage,
	imageOnline, imageOffline;

	/**
	 * Basic Contact Display of AppUser
	 * @param appUser
	 */
	public MUCContactDisplay(AppUser appUser) {
		this.username = appUser.getJid().getLocal();
		this.appUser = appUser;
		usernameLabel = new Label(username);

		//usernameLabel.setGraphic(new ImageView(imageMessage));
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
		this.setMaxWidth(380);
		this.setOnMouseClicked(e->select());
		this.getChildren().addAll(usernameLabel);
	}

	/**
	 * Contact Display with message notification
	 * @param appUser
	 * @param imageMessage
	 * @param imageNewMessage
	 */
	public MUCContactDisplay(AppUser appUser, Image imageMessage, Image imageNewMessage,
			Image imageOnline, Image imageOffline){
		this.appUser = appUser;
		this.username = appUser.getJid().getLocal();
		this.imageMessage = imageMessage;
		this.imageNewMessage = imageNewMessage;
		this.imageOnline = imageOnline;
		this.imageOffline = imageOffline;
		usernameLabel = new Label(username);
		usernameLabel.setGraphic(new ImageView(imageNewMessage));
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		HBox usernameHBox = new HBox(usernameLabel);
		usernameHBox.setAlignment(Pos.CENTER);
		onlineLabel = new Label("Offline");
		onlineLabel.setGraphic(new ImageView(imageOffline));
		onlineLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		HBox onlineHBox = new HBox(onlineLabel);
		onlineHBox.setAlignment(Pos.CENTER);
		deleteButton = new Button("Delete");
		deleteButton.setOnAction(e->deleteAppUser.getAppUser(appUser));
		deleteButton.setMinHeight(35);
		deleteButton.setFocusTraversable(false);
		blockButton = new Button("Block");
		blockButton.setOnAction(e->blockAppUser.getAppUser(appUser));
		blockButton.setMinHeight(35);
		blockButton.setFocusTraversable(false);
		HBox buttonHBox = new HBox(deleteButton,blockButton);
		buttonHBox.setAlignment(Pos.CENTER);

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
		this.setMaxWidth(380);
		this.setOnMouseClicked(e-> {
			openAppUser.getAppUser(appUser);
			usernameLabel.setGraphic(new ImageView(imageMessage));
			System.out.println("Clicked");});
		HBox mainHBox = new HBox(onlineHBox, usernameHBox, buttonHBox);
		mainHBox.setSpacing(30);
		mainHBox.setAlignment(Pos.CENTER);
		this.getChildren().addAll(mainHBox);
	}

	/**
	 * Highlight selected contact
	 */
	public void select() {
		if (selected) {
			this.setStyle("-fx-padding: 5;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 3;" +
	                "-fx-border-radius: 5;" +
	                "-fx-border-color: black;");
			selected = false;
			System.out.println("Unselect");
			openAppUser.getAppUser(appUser);
		} else {
			this.setStyle("-fx-padding: 5;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 3;" +
	                "-fx-border-radius: 5;" +
	                "-fx-border-color: red;");
			selected = true;
			System.out.println("Select");
			openAppUser.getAppUser(appUser);
		}
	}

	// TODO: Constructor for MUCS ??
	public MUCContactDisplay(AppMuc appMUC, Image imageMessage, Image imageNewMessage){
		this.imageMessage = imageMessage;
		this.imageNewMessage = imageNewMessage;
		this.appMUC = appMUC;
		this.username = appMUC.getRoomID();
		usernameLabel = new Label(username);
		usernameLabel.setGraphic(new ImageView(imageNewMessage));
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
		this.setMaxWidth(380);
		this.setOnMouseClicked(e-> {
			usernameLabel.setGraphic(new ImageView(this.imageMessage));
			getMUCEvent.getMUC(this.appMUC);});

		this.getChildren().addAll(usernameLabel);
	}

	/**
	 * Set new message Image
	 */
	public void setNewMessageImage() {
		usernameLabel.setGraphic(new ImageView(imageNewMessage));
	}

	/**
	 * Set no new message icon
	 */
	public void setMessageImage() {
		usernameLabel.setGraphic(new ImageView(imageMessage));
	}

	/**
	 * Set user online image
	 */
	public void setOnline() {
		onlineLabel.setGraphic(new ImageView(imageOnline));
		onlineLabel.setText("Online");
	}

	/**
	 * Set user offline image
	 */
	public void setOffline() {
		onlineLabel.setGraphic(new ImageView(imageOffline));
		onlineLabel.setText("Offline");
	}

	/**
	 * Set on SelectAppUser Event
	 * @param selectAppUser
	 */
	public void setOnSelectAppUser(SelectAppUser selectAppUser) {
		this.openAppUser = selectAppUser;
	}

	public void setOnBlockAppUser(SelectAppUser selectAppUser) {
		this.blockAppUser = selectAppUser;
	}

	public void setOnDeletetAppUser(SelectAppUser selectAppUser) {
		this.deleteAppUser = selectAppUser;
	}

	/**
	 * Set on GetMUCEvent Event
	 * @param getMUCEvent
	 */
	public void setOnGetMUCEvent (GetMUCEvent getMUCEvent) {
		this.getMUCEvent = getMUCEvent;
	}

	public AppUser getAppUser() {
		return this.appUser;
	}
}
