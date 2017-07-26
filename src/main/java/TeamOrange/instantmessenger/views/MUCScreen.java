package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.models.AppMuc;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

	public class MUCScreen extends Screen {
		
		private ChangeScreen changeScreen;
		private LinkedList<MUCContactDisplay> displayList;
		private ScrollPane mucScrollPane;
		private VBox mucVBox;
		private GetMUCEvent getMUCEvent;

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

			displayList = new LinkedList<MUCContactDisplay>();
			Button button = new Button("Create Group");
			button.setAlignment(Pos.CENTER);
			button.setMinWidth(100);
			button.setOnAction(e->changeScreen.SetScreen(ScreenEnum.CREATEMUC));
			HBox topHbox = new HBox(button);
			topHbox.setAlignment(Pos.CENTER);
			
			// MUC List
			mucScrollPane = new ScrollPane();
			mucVBox = new VBox();
			mucVBox.setPrefHeight(400);
			mucVBox.setPrefWidth(400);
			mucScrollPane.setContent(mucVBox);
			mucScrollPane.setFitToWidth(true);
			mucScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
			displayList = new LinkedList<MUCContactDisplay>();
		
			// VBox Container holds all Objects
			VBox vbox = new VBox();
			vbox.getChildren().addAll(topHbox, mucScrollPane);
			this.getChildren().add(vbox);
		}

		public void loadLater(List<AppMuc> mucList){
			Platform.runLater(new Runnable(){
				@Override public void run(){
					load(mucList);
				}
			});
		}

		public void load(List<AppMuc> mucList){
			mucVBox.getChildren().clear();
			displayList.clear();
			
			for(AppMuc appMUC : mucList){
				MUCContactDisplay mucDisplay = new MUCContactDisplay(appMUC);
				mucDisplay.setOnGetMUCEvent(e->getMUCEvent.getMUC(e));
				mucVBox.getChildren().add(mucDisplay);
				displayList.add(mucDisplay);
			}
		}
		
		public void setOnOpenMUC(GetMUCEvent getMUCEvent) {
			this.getMUCEvent = getMUCEvent;
		}
		
		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}
		
}
