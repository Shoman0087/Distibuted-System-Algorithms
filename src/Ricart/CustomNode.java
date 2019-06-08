package Ricart;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class CustomNode {
	Circle circle;
	StackPane pane;
	int xLoc;
	int yLoc;
	int toX;
	int toY;
	Processor proc;
	
	public CustomNode() {
		// TODO Auto-generated constructor stub
		circle = new Circle(30);
		
	}
	
	public CustomNode(int x, int y, Processor proc) {
		circle = new Circle(x,y,30);
		pane = new StackPane();
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.RED);
		Text text = new Text("P" + proc.processorID);
		pane.getChildren().addAll(circle,text);
		pane.setLayoutX(x);
		pane.setLayoutY(y);
		this.proc = proc;
		this.xLoc = x;
		this.yLoc = y;
	}
}
