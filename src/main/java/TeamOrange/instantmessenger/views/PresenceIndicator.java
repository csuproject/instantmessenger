package TeamOrange.instantmessenger.views;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class PresenceIndicator {

	public PresenceIndicator(String userName){
		Circle indicatorCircle = new Circle(0,0,5);
		indicatorCircle.setFill(Color.LIGHTGRAY);
	}

}
