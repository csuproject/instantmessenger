package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.LogoutEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavigationScreen extends Screen {

	HBox navigationBox;
	Button contactButton;
	Button chatButton;
	Button logoutButton;
	ChangeScreen changeScreen;
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
		contactButton = new Button("Chats");
		contactButton.setMinSize(100, 10);
		contactButton.setOnAction(e->this.changeScreen.SetScreen(ScreenEnum.HOME));
		contactButton.setFocusTraversable(false);
		chatButton = new Button("Groups");
		chatButton.setMinSize(100, 10);
		chatButton.setOnAction(e->this.changeScreen.SetScreen(ScreenEnum.MUC));
		chatButton.setFocusTraversable(false);
		logoutButton = new Button("Logout");
		logoutButton.setMinSize(100, 10);
		logoutButton.setOnAction( e->logout() );
		logoutButton.setFocusTraversable(false);
		navigationBox.getChildren().addAll(contactButton, chatButton, logoutButton);
		this.getChildren().addAll(navigationBox);
	}

	@Override
	public void load(ScreenInput input){

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
