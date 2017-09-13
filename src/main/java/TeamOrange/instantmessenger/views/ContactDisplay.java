package TeamOrange.instantmessenger.views;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import TeamOrange.instantmessenger.lambda.ChatButtonPress;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ContactDisplay extends FlowPane {

	private HomeScreen parent;
	private String username;
	private Label usernameLabel;
	private Button chatButton;

	public ContactDisplay(HomeScreen parent, String username){
		this.parent = parent;
		this.username = username;
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

		this.chatButton = new Button("Chat");
		this.chatButton.setOnAction(e->parent.chatButtonPress(username));
		this.chatButton.setFocusTraversable(false);

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: green;");
		this.setMaxWidth(350);
		this.setHgap(10);
		this.getChildren().addAll(usernameLabel, chatButton);
	}
}
