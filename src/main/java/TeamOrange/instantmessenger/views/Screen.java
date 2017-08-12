package TeamOrange.instantmessenger.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;

public abstract class Screen extends StackPane {

	private GuiBase guiBase;

	public Screen(GuiBase guiBase){
		this.guiBase = guiBase;
	}

	protected void load(ScreenInput input){

	}

	public void loadLater(ScreenInput input){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				load(input);
			}
		});
	}

	public void alertLater(String message, String title, AlertType type){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				alert(message, title, type);
			}
		});
	}

	public void alert(String message, String title, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.setX( guiBase.getCenterX() - 211 );
		alert.setY( guiBase.getCenterY() - 70 );
		alert.showAndWait();
	}
}
