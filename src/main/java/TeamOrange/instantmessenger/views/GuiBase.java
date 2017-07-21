package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.App;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
	BorderPane borderpane;
	Scene scene;
	Button button;
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		borderpane = new BorderPane();
		scene = new Scene( new StackPane(), 400, 600);
		this.stage = primaryStage;
		stage.setTitle("Confide");
		button = new Button();
		app = new App(this);

		stage.show();
	}

	public void setScreen(Screen screen){
		scene.setRoot(screen);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Set view with stacked Screens
	 * @param screen
	 * @param navigationScreen
	 */
	public void setScreen(Screen screen, Screen navigationScreen){
		borderpane.setTop(screen);
		borderpane.setBottom(navigationScreen);
		scene.setRoot(borderpane);
		stage.setScene(scene);
		stage.show();
	}
}
