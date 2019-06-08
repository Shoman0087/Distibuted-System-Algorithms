package Ricart;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Message {
	Rectangle rec;
	int xLoc;
	int yLoc;
	int toX;
	int toY;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(int xLoc, int yLoc) {
		this.rec = new Rectangle(20,10,Color.BLUE);
		this.rec.setLayoutX(xLoc);
		this.rec.setLayoutY(yLoc);
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	

}
