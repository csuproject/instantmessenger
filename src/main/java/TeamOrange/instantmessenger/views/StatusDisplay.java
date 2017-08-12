package TeamOrange.instantmessenger.views;

import java.net.URISyntaxException;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class StatusDisplay extends Screen {
	private Label labelUserName;
	private Label labelStatus;
	Image imageOnline;
	Image imageOffline;
	ImageView viewStatusImage;
	HBox hboxAll;
	
	public StatusDisplay() {
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
		HBox hboxStatus = new HBox(labelStatus, viewStatusImage);
		hboxStatus.setSpacing(20);
		hboxAll = new HBox(hboxUserName, hboxStatus);
		hboxAll.setSpacing(100);
		hboxAll.setAlignment(Pos.TOP_CENTER);
		hboxAll.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 0;" + 
                "-fx-border-color: black;");
		this.getChildren().add(hboxAll);
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
}
