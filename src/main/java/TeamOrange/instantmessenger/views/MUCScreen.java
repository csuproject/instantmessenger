package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.MUCRoomEvent;
import TeamOrange.instantmessenger.models.AppMuc;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

	public class MUCScreen extends Screen {

		private ChangeScreen changeScreen;
		private List<MUCContactDisplay> displayList;
		private ScrollPane mucScrollPane;
		private VBox mucVBox;
		private MUCRoomEvent addMUCEvent;
		private VBox screenVBox;
		private HBox addGroupHBox;
		private HBox topHBox;
		private TextField addGroupTextField;
		private Image imageMessage;
		private Image imageNewMessage;
		private String mucInFocus;
		private MUCRoomEvent openMUCEvent;

		public MUCScreen(GuiBase guiBase){
			super(guiBase);
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

			//////////////////////////////////////////////////////////////////////////////
			//-------------------------------Resources----------------------------------//
			//////////////////////////////////////////////////////////////////////////////
			Image imageAccept = new Image(getClass().getResource(
					"/resources/accept-icon.png").toURI().toString(),25,25,false,false);
			Image imageDecline = new Image(getClass().getResource(
					"/resources/decline-icon.png").toURI().toString(),25,25,false,false);
			imageMessage = new Image(getClass().getResource(
					"/resources/message.png").toURI().toString(),50,50,false,false);
			imageNewMessage = new Image(getClass().getResource(
					"/resources/message-new.png").toURI().toString(),50,50,false,false);
			
			//////////////////////////////////////////////////////////////////////////////
			//-------------------------------Top Control Display------------------------//
			//////////////////////////////////////////////////////////////////////////////
			Button createGroupButton = new Button("Create Group");
			createGroupButton.setMinWidth(100);
			createGroupButton.setOnAction(e->changeScreen.SetScreen(ScreenEnum.CREATEMUC));
			createGroupButton.setFocusTraversable(false);
			Button addGroupButton = new Button("Add Group");
			addGroupButton.setMinWidth(100);
			addGroupButton.setOnAction(e->openAddGroupBox());
			addGroupButton.setFocusTraversable(false);
			topHBox = new HBox(addGroupButton,createGroupButton);
			topHBox.setAlignment(Pos.CENTER);
			
			//////////////////////////////////////////////////////////////////////////////
			//-------------------------------Add Group Display--------------------------//
			//////////////////////////////////////////////////////////////////////////////
			addGroupTextField = new TextField();
			addGroupTextField.setPromptText("Add Group Chat");
			addGroupTextField.setMinHeight(35);
			addGroupTextField.setMinWidth(320);
			addGroupTextField.textProperty().addListener( // restrict input to lower case
			  (observable, oldValue, newValue) -> {
			    ((javafx.beans.property.StringProperty)observable).setValue(
			    		newValue.toLowerCase());	}	);
			addGroupTextField.setOnKeyPressed(ke->{
				if(ke.getCode() == KeyCode.ENTER){	addGroup();
				} else if(ke.getCode() == KeyCode.ESCAPE){	closeAddGroupBox();	}	});
			Button acceptAddGroupButton = new Button();
			acceptAddGroupButton.setMaxWidth(30);
			acceptAddGroupButton.setGraphic(new ImageView(imageAccept));
			acceptAddGroupButton.setOnAction(e->addGroup());
			acceptAddGroupButton.setFocusTraversable(false);
			Button declineAddGroupButton = new Button();
			declineAddGroupButton.setMaxWidth(20);
			declineAddGroupButton.setGraphic(new ImageView(imageDecline));
			declineAddGroupButton.setOnAction(e->closeAddGroupBox());
			declineAddGroupButton.setFocusTraversable(false);
			addGroupHBox = new HBox(
					addGroupTextField,acceptAddGroupButton,declineAddGroupButton);
			addGroupHBox.setAlignment(Pos.CENTER);
			
			//////////////////////////////////////////////////////////////////////////////
			//-------------------------------MUC List Display---------------------------//
			//////////////////////////////////////////////////////////////////////////////
			mucScrollPane = new ScrollPane();
			mucScrollPane.setFocusTraversable(false);
			mucScrollPane.setOnMouseClicked((e)->addGroupTextField.requestFocus());
			mucScrollPane.setStyle("-fx-focus-color: transparent;");
			mucVBox = new VBox();
			mucVBox.setPrefHeight(500);
			mucVBox.setPrefWidth(500);
			mucScrollPane.setContent(mucVBox);
			mucScrollPane.setFitToWidth(true);
			mucScrollPane.setFitToHeight(true);
			mucScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
			
			//////////////////////////////////////////////////////////////////////////////
			//----------------------------------Screen----------------------------------//
			//////////////////////////////////////////////////////////////////////////////
			displayList = new ArrayList<MUCContactDisplay>();
			screenVBox = new VBox();
			screenVBox.getChildren().addAll(topHBox, mucScrollPane);
			this.setMinHeight(500);
			this.setMaxHeight(500);
			this.getChildren().add(screenVBox);
		}
		
		public void loadLater(MUCScreenInput input){
			Platform.runLater(new Runnable(){
				@Override public void run(){
					load(input);
				}
			});
		}
		
		public void load(MUCScreenInput input){
			mucVBox.getChildren().clear();
			displayList.clear();

			LinkedList<AppMuc> mucList = input.getMUCList();
			for(AppMuc muc : mucList){
				
				MUCContactDisplay mucDisplay = 
						new MUCContactDisplay(muc,imageMessage, imageNewMessage);
				
				// Open MUC
				mucDisplay.setOnGetMUCEvent(e->{
					muc.setNotification(false);
					openMUCEvent.getRoomID(e.getRoomID());
				});
	
				// Set Notification
				if (muc.getNotification()) 
					mucDisplay.setNewMessageImage();
				else
					mucDisplay.setMessageImage();
				// Add to HomeScreen List
				displayList.add(mucDisplay);
				mucVBox.getChildren().add(mucDisplay);
			}
		}
		
		/**
		 * Add MUC to list
		 */
		private void addGroup() {
			String roomID = addGroupTextField.getText();
			closeAddGroupBox();
			addMUCEvent.getRoomID(roomID);
		}

		/**
		 * Open add group box
		 */
		private void openAddGroupBox() {
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(topHBox, addGroupHBox, mucScrollPane);
			addGroupTextField.requestFocus();
		}

		/**
		 * Close add group box
		 */
		private void closeAddGroupBox() {
			addGroupTextField.clear();
			screenVBox.getChildren().clear();
			screenVBox.getChildren().addAll(topHBox, mucScrollPane);
		}
		
		public void setOnOpenMUC(MUCRoomEvent openMUCEvent) {
			this.openMUCEvent = openMUCEvent;
		}

		public void setOnAddMUC(MUCRoomEvent addMUCEvent) {
			this.addMUCEvent = addMUCEvent;
		}

		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}



}
