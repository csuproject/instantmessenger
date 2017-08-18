package TeamOrange.instantmessenger.views;

import java.net.URISyntaxException;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.SelectAppUser;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MUCContactDisplay extends HBox {

	private String username;
	private Label usernameLabel;
	private boolean selected;
	private SelectAppUser selectAppUser;
	private GetMUCEvent getMUCEvent;
	public AppUser appUser;
	public AppMuc appMUC;
	Image imageMessage;
	Image imageNewMessage;
		
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
	public MUCContactDisplay(AppUser appUser, Image imageMessage, Image imageNewMessage){
		this.imageMessage = imageMessage;
		this.imageNewMessage = imageNewMessage;
		this.appUser = appUser;
		this.username = appUser.getJid().getLocal();
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
			selectAppUser.getAppUser(appUser);
			usernameLabel.setGraphic(new ImageView(this.imageMessage));
			System.out.println("Clicked");});
		this.getChildren().addAll(usernameLabel);
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
			selectAppUser.getAppUser(appUser);
		} else {
			this.setStyle("-fx-padding: 5;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 3;" +
	                "-fx-border-radius: 5;" +
	                "-fx-border-color: red;");
			selected = true;
			System.out.println("Select");
			selectAppUser.getAppUser(appUser);
		}
	}
	
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
	
	public void setNewMessageImage() {
		usernameLabel.setGraphic(new ImageView(imageNewMessage));
	}
	
	public void setMessageImage() {
		usernameLabel.setGraphic(new ImageView(imageMessage));
	}
	
	public void setOnSelectAppUser(SelectAppUser selectAppUser) {
		this.selectAppUser = selectAppUser;
	}
	
	public void setOnGetMUCEvent (GetMUCEvent getMUCEvent) {
		this.getMUCEvent = getMUCEvent;
	}
}
