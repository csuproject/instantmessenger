package TeamOrange.instantmessenger.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class ContactRequestDisplay extends FlowPane {
	private HomeScreen parent;
	private String username;
	private Label usernameLabel;
	private Button acceptButton;
	private Button declineButton;

	public ContactRequestDisplay(HomeScreen parent, String username){
		this.parent = parent;
		this.username = username;
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

		this.acceptButton = new Button("Accept");
		this.acceptButton.setOnAction(e->parent.acceptContactRequestButtonPress(username));

		this.declineButton = new Button("Decline");
		this.declineButton.setOnAction(e->parent.declineContactRequestButtonPress(username));

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: red;");
		this.setHgap(10);
		this.getChildren().addAll(usernameLabel, acceptButton, declineButton);
	}

	public String getName() {
		return this.username;
	}
}
