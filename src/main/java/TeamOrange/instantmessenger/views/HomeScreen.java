package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.models.AppJid;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class HomeScreen extends Screen {

	private Label header;

	public HomeScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {

		// header
		header = new Label("Hello, Unknown");
		header.setFont(Font.font(60));


//		// User Name Input
//		Label userNameLabel = new Label("Username: " );
//		userNameTextField = new TextField();
//		HBox userNameInput = new HBox();
//		userNameInput.getChildren().addAll(userNameLabel, userNameTextField);
//		userNameInput.setSpacing(10);
//
//		// Password Input
//		Label passwordLabel = new Label("Password: " );
//		passwordTextField = new TextField();
//		HBox passwordInput = new HBox();
//		passwordInput.getChildren().addAll(passwordLabel, passwordTextField);
//		passwordInput.setSpacing(10);
//
//		// Buttons
//		login = new Button();
//		login.setText("Login");
//		login.setOnAction(e -> loginBtnPress() );
//		createAccount = new Button();
//		createAccount.setText("Create Account");
//		createAccount.setOnAction(e -> createAccountBtnPress() );
//		HBox buttons = new HBox();
//		buttons.getChildren().addAll(login, createAccount);
//		buttons.setSpacing(20);

		//Pane
		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.add(header, 0, 0);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}

	public void load(HomeScreenInput input){
		header.setText("Hello, " + input.getName());
	}

}
