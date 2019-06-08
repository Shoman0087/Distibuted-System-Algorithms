package application;
	
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Centralized.CentralizedAlgorithm;
import Ricart.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
//		centralizedAlgorithm(5, 1);
//		CentralizedAlgorithm c = new CentralizedAlgorithm(5, 0);
		
//		Controller controller = new Controller();
//		controller.ricartAlgorithm(5);
		
		GridPane pane = new GridPane();
		pane.setVgap(30);
		pane.setHgap(20);
		
		Label numOfProc = new Label("Number Of Processors");
		Label numOfResource = new Label("Number Of Resource");
		
		TextField numOfProcField = new TextField();
		TextField numOfResourceField = new TextField();
		
		pane.add(numOfProc, 0, 0);
		pane.add(numOfProcField, 1, 0);
		
		pane.add(numOfResource, 0, 1);
		
		pane.add(numOfResourceField, 1, 1);
		
		Label algorithm = new Label("Algorithm");
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Centralized Algorithm",
			        "Ricart Algorithm"
			    );
			 ComboBox<String> comboBox = new ComboBox(options);
			 comboBox.setValue("Centralized Algorithm");
			 
		pane.add(algorithm, 0, 2);
		pane.add(comboBox, 1, 2);
		
		Button run = new Button("Run");
		pane.add(run, 1, 3);
		
		pane.setAlignment(Pos.CENTER);
	
		run.setOnAction(e -> {
			int proc = Integer.parseInt(numOfProcField.getText());
			int resource = Integer.parseInt(numOfResourceField.getText());
			int algo = 0;
			
			String algor = comboBox.getValue();
			if (algor.equals("Centralized Algorithm"))
				algo = 0;
			else if (algor.equals("Ricart Algorithm"))
				algo = 1;
			else if (algor.equals("Ring Algorithm"))
				algo = 2;
			
			if (algo == 0) {
				CentralizedAlgorithm driver = new CentralizedAlgorithm(proc, resource);
			} else if (algo == 1) {
				Controller driver = new Controller(proc,resource);
			}
				
			
		});
		
		
		
		Scene scene = new Scene(pane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		System.out.println("done");
	}
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
