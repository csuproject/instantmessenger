package TeamOrange.instantmessenger.views;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * Displays a message.
 *
 */
public class MessageDisplay extends VBox{
	Label username;
	Label message;

	public MessageDisplay(String username, String message, boolean isSent, boolean outbound){
		this.username = new Label(username);
		Font font = new Font(25);
		this.username.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		this.message = new Label(message);
		this.message.setFont(Font.font(15));
		this.message.setWrapText(true);
		//this.message.setPrefWidth(640-20-20-20);

		String borderColor = "blue";
		if(outbound){
			if(isSent){
				borderColor = "green";
			} else{
				borderColor = "red";
			}
		}


		this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: "+borderColor+";");



		this.setPrefWidth(300);
		this.getChildren().addAll(this.username, this.message);
	}


}
