package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.models.AppMuc;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.AddMUCEvent;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

	public class MUCScreen extends Screen {
		
		private ChangeScreen changeScreen;
		private LinkedList<MUCContactDisplay> displayList;
		private ScrollPane mucScrollPane;
		private VBox mucVBox;
		private GetMUCEvent getMUCEvent;
		private AddMUCEvent addMUCEvent;
		private VBox screenVBox;
		private HBox addGroupHBox;
		private HBox topHBox;
		private TextField addGroupTextField;

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
			Button createGroupButton = new Button("Create Group");
			createGroupButton.setMinWidth(100);
			createGroupButton.setOnAction(e->changeScreen.SetScreen(ScreenEnum.CREATEMUC));
			Button addGroupButton = new Button("Add Group");
			addGroupButton.setMinWidth(100);
			addGroupButton.setOnAction(e->openAddGroupBox());
			topHBox = new HBox(addGroupButton,createGroupButton);
			topHBox.setAlignment(Pos.CENTER);
			
			// Add Group Chat UI
			addGroupTextField = new TextField();
			addGroupTextField.setPromptText("Add Group Chat");
			addGroupTextField.setMinHeight(35);
			addGroupTextField.setMinWidth(320);
			Image imageAccept = new Image(getClass().getResource(
					"/resources/accept-icon.png").toURI().toString(),25,25,false,false);
			Button accpetAddGroupButton = new Button();
			accpetAddGroupButton.setMaxWidth(30);
			accpetAddGroupButton.setGraphic(new ImageView(imageAccept));
			accpetAddGroupButton.setOnAction(e->addGroup());
			Image imageDecline = new Image(getClass().getResource(
					"/resources/decline-icon.png").toURI().toString(),25,25,false,false);
			Button declineAddGroupButton = new Button();
			declineAddGroupButton.setMaxWidth(20);
			declineAddGroupButton.setGraphic(new ImageView(imageDecline));
			declineAddGroupButton.setOnAction(e->closeAddGroupBox());
			addGroupHBox = new HBox(
					addGroupTextField,accpetAddGroupButton,declineAddGroupButton);
			addGroupHBox.setAlignment(Pos.CENTER);
			
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
			screenVBox = new VBox();
			screenVBox.getChildren().addAll(topHBox, mucScrollPane);
			this.getChildren().add(screenVBox);
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
		
		private void addGroup() {
			String groupName;
			groupName = addGroupTextField.getText();
			addMUCEvent.getMUCName(groupName);
			closeAddGroupBox();
		}
		
		private void openAddGroupBox() {
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(topHBox, addGroupHBox, mucScrollPane);
		}
		
		private void closeAddGroupBox() {
			addGroupTextField.clear();
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(topHBox, mucScrollPane);
		}
		
		public void setOnOpenMUC(GetMUCEvent getMUCEvent) {
			this.getMUCEvent = getMUCEvent;
		}
		
		public void setOnAddGroupGetMUCEvent(AddMUCEvent addMUCEvent) {
			this.addMUCEvent = addMUCEvent;
		}
		
		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}
		

		
}
