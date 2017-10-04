package TeamOrange.instantmessenger.views;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
		Color borderColor = Color.BLUE;
		if(outbound){
			if(isSent){
				borderColor = Color.GREEN;
			} else{
				borderColor = Color.RED;
			}
		}

		setup(username, message, isSent, outbound, borderColor);
	}

	public MessageDisplay(String username, String message, boolean isSent, boolean outbound, Color color){
		setup(username, message, isSent, outbound, color);
	}

	private void setup(String username, String message, boolean isSent, boolean outbound, Color color){
		this.username = new Label(username);
		Font font = new Font(25);
		this.username.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		this.message = new Label(message);
		this.message.setFont(Font.font(15));
		this.message.setWrapText(true);
		//this.message.setPrefWidth(640-20-20-20);


		this.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: rgb("+color.getRed()*255+","+color.getGreen()*255+","+color.getBlue()*255+");");
		this.setBackground(new Background(new BackgroundFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.15), new CornerRadii(5), Insets.EMPTY)));


		this.setPrefWidth(300);
		this.getChildren().addAll(this.username, this.message);
	}


}
