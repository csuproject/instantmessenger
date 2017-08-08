package TeamOrange.instantmessenger.views;

import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import TeamOrange.instantmessenger.lambda.ChatButtonPress;
import TeamOrange.instantmessenger.lambda.SendNewMessageEvent;
import TeamOrange.instantmessenger.lambda.StatusEvent;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppPresenceIndicator;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class ContactDisplay extends HBox {

	private HomeScreen parent;
	private String username;
	private Label usernameLabel;
	private Button chatButton;
	private AppPresenceIndicator appPresenceIndicator;
	private Circle indicatorCircle;

	public ContactDisplay(HomeScreen parent, String username) {
		this.parent = parent;
		this.username = username;
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

		this.chatButton = new Button("Chat");
		this.chatButton.setOnAction(e -> parent.chatButtonPress(username));
		this.chatButton.setFocusTraversable(false);

		this.appPresenceIndicator = new AppPresenceIndicator(username,"teamorange.space");
		indicatorCircle = new Circle(0,0,7);
		indicatorCircle.setFill (appPresenceIndicator.setIndicatorColour());

		this.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
				+ "-fx-border-insets: 3;" + "-fx-border-radius: 5;" + "-fx-border-color: green;");
		this.setMaxWidth(350);
		this.getChildren().addAll(indicatorCircle, usernameLabel, chatButton);
	}

}
