package TeamOrange.instantmessenger.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class ChatScreen extends Screen {

	private Label header;


	public ChatScreen(){
		try {
			create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create() throws Exception {
		// header
		header = new Label("Chat with Unknown");
		header.setFont(Font.font(60));

		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.add(header, 0, 0);
		gridPane.setAlignment(Pos.CENTER);

		this.getChildren().add(gridPane);
	}

	public void load(ChatScreenInput input){
		header.setText(input.getPartner());
	}
}
