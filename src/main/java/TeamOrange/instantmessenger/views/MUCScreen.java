package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.MUCRoomEvent;
import TeamOrange.instantmessenger.models.AppMuc;
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
		private ScrollPane mucScrollPane;
		private VBox mucVBox;
//<<<<<<< HEAD
		private MUCRoomEvent addMUCEvent;
//=======
//		private GetMUCEvent getMUCEvent;
//		private AddMUCEvent addMUCEvent;
		private DeclineMucRequestEvent declineMucRequestEvent;
		private AcceptMucRequestEvent acceptMucRequestEvent;
//>>>>>>> refs/remotes/origin/c4-ft-muc-improvement
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
			// restrict input to lower case
			addGroupTextField.setOnKeyTyped(keyEvent->loginUserNameTextFieldFormatValidation(keyEvent));
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

		public void load(MUCScreenInput input){
			mucVBox.getChildren().clear();
			displayList.clear();

			List<AppMuc> mucList = (List)((LinkedList)input.getMUCList()).clone();
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

			List<AppMucRequest> requests = input.getMucRequests();
			for(AppMucRequest request : requests){
				MucRequestDisplay requestDisplay = new MucRequestDisplay(this, request.getFrom().getLocal(), request.getRoomID());
				mucVBox.getChildren().add(requestDisplay);
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

		public void setOnAcceptMucRequest(AcceptMucRequestEvent acceptMucRequestEvent){
			this.acceptMucRequestEvent = acceptMucRequestEvent;
		}

		public void setOnDeclineMucRequest(DeclineMucRequestEvent declineMucRequestEvent){
			this.declineMucRequestEvent = declineMucRequestEvent;
		}

		public void setOnChangeScreen(ChangeScreen changeScreen){
			this.changeScreen = changeScreen;
		}

//		public void reset(){
//			Platform.runLater(new Runnable(){
//				@Override public void run(){
//					mucList.clear();
//					displayList.clear();
//					mucVBox.getChildren().clear();
//				}
//			});
//		}

		public void acceptMucRequestButtonPress(String roomID, String from){
			this.acceptMucRequestEvent.accept(roomID, from);
		}

		public void declineMucRequestButtonPress(String roomID, String from){
			this.declineMucRequestEvent.decline(roomID, from);
		}



}
