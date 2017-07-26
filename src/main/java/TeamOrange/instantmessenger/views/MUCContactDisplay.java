package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.lambda.GetMUCEvent;
import TeamOrange.instantmessenger.lambda.SelectAppUser;
import TeamOrange.instantmessenger.models.AppMuc;
import TeamOrange.instantmessenger.models.AppUser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MUCContactDisplay extends HBox {

	private String username;
	private Label usernameLabel;
	private boolean selected;
	private SelectAppUser selectAppUser;
	private GetMUCEvent getMUCEvent;
	private AppUser appUser;
	private AppMuc appMUC;
 
	public MUCContactDisplay(AppUser appUser){
		this.username = appUser.getJid().getLocal();
		this.appUser = appUser;
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		
		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
		this.setMaxWidth(380);
		this.setOnMouseClicked(e->select());
		this.getChildren().addAll(usernameLabel);
	}
	
	public void select() {
		if (selected) {
			this.setStyle("-fx-padding: 5;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 3;" +
	                "-fx-border-radius: 5;" +
	                "-fx-border-color: black;");
			selected = false;
			System.out.println("Unselect");
			selectAppUser.getAppUser(appUser);
		} else {
			this.setStyle("-fx-padding: 5;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 3;" +
	                "-fx-border-radius: 5;" +
	                "-fx-border-color: red;");
			selected = true;
			System.out.println("Select");
			selectAppUser.getAppUser(appUser);
		}
	}
	
	public MUCContactDisplay(AppMuc appMUC){
		this.appMUC = appMUC;
		this.username = appMUC.getRoomID();
		usernameLabel = new Label(username);
		usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		
		this.setStyle("-fx-padding: 5;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
		this.setMaxWidth(380);
		this.setOnMouseClicked(e->getMUCEvent.getMUC(this.appMUC));
		this.getChildren().addAll(usernameLabel);
	}
	
	public void setOnSelectAppUser(SelectAppUser selectAppUser) {
		this.selectAppUser = selectAppUser;
	}
	
	public void setOnGetMUCEvent (GetMUCEvent getMUCEvent) {
		this.getMUCEvent = getMUCEvent;
	}
}
