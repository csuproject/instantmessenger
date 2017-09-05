package TeamOrange.instantmessenger.views;

import java.net.URISyntaxException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;

public abstract class Screen extends StackPane {

	private GuiBase guiBase;
	private AudioClip wrongKeyAudioClip;

	public Screen(GuiBase guiBase){
		this.guiBase = guiBase;
		try {
			wrongKeyAudioClip = new AudioClip(
					getClass().getResource("/resources/misc_menu.wav").toURI().toString() );
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void loginUserNameTextFieldFormatValidation(KeyEvent keyEvent){
		char c = keyEvent.getCharacter().charAt(0);
		if( (c >= 'A' && c <= 'Z') ){
			wrongKeyAudioClip.play();
			keyEvent.consume();
		}
	}
}
