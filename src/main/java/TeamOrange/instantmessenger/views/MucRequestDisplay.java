package TeamOrange.instantmessenger.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * Displays a muc request.
 *
 */
public class MucRequestDisplay extends FlowPane {
	private MUCScreen parent;
	private String from;
	private String roomName;
	private Label label;
	private Button acceptButton;
	private Button declineButton;

	public MucRequestDisplay(MUCScreen parent, String from, String roomName) {
		this.parent = parent;
		this.from = from;
		this.roomName = roomName;
		label = new Label(from + " invites you to join the room \"" + roomName + "\"");
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 10));

		this.acceptButton = new Button("Accept");
		this.acceptButton.setFocusTraversable(false);
		this.acceptButton.setOnAction(e->parent.acceptMucRequestButtonPress(roomName, from));

		this.declineButton = new Button("Decline");
		this.declineButton.setFocusTraversable(false);
		this.declineButton.setOnAction(e->parent.declineMucRequestButtonPress(roomName, from));

		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: red;");
		this.setHgap(10);
		this.getChildren().addAll(label, acceptButton, declineButton);
	}

	public String getRoomName() {
		return this.roomName;
	}

}
