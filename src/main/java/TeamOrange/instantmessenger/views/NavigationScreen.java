package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.application.Platform;
import TeamOrange.instantmessenger.lambda.LogoutEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * This is the navigation screen. It is always present at the bottom of the screen when the user is logged in.
 * It allows the user to navigate to the Contacts screen, the Groups screen, or to logout.
 * It also alows the user to know which section they are in, as the button for that section looks selected.
 *
 */
public class NavigationScreen extends Screen {

	HBox navigationBox;
	CustomButton contactButton;
	CustomButton chatButton;
	CustomButton logoutButton;
	ChangeScreen changeScreen;
	Image imageMessage;
	Image imageNewMessage;
	LogoutEvent logoutEvent;

	public NavigationScreen(GuiBase guiBase){
		super(guiBase);
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the screen.
	 * @throws Exception
	 */
	public void create() throws Exception {

		navigationBox = new HBox();
		navigationBox.setAlignment(Pos.CENTER);
		imageMessage = new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false);
		imageNewMessage = new Image(getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false);
		Image imageLogout = new Image(getClass().getResource(
				"/resources/logout-icon.png").toURI().toString(),25,25,false,false);
		contactButton = new CustomButton("Contacts");

		contactButton.setGraphic(new ImageView(imageMessage));
		contactButton.setMinSize(135, 60);
		contactButton.setOnAction(e-> {
			this.changeScreen.SetScreen(ScreenEnum.HOME);
			setImageContactMessage();
		});
		contactButton.setFocusTraversable(false);
		chatButton = new CustomButton("Groups");
		chatButton.setMinSize(135, 60);
		chatButton.setGraphic(new ImageView(imageMessage));
		chatButton.setOnAction(e->{
			this.changeScreen.SetScreen(ScreenEnum.MUC);
			setImageGroupMessage();
		});
		chatButton.setFocusTraversable(false);
		logoutButton = new CustomButton("Logout");
		logoutButton.setGraphic(new ImageView(imageLogout));
		logoutButton.setMinSize(135, 60);
		logoutButton.setFocusTraversable(false);
		logoutButton.setOnAction(e->{
			logoutButtonPress();
		});

		contactButton.setPrefWidth(141.0);
		contactButton.setPrefHeight(66.0);
		chatButton.setPrefWidth(141.0);
		chatButton.setPrefHeight(66.0);
		logoutButton.setPrefWidth(141.0);
		logoutButton.setPrefHeight(66.0);

		navigationBox.getChildren().addAll(contactButton, chatButton, logoutButton);
		this.getChildren().addAll(navigationBox);
	}

	/**
	 * Updates the image on the Contacts button to reflect that there is a new message in the Contacts screen.
	 */
	public void setImageNewContactMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				contactButton.setGraphic(new ImageView(imageNewMessage));}});
	}

	/**
	 * Updates the image on the Groups button to reflect that there is a new message in the Groups screen.
	 */
	public void setImageNewGroupMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				chatButton.setGraphic(new ImageView(imageNewMessage));}});
	}

	/**
	 * Updates the image on the Contacts button to reflect that there is NOT a new message in the Contacts screen.
	 */
	public void setImageContactMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				contactButton.setGraphic(new ImageView(imageMessage));}});
	}

	/**
	 * Updates the image on the Groups button to reflect that there is NOT a new message in the Groups screen.
	 */
	public void setImageGroupMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				chatButton.setGraphic(new ImageView(imageMessage));}});
	}

	/**
	 * This is called when the logout button is pressed.
	 * Displays an alert asking the user if they are sure,
	 * if they are then it logs them out by sending the logout event.
	 */
	public void logoutButtonPress(){
		boolean contactsButtonSelected;
		if(contactButton.isSelected()){
			contactsButtonSelected = true;
			contactButton.unSelect();
		} else {
			contactsButtonSelected = false;
			chatButton.unSelect();
		}
		logoutButton.select();
		setImageContactMessage();
		setImageGroupMessage();
		boolean answer = alertBoolean("Are you sure you would like to log out?", "Are You Sure?");
		if(answer){
			logoutEvent.logout();
		} else {
			logoutButton.unSelect();
			if(contactsButtonSelected){
				contactButton.select();
			} else {
				chatButton.select();
			}
		}
	}

	/**
	 * This is called when the logout button is pressed.
	 * Sends the logout event.
	 */
	public void logout(){
		logoutEvent.logout();
	}

	public void setOnLogoutEvent(LogoutEvent logoutEvent){
		this.logoutEvent = logoutEvent;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

	public void setSelectedToContacts(){
		contactButton.select();
		chatButton.unSelect();
		logoutButton.unSelect();
	}

	public void setSelectedToGroups(){
		chatButton.select();
		contactButton.unSelect();
		logoutButton.unSelect();
	}

	public void setSelectedToLogout(){
		logoutButton.select();
		contactButton.unSelect();
		chatButton.unSelect();
	}

}
