package TeamOrange.instantmessenger.views;

import javafx.scene.control.Button;

public class CustomButton extends Button {

	private boolean selected;
	private boolean mouseIn;

	public CustomButton(){
		this("");
	}

	public CustomButton(String text){
		super(text);
		selected = false;
		mouseIn = false;

		this.setFocusTraversable(false);

		updateStyle();

		this.setOnMouseEntered(e->{
			mouseIn = true;
			updateStyle();
		});

		this.setOnMouseExited(e->{
			mouseIn = false;
			updateStyle();
		});
	}

	public void select(){
		selected = true;
		updateStyle();
	}

	public void unSelect(){
		selected = false;
		updateStyle();
	}

	private void updateStyle(){
		if(mouseIn){
			mouseInStyle();
		} else {
			mouseOutStyle();
		}

	}


	public void mouseOutStyle(){
		if(selected){
			this.setStyle(
				"-fx-background-color: white;"+
				"-fx-background-insets: 0,1,4,5,6;"+
				"-fx-background-radius: 9,8,5,4,3;"+
				"-fx-padding: 5 10 5 10;"+
				"-fx-font-family: \"Helvetica\";"+
				"-fx-font-size: 18px;"+
				"-fx-font-weight: bold;"+
				"-fx-text-fill: grey;"+
				"-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"+
				"-fx-border-style: solid outside;" +
                "-fx-border-width: 3;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 9,8,5,4,3;" +
                "-fx-border-color: black;"
			);
		} else {
			this.setStyle(
				"-fx-background-color: grey;"+
				"-fx-background-insets: 0,1,4,5,6;"+
				"-fx-background-radius: 9,8,5,4,3;"+
				"-fx-padding: 5 10 5 10;"+
				"-fx-font-family: \"Helvetica\";"+
				"-fx-font-size: 18px;"+
				"-fx-font-weight: bold;"+
				"-fx-text-fill: white;"+
				"-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"+
				"-fx-border-style: solid outside;" +
                "-fx-border-width: 3;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 9,8,5,4,3;" +
                "-fx-border-color: black;"
			);
		}
	}

	private void mouseInStyle(){
		if(selected){
			this.setStyle(
				"-fx-background-color: white;"+
				"-fx-background-insets: 0,1,4,5,6;"+
				"-fx-background-radius: 20;"+
				"-fx-padding: 5 10 5 10;"+
				"-fx-font-family: \"Helvetica\";"+
				"-fx-font-size: 18px;"+
				"-fx-font-weight: bold;"+
				"-fx-text-fill: black;"+
				"-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"+
				"-fx-border-style: solid outside;" +
                "-fx-border-width: 3;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 20;" +
                "-fx-border-color: black;"
			);
		} else {
			this.setStyle(
				"-fx-background-color: grey;"+
				"-fx-background-insets: 0,1,4,5,6;"+
				"-fx-background-radius: 20;"+
				"-fx-padding: 5 10 5 10;"+
				"-fx-font-family: \"Helvetica\";"+
				"-fx-font-size: 18px;"+
				"-fx-font-weight: bold;"+
				"-fx-text-fill: black;"+
				"-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"+
				"-fx-border-style: solid outside;" +
                "-fx-border-width: 3;" +
                "-fx-border-insets: 3;" +
                "-fx-border-radius: 20;" +
                "-fx-border-color: black;"
			);
		}
	}

}
