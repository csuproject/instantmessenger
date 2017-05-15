package TeamOrange.instantmessenger.views;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;

public class MessageDisplay extends VBox{
	Label username;
	Label message;

	public MessageDisplay(String username, String message){
		this.username = new Label(username);
		Font font = new Font(25);
		this.username.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
		this.message = new Label(message);
		this.message.setFont(Font.font(20));
		this.message.setWrapText(true);
		//this.message.setPrefWidth(640-20-20-20);

		this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
		this.setPrefWidth(640-20-20-20);
		this.getChildren().addAll(this.username, this.message);
	}


}
