package TeamOrange.instantmessenger.views;


import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.CreateAccountEvent;
import TeamOrange.instantmessenger.lambda.LoginEvent;
import exceptions.CreateUserInvalidFormatException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class AccountScreen extends Screen {

	private TextField loginUserNameTextField;
	private PasswordField loginPasswordTextField;
	private Button login;

	private TextField createAccountUserNameTextField;
	private PasswordField createAccountPasswordTextField;
	private Button createAccount;

	private CreateAccountEvent createAccountEvent;
	private LoginEvent loginEvent;

	public AccountScreen(GuiBase guiBase){
		super(guiBase);
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		// Login
		Label loginLabel = new Label("Login");
		loginLabel.setFont(new Font(30));
		//
		loginUserNameTextField = new TextField();
		loginUserNameTextField.setOnKeyPressed(keyEvent->loginKeyPressed(keyEvent));
		// restrict input to lower case
		loginUserNameTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
		//loginUserNameTextField.addEventHandler(KeyEvent.KEY_TYPED , loginUserNameTextFieldFormatValidation());
		Label loginUserNameLabel = new Label("Username: " );
		loginUserNameLabel.setFont(new Font(18));
		HBox loginUserNameInput = new HBox();
		loginUserNameInput.getChildren().addAll(loginUserNameLabel, loginUserNameTextField);
		loginUserNameInput.setSpacing(10);
		//
		loginPasswordTextField = new PasswordField();
		loginPasswordTextField.setOnKeyPressed(keyEvent->loginKeyPressed(keyEvent));
		Label loginPasswordLabel = new Label("Password: " );
		loginPasswordLabel.setFont(new Font(18));
		HBox loginPasswordInput = new HBox();
		loginPasswordInput.getChildren().addAll(loginPasswordLabel, loginPasswordTextField);
		loginPasswordInput.setSpacing(10);
		//
		login = new Button();
		login.setText("Login");
		login.setOnAction(e -> loginBtnPress() );
		login.setFocusTraversable(false);
		//
		VBox loginSection = new VBox();
		loginSection.getChildren().addAll(loginLabel, loginUserNameInput, loginPasswordInput, login);
		loginSection.setAlignment(Pos.CENTER);
		loginSection.setSpacing(10);
		loginSection.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: purple;");


		// Create an account
		Label createAccountLabel = new Label("Create Account");
		createAccountLabel.setFont(new Font(30));
		//
		createAccountUserNameTextField = new TextField();
		createAccountUserNameTextField.setOnKeyPressed(keyEvent->createAccountKeyPressed(keyEvent));
		// restrict input to lower case
		createAccountUserNameTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
		Label createAccountserNameLabel = new Label("Username: " );
		createAccountserNameLabel.setFont(new Font(18));
		HBox createAccountUserNameInput = new HBox();
		createAccountUserNameInput.getChildren().addAll(createAccountserNameLabel, createAccountUserNameTextField);
		createAccountUserNameInput.setSpacing(10);
		//
		createAccountPasswordTextField = new PasswordField();
		createAccountPasswordTextField.setOnKeyPressed(keyEvent->createAccountKeyPressed(keyEvent));
		Label createAccountPasswordLabel = new Label("Password: " );
		createAccountPasswordLabel.setFont(new Font(18));
		HBox createAccountPasswordInput = new HBox();
		createAccountPasswordInput.getChildren().addAll(createAccountPasswordLabel, createAccountPasswordTextField);
		createAccountPasswordInput.setSpacing(10);
		//
		createAccount = new Button();
		createAccount.setText("Create Account");
		createAccount.setOnAction(e -> createAccountBtnPress() );
		createAccount.setFocusTraversable(false);
		//
		VBox createAccountSection = new VBox();
		createAccountSection.getChildren().addAll(createAccountLabel, createAccountUserNameInput, createAccountPasswordInput, createAccount);
		createAccountSection.setAlignment(Pos.CENTER);
		createAccountSection.setSpacing(10);
		createAccountSection.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: purple;");


		//Pane
		GridPane gridPane = new GridPane();
		gridPane.setVgap(40);
		gridPane.add(loginSection, 0, 0);
		gridPane.add(createAccountSection, 0, 1);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}

	public void loginKeyPressed(javafx.scene.input.KeyEvent keyEvent){
		if( keyEvent.getCode().equals(javafx.scene.input.KeyCode.ENTER) ){
			loginBtnPress();
		}
	}

	public void createAccountKeyPressed(javafx.scene.input.KeyEvent keyEvent){
		if( keyEvent.getCode().equals(javafx.scene.input.KeyCode.ENTER) ){
			createAccountBtnPress();
		}
	}


	public void loginBtnPress(){
		String username = loginUserNameTextField.getText().trim();
		String password = loginPasswordTextField.getText().trim();
		loginUserNameTextField.clear();
		loginPasswordTextField.clear();
		loginEvent.login(username, password);
	}

	public void createAccountBtnPress(){
		String username = createAccountUserNameTextField.getText().trim();
		String password = createAccountPasswordTextField.getText().trim();
		createAccountUserNameTextField.clear();
		createAccountPasswordTextField.clear();

		if(isValidCreateAccountFormat(username, password)){
			createAccountEvent.createAccount(username, password);
//			alert("User( \""+username+"\" ) successfully created", "user created", AlertType.CONFIRMATION);
			loginUserNameTextField.setText(username);
			loginPasswordTextField.setText(password);
			loginUserNameTextField.requestFocus();
		} else{
			alert("Please ensure that you have filled out both username and password fields.", "invalid input", AlertType.WARNING);
		}
	}

	private boolean isValidCreateAccountFormat(String userName, String password){
		if(userName.isEmpty() || password.isEmpty()){
			return false;
		}
		return true;
	}

	public void setOnCreateAccountEvent(CreateAccountEvent createAccountEvent){
		this.createAccountEvent = createAccountEvent;
	}

	public void setOnLoginEvent(LoginEvent loginEvent){
		this.loginEvent = loginEvent;
	}

}
