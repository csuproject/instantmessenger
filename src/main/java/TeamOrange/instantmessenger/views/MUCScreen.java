package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.DeclineContactRequestEvent;
import java.util.LinkedList;
import TeamOrange.instantmessenger.lambda.AcceptContactRequestEvent;
import TeamOrange.instantmessenger.lambda.AddContactEvent;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.ChatWithContactEvent;
import TeamOrange.instantmessenger.models.AppJid;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

	public class MUCScreen extends Screen {
		
		private ChangeScreen changeScreen;

		public MUCScreen(){
			try {
				create();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Create Screen
		 * @throws Exception
		 */
		public void create() throws Exception {

			Button button = new Button("Create Group");
			button.setOnAction(e->changeScreen.SetScreen(ScreenEnum.CREATEMUC));
			this.getChildren().add(button);
		}

		public void loadLater(MUCScreenInput input){
			Platform.runLater(new Runnable(){
				@Override public void run(){
					load(input);
				}
			});
		}

		public void load(MUCScreenInput input){

		}
		
		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}


}
