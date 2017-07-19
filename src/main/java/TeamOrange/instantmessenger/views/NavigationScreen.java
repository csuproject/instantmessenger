package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavigationScreen extends Screen {

	HBox navigationBox;
	Button contactButton;
	Button chatButton;
	Button logoutButton;
	ChangeScreen changeScreen;
	
	public NavigationScreen(){
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
		chatButton = new Button("Groups");
		chatButton.setMinSize(100, 10);
		chatButton.setOnAction(e->this.changeScreen.SetScreen(ScreenEnum.MUC));
		logoutButton = new Button("Logout");
		logoutButton.setMinSize(100, 10);
		navigationBox.getChildren().addAll(contactButton, chatButton, logoutButton);
		this.getChildren().addAll(navigationBox);
	}
	
	public void setOnChangeScreen(ChangeScreen changeScreen){
		this.changeScreen = changeScreen;
	}

	
}
