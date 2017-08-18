package TeamOrange.instantmessenger.views;

import java.net.URISyntaxException;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatusDisplay extends Screen {
	private Label labelUserName;
	private Label labelStatus;
	private Label screenView;
	Image imageOnline;
	Image imageOffline;
	ImageView viewStatusImage;

	public StatusDisplay(GuiBase guiBase) {
		super(guiBase);
		try {
			imageOnline = new Image(getClass().getResource(
					"/resources/accept-icon.png").toURI().toString(),20,20,false,false);
			imageOffline = new Image(getClass().getResource(
					"/resources/decline-icon.png").toURI().toString(),20,20,false,false);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		viewStatusImage = new ImageView(imageOffline);
		labelStatus = new Label("Offline");
		labelStatus.setStyle("-fx-font: 15 arial;");
		labelUserName = new Label("username");
		labelUserName.setStyle("-fx-font: 15 arial;");
		HBox hboxUserName = new HBox(labelUserName);
		HBox hboxConn = new HBox(labelStatus, viewStatusImage);
		hboxConn.setSpacing(10);
		HBox hboxStatus = new HBox(hboxUserName, hboxConn);
		hboxStatus.setSpacing(100);
		screenView = new Label();
		screenView.setStyle("-fx-font: 20 arial;");
		screenView.setMinWidth(50);
		HBox hboxScreen = new HBox(screenView);
		HBox hboxTop = new HBox(hboxUserName,hboxScreen,hboxStatus);
		hboxTop.setAlignment(Pos.CENTER);
		hboxTop.setSpacing(50);
		hboxTop.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 0;" + 
                "-fx-border-color: black;");
		this.getChildren().add(hboxTop);
	}
	
	/**
	 * Set the connection status of display
	 * @param connectionStatus
	 */
	public void setConnectionStatus(boolean connectionStatus) {
		if(connectionStatus) {
			viewStatusImage.setImage(imageOnline);
			labelStatus.setText("Online");
		}
		else {
			viewStatusImage.setImage(imageOffline);
			labelStatus.setText("Offline");
		}
	}
	
	/**
	 * Set the user name of display
	 * @param userName
	 */
	public void setUserName(String userName) {
		labelUserName.setText(userName);
	}
	
	/**
	 * Set screen view text
	 * @param screen
	 */
	public void setScreenView(ScreenEnum screen) {
	   	switch(screen){
				case HOME:
				{screenView.setText("Contacts");} break;
				case MUC:
				{screenView.setText("Groups");} break;
				case CREATEMUC:
				{screenView.setText("Create Group");} break;
				case CHAT:
				{screenView.setText("Chat");} break;
				case MUCCHAT:
				{screenView.setText("Chat");} break;
	   	}
	}
}
