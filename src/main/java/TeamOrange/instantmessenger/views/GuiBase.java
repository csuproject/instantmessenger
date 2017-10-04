package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.App;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This is the starting point for the application
 * It creates the javafx stage, and changes the screen
 *
 */
public class GuiBase extends Application {

	public static ImageView IMAGE_ACCEPT;
	public static ImageView IMAGE_DECLINE;
	public static ImageView IMAGE_MESSAGE;
	public static ImageView IMAGE_NEW_MESSAGE;

	private Stage stage;
	private App app;
	BorderPane borderpane;
	Scene scene;
	Button button;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IMAGE_ACCEPT = new ImageView( new Image(getClass().getResource(
				"/resources/accept-icon.png").toURI().toString(),25,25,false,false) );
		IMAGE_DECLINE = new ImageView( new Image(getClass().getResource(
				"/resources/decline-icon.png").toURI().toString(),25,25,false,false) );
		IMAGE_MESSAGE = new ImageView( new Image(getClass().getResource(
				"/resources/message.png").toURI().toString(),50,50,false,false) );
		IMAGE_NEW_MESSAGE = new ImageView( new Image( getClass().getResource(
				"/resources/message-new.png").toURI().toString(),50,50,false,false) );

		borderpane = new BorderPane();
		this.stage = primaryStage;
		stage.setTitle("Confide");
		stage.setWidth(680);
		stage.setHeight(680);
		stage.setResizable(false);
		stage.setOnCloseRequest(we->{
			this.app.onClose();
		});

		scene = new Scene( new StackPane(), 680, 680);
		button = new Button();
		app = new App(this);

		stage.show();
	}

	public void setScreenLater(Screen statusDisplay, Screen screen){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				setScreen(statusDisplay, screen);
			}
		});
	}

	public void setScreen(Screen screen){
		scene.setRoot(screen);
		stage.setScene(scene);
		stage.show();
	}

	public void setScreenLater(Screen statusDisplay, Screen screen, Screen navigationScreen){
		Platform.runLater(new Runnable(){
			@Override public void run(){
				setScreen(statusDisplay, screen, navigationScreen);
			}
		});
	}

	public double getCenterX(){
		return stage.getX() + stage.getWidth()/2;
	}

	public double getCenterY(){
		return stage.getY() + stage.getHeight()/2;
	}

	/**
	 * Set view with stacked Screens
	 * @param screen
	 * @param navigationScreen
	 */
	public void setScreen(Screen screenTop, Screen navigationScreen){
		borderpane.setTop(screenTop);
		borderpane.setBottom(null);
		borderpane.setCenter(navigationScreen);
		scene.setRoot(borderpane);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Set view with stacked Screens
	 * @param screen
	 * @param navigationScreen
	 */
	public void setScreen(Screen screenTop, Screen screenCentre, Screen navigationScreen){
		borderpane.setTop(screenTop);
		borderpane.setCenter(screenCentre);
		borderpane.setBottom(navigationScreen);
		scene.setRoot(borderpane);
		stage.setScene(scene);
		stage.show();
	}
}
