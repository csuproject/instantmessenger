package TeamOrange.instantmessenger.views;

import TeamOrange.instantmessenger.App;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiBase extends Application {
	private Stage stage;
	private Screen currentScreen;
	private App app;

	Scene accountScene;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		app = new App(this);
		app.init();

		this.stage = primaryStage;
		stage.setTitle("Confide");

		accountScene = new Scene( app.getAccountScreen(), 640, 480 );
		setScreen(Screen.ACCOUNT);

		stage.show();
	}

	public void setScreen(Screen screen){
		this.currentScreen = screen;
		switch(currentScreen){
			case ACCOUNT:
			{
				stage.setScene(accountScene);
			} break;
		}
	}
}
