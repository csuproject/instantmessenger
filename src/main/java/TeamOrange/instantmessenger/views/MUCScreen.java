package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.MUCRoomEvent;
import TeamOrange.instantmessenger.lambda.OpenInviteMucScreenEvent;
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
import TeamOrange.instantmessenger.lambda.DeleteMucEvent;
import TeamOrange.instantmessenger.lambda.GetMUCEvent;
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
		private MUCRoomEvent addMUCEvent;
		private DeclineMucRequestEvent declineMucRequestEvent;
		private AcceptMucRequestEvent acceptMucRequestEvent;
		private VBox screenVBox;
		private HBox addGroupHBox;
		private HBox topHBox;
		private TextField addGroupTextField;
		private Image imageMessage;
		private Image imageNewMessage;
		private MUCRoomEvent openMUCEvent;
		private OpenInviteMucScreenEvent openInviteMucScreenEvent;
		private DeleteMucEvent deleteMUCEvent;

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
			//-------------------------------Top Control Display------------------------//
			//////////////////////////////////////////////////////////////////////////////
			Button addGroupButton = new Button("Add Group");
			addGroupButton.setMinWidth(100);
			addGroupButton.setOnAction(e->openAddGroupBox());
			addGroupButton.setFocusTraversable(false);
			topHBox = new HBox(addGroupButton);
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
			acceptAddGroupButton.setGraphic( GuiBase.IMAGE_ACCEPT );
			acceptAddGroupButton.setOnAction(e->addGroup());
			acceptAddGroupButton.setFocusTraversable(false);
			Button declineAddGroupButton = new Button();
			declineAddGroupButton.setMaxWidth(20);
			declineAddGroupButton.setGraphic( GuiBase.IMAGE_DECLINE );
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

			List<AppMuc> mucList = (List)((LinkedList)input.getMUCList()).clone();
			for(AppMuc muc : mucList){

				MUCContactDisplay mucDisplay =
						new MUCContactDisplay(muc,imageMessage, imageNewMessage);

				// Open MUC
				mucDisplay.setOnOpenMUCEvent(e->{
					muc.setNotification(false);
					openMUCEvent.getRoomID(e.getRoomID());	});
				// Delete MUC
				mucDisplay.setOnInviteMUCEvent(mucToInvite->{
					openInviteMucScreenEvent.invite(mucToInvite);
				});
				// Invite MUC
				mucDisplay.setOnDeleteMUCEvent(mucToDelete->{
					deleteMUCEvent.delete(mucToDelete);
				});
//						delete->deleteMUCEvent.getRoomID(delete.getRoomID()));

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

		public void setOnOpenInviteMucScreenEvent (OpenInviteMucScreenEvent openInviteMucScreenEvent) {
			this.openInviteMucScreenEvent = openInviteMucScreenEvent;
		}

		public void setOnDeleteMUCEvent (DeleteMucEvent deleteMUCEvent) {
			this.deleteMUCEvent = deleteMUCEvent;
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

		public void acceptMucRequestButtonPress(String roomID, String from){
			this.acceptMucRequestEvent.accept(roomID, from);
		}

		public void declineMucRequestButtonPress(String roomID, String from){
			this.declineMucRequestEvent.decline(roomID, from);
		}



}
