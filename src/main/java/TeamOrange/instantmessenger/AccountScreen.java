package TeamOrange.instantmessenger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.InsetsBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AccountScreen extends StackPane {

	private Stage stage;
	private TextField userNameTextField;
	private TextField passwordTextField;
	App app;

//	public void run(){
//		try {
//			start(new Stage());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public AccountScreen(App app){
		this.app = app;
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {

		// User Name Input
		Label userNameLabel = new Label("Username: " );
		userNameTextField = new TextField();
		HBox userNameInput = new HBox();
		userNameInput.getChildren().addAll(userNameLabel, userNameTextField);
		userNameInput.setSpacing(10);

		// Password Input
		Label passwordLabel = new Label("Password: " );
		passwordTextField = new TextField();
		HBox passwordInput = new HBox();
		passwordInput.getChildren().addAll(passwordLabel, passwordTextField);
		passwordInput.setSpacing(10);

		// Buttons
		Button login = new Button();
		login.setText("Login");
		login.setOnAction(e -> loginBtnPress() );
		Button createAccount = new Button();
		createAccount.setText("Create Account");
		createAccount.setOnAction(e -> createAccountBtnPress() );
		HBox buttons = new HBox();
		buttons.getChildren().addAll(login, createAccount);
		buttons.setSpacing(20);

		//Pane
		//StackPane stackPane = new StackPane();
		GridPane gridPane = new GridPane();
//		root.setPadding(new Insets(20));
		gridPane.setVgap(20);
		gridPane.add(userNameInput, 0, 0);
		gridPane.add(passwordInput, 0, 1);
		gridPane.add(buttons, 0, 2);
//		root.getChildren().addAll(userNameInput, buttons);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);

		// Scene
//		Scene scene = new Scene(stackPane, 640, 480);
//		stage.setScene(scene);
//		stage.show();
	}

	public void loginBtnPress(){
		String username = userNameTextField.getText();
		String password = passwordTextField.getText();
		boolean success = app.login(username, password);

		if(success){
			System.out.println("successfully logged in");
		}
	}

	public void createAccountBtnPress(){
		String username = userNameTextField.getText();
		String password = passwordTextField.getText();
		app.createAccount(username, password);
	}

}
