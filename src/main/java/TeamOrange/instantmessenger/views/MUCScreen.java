package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppMucList;
import TeamOrange.instantmessenger.models.AppMucRequest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeamOrange.instantmessenger.lambda.AcceptMucRequestEvent;
import TeamOrange.instantmessenger.lambda.AddMUCEvent;
import TeamOrange.instantmessenger.lambda.ChangeScreen;
import TeamOrange.instantmessenger.lambda.DeclineMucRequestEvent;
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
import resources.GroupList;

	public class MUCScreen extends Screen {

		private ChangeScreen changeScreen;
		private List<MUCContactDisplay> displayList;
		private List<AppMuc> mucList;
		private ScrollPane mucScrollPane;
		private VBox mucVBox;
		private GetMUCEvent getMUCEvent;
		private AddMUCEvent addMUCEvent;
		private DeclineMucRequestEvent declineMucRequestEvent;
		private AcceptMucRequestEvent acceptMucRequestEvent;
		private VBox screenVBox;
		private HBox addGroupHBox;
		private HBox topHBox;
		private TextField addGroupTextField;
		private LinkedList<String> groupList;
		private Image imageMessage;
		private Image imageNewMessage;


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

			mucList = new ArrayList<AppMuc>();
			displayList = new ArrayList<MUCContactDisplay>();
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

			// Add Group Chat UI
			addGroupTextField = new TextField();
			addGroupTextField.setPromptText("Add Group Chat");
			addGroupTextField.setMinHeight(35);
			addGroupTextField.setMinWidth(320);
			// restrict input to lower case
			addGroupTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
			addGroupTextField.setOnKeyPressed(ke->{
				if(ke.getCode() == KeyCode.ENTER){
					addGroup();
				} else if(ke.getCode() == KeyCode.ESCAPE){
					closeAddGroupBox();
				}
			});
			Image imageAccept = new Image(getClass().getResource(
					"/resources/accept-icon.png").toURI().toString(),25,25,false,false);
			Button acceptAddGroupButton = new Button();
			acceptAddGroupButton.setMaxWidth(30);
			acceptAddGroupButton.setGraphic(new ImageView(imageAccept));
			acceptAddGroupButton.setOnAction(e->addGroup());
			acceptAddGroupButton.setFocusTraversable(false);
			Image imageDecline = new Image(getClass().getResource(
					"/resources/decline-icon.png").toURI().toString(),25,25,false,false);
			Button declineAddGroupButton = new Button();
			declineAddGroupButton.setMaxWidth(20);
			declineAddGroupButton.setGraphic(new ImageView(imageDecline));
			declineAddGroupButton.setOnAction(e->closeAddGroupBox());
			declineAddGroupButton.setFocusTraversable(false);
			addGroupHBox = new HBox(
					addGroupTextField,acceptAddGroupButton,declineAddGroupButton);
			addGroupHBox.setAlignment(Pos.CENTER);

			// MUC List
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
			displayList = new LinkedList<MUCContactDisplay>();

			// Chat status images
			imageMessage = new Image(getClass().getResource(
					"/resources/message.png").toURI().toString(),50,50,false,false);
			imageNewMessage = new Image(getClass().getResource(
					"/resources/message-new.png").toURI().toString(),50,50,false,false);

			// VBox Container holds all Objects
			screenVBox = new VBox();
			screenVBox.getChildren().addAll(topHBox, mucScrollPane);
			this.setMinHeight(500);
			this.setMaxHeight(500);
			this.getChildren().add(screenVBox);
		}

		private void writeGroupList(GroupList getGroupList) {
		      try {
		          FileOutputStream fileOut =
		          new FileOutputStream("./GroupList.ser");
		          ObjectOutputStream out = new ObjectOutputStream(fileOut);
		          out.writeObject(getGroupList);
		          out.close();
		          fileOut.close();
		          System.out.printf("Serialized data is saved in GroupList.ser");
		       }catch(IOException i) {
		          i.printStackTrace();
		       }
		}

		private void readGroupList() {
			GroupList getGroupList = null;
		     try {
		         FileInputStream fileIn = new FileInputStream("./GroupList.ser");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         getGroupList = (GroupList) in.readObject();
		         in.close();
		         fileIn.close();
		      }catch(IOException i) {
		         i.printStackTrace();
		         getGroupList = new GroupList();
		    	  writeGroupList(getGroupList);
		         return;
		      }catch(ClassNotFoundException c) {
		         System.out.println("GroupList class not found");
		         c.printStackTrace();
		    	  writeGroupList(getGroupList);
		         return;
		      }
		}

		/**
		 * Load new MUC
		 * @param mucList
		 */
		public void loadNew(AppMucList mucs) {
			mucList.clear();
			displayList.clear();
			mucVBox.getChildren().clear();

			List<AppMuc> inputMucList = mucs.getMucList();
			for(AppMuc appMUC : inputMucList){
				MUCContactDisplay mucDisplay =
						new MUCContactDisplay(appMUC,imageMessage, imageNewMessage);
				mucDisplay.setOnGetMUCEvent(e->getMUCEvent.getMUC(e));
				this.mucList.add(appMUC);
				displayList.add(mucDisplay);
				mucVBox.getChildren().add(mucDisplay);
			}

			List<AppMucRequest> requests = mucs.getMucRequests();
			for(AppMucRequest request : requests){
				MucRequestDisplay requestDisplay = new MucRequestDisplay(this, request.getFrom().getLocal(), request.getRoomID());
				mucVBox.getChildren().add(requestDisplay);
			}

			////////////////////////////////////////

//			List<AppMuc> inputMucList = mucs.getMucList();
//			for(AppMuc appMUC : inputMucList){
//				if(!this.mucList.contains(appMUC)) {
//					MUCContactDisplay mucDisplay =
//							new MUCContactDisplay(appMUC,imageMessage, imageNewMessage);
//					mucDisplay.setOnGetMUCEvent(e->getMUCEvent.getMUC(e));
//					this.mucList.add(appMUC);
//					displayList.add(mucDisplay);
//					mucVBox.getChildren().add(mucDisplay);
//				}
//			}
//
//			List<AppMucRequest> requests = mucs.getMucRequests();
//			for(AppMucRequest request : requests){
//				MucRequestDisplay requestDisplay = new MucRequestDisplay(this, request.getFrom().getLocal(), request.getRoomID());
//				mucVBox.getChildren().add(requestDisplay);
//			}
		}

		public void loadNewLater(AppMucList mucs){
			Platform.runLater(new Runnable(){
				@Override public void run(){
					loadNew(mucs);
				}
			});
		}

		/**
		 * Load Screen
		 * @param mucList
		 */
		public void loadNewMessage(AppMuc muc) {

			// Find MUC index
			for(MUCContactDisplay display : displayList) {
				if(display.appMUC == muc) {
					Platform.runLater(new Runnable(){
						@Override public void run(){
							display.setNewMessageImage();}});
				}
			}
		}


		private void addGroup() {
			String groupName;
			groupName = addGroupTextField.getText();
			closeAddGroupBox();
			addMUCEvent.getMUCName(groupName);
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

		public void setOnOpenMUC(GetMUCEvent getMUCEvent) {
			this.getMUCEvent = getMUCEvent;
		}

		public void setOnAddGroupGetMUCEvent(AddMUCEvent addMUCEvent) {
			this.addMUCEvent = addMUCEvent;
		}

		public void setOnAcceptMucRequest(AcceptMucRequestEvent acceptMucRequestEvent){
			this.acceptMucRequestEvent = acceptMucRequestEvent;
		}

		public void setOnDeclineMucRequest(DeclineMucRequestEvent declineMucRequestEvent){
			this.declineMucRequestEvent = declineMucRequestEvent;
		}

		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}

		public void reset(){
			Platform.runLater(new Runnable(){
				@Override public void run(){
					mucList.clear();
					displayList.clear();
					mucVBox.getChildren().clear();
				}
			});
		}

		public void acceptMucRequestButtonPress(String roomID, String from){
			this.acceptMucRequestEvent.accept(roomID, from);
		}

		public void declineMucRequestButtonPress(String roomID, String from){
			this.declineMucRequestEvent.decline(roomID, from);
		}



}
