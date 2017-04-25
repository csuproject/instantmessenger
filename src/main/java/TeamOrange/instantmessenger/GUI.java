package TeamOrange.instantmessenger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

	private Stage stage;
	private App app;
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		app = new App();
		app.init();

		this.stage = primaryStage;
		stage.setTitle("Confide");

		Scene scene = new Scene(new AccountScreen(app), 640, 480);
		stage.setScene(scene);
		stage.show();
	}

}
