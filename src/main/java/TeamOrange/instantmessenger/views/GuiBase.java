package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.App;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This is the starting point for the application
 * It creates the javafx stage, and changes the screen
 *
 */
public class GuiBase extends Application {
	private Stage stage;
	private App app;

	Scene scene;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		scene = new Scene( new StackPane(), 640, 480);
		this.stage = primaryStage;
		stage.setTitle("Confide");

		app = new App(this);


		stage.show();
	}

	public void setScreen(Screen screen){
		scene.setRoot(screen);
		stage.setScene(scene);
		stage.show();
	}
}
