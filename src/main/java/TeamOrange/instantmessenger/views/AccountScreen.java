package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.lambda.LoginEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class AccountScreen extends Screen {

	private TextField userNameTextField;
	private TextField passwordTextField;
	private Button login;
	private Button createAccount;

	private CreateAccountEvent createAccountEvent;
	private LoginEvent loginEvent;

	public AccountScreen(){
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
		login = new Button();
		login.setText("Login");
		login.setOnAction(e -> loginBtnPress() );
		createAccount = new Button();
		createAccount.setText("Create Account");
		createAccount.setOnAction(e -> createAccountBtnPress() );
		HBox buttons = new HBox();
		buttons.getChildren().addAll(login, createAccount);
		buttons.setSpacing(20);

		//Pane
		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.add(userNameInput, 0, 0);
		gridPane.add(passwordInput, 0, 1);
		gridPane.add(buttons, 0, 2);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}


	public void loginBtnPress(){
		String username = userNameTextField.getText();
		String password = passwordTextField.getText();
		loginEvent.login(username, password);
	}

	public void createAccountBtnPress(){
		String username = userNameTextField.getText();
		String password = passwordTextField.getText();
		createAccountEvent.createAccount(username, password);
	}

	public void setOnCreateAccountEvent(CreateAccountEvent createAccountEvent){
		this.createAccountEvent = createAccountEvent;
	}

	public void setOnLoginEvent(LoginEvent loginEvent){
		this.loginEvent = loginEvent;
	}

}
