package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.application.Platform;
import TeamOrange.instantmessenger.lambda.LogoutEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NavigationScreen extends Screen {

	HBox navigationBox;
	Button contactButton;
	Button chatButton;
	Button logoutButton;
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

	public void create() throws Exception {

		navigationBox = new HBox();
		navigationBox.setAlignment(Pos.CENTER);
		imageMessage = new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false);
		imageNewMessage = new Image(getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false);
		Image imageLogout = new Image(getClass().getResource(
				"/resources/logout-icon.png").toURI().toString(),25,25,false,false);
		contactButton = new Button("Chats");

		contactButton.setGraphic(new ImageView(imageMessage));
		contactButton.setMinSize(135, 60);
		contactButton.setOnAction(e-> {
			this.changeScreen.SetScreen(ScreenEnum.HOME);
			setImageContactMessage();
		});
		chatButton = new Button("Groups");
		chatButton.setMinSize(135, 60);
		chatButton.setGraphic(new ImageView(imageMessage));
		chatButton.setOnAction(e->{
			this.changeScreen.SetScreen(ScreenEnum.MUC);
			setImageGroupMessage();
		});
		logoutButton = new Button("Logout");
		logoutButton.setGraphic(new ImageView(imageLogout));
		logoutButton.setMinSize(135, 60);
		logoutButton.setOnAction(e->{
			setImageContactMessage();
			setImageGroupMessage();
			logoutEvent.logout();
		});

		navigationBox.getChildren().addAll(contactButton, chatButton, logoutButton);
		this.getChildren().addAll(navigationBox);
	}

	public void setImageNewContactMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				contactButton.setGraphic(new ImageView(imageNewMessage));}});
	}

	public void setImageNewGroupMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				chatButton.setGraphic(new ImageView(imageNewMessage));}});
	}

	public void setImageContactMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				contactButton.setGraphic(new ImageView(imageMessage));}});
	}

	public void setImageGroupMessage() {
		Platform.runLater(new Runnable(){
			@Override public void run(){
				chatButton.setGraphic(new ImageView(imageMessage));}});
	}

	public void logout(){
		logoutEvent.logout();
	}

	public void setOnLogoutEvent(LogoutEvent logoutEvent){
		this.logoutEvent = logoutEvent;
	}

	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

}
