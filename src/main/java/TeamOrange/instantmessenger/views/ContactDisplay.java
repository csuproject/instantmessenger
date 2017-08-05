package TeamOrange.instantmessenger.views;

import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import TeamOrange.instantmessenger.lambda.ChatButtonPress;
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
	private Circle presenceIndicator;
	//private PresenceIndicator presenceIndicator;

	public ContactDisplay(HomeScreen parent, String username){
		this.parent = parent;
		this.username = username;
		this.setId(username);  //each contact display will use UserID as its handle
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

		this.chatButton = new Button("Chat");
		this.chatButton.setOnAction(e->parent.chatButtonPress(username));

		//TO DO - link this circle to actual presence information and change colour accordingly
		this.presenceIndicator= new Circle(0,0,5);
		presenceIndicator.setFill(Color.LIGHTGRAY);

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: green;");
		this.getChildren().addAll(presenceIndicator,usernameLabel, chatButton);
	}

	public void showActivity(){

	}
}
