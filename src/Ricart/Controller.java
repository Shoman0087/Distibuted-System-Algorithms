package Ricart;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {
	
	Pane centerPane;
	BorderPane root;
	int width = 1200;
	int height = 800;
	CustomNode nod ;
	CustomNode[] currentWork;
	ArrayList<Message> messageList = new ArrayList<>();
	ArrayList<HBox> queueList = new ArrayList<>();
	ArrayList<VBox> infoList = new ArrayList<>();
	Text timerText = new Text("Timer : ");
	Text MessagesCount = new Text("Number Of Messages : 0");
	int currentInProcess = 0;
	
	public Controller(int numberOfProcessors, int numberOfResources) {
		// TODO Auto-generated constructor stub
		
		centerPane = new Pane();
		centerPane.setId("pane");
		centerPane.setMaxHeight(400);
		centerPane.setMaxWidth(400);
		centerPane.setMinHeight(400);
		centerPane.setMinWidth(400);

		Scene scene = new Scene(centerPane,1200,800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		currentWork = new CustomNode[numberOfProcessors];
		ricartAlgorithm(numberOfProcessors,numberOfResources);
	}
	
	
	
	public void ricartAlgorithm(int numberOfProcessors, int numberOfResources) {
		
		CustomNode[] myProcessors = new CustomNode[numberOfProcessors];
		
		
		Processor[] processors = new Processor[numberOfProcessors];
		int[] requTimes = new int[numberOfProcessors];
		requTimes[0] = 0;

		for (int i = 1 ; i < numberOfProcessors ; i++) {
			requTimes[i] = (int)(Math.random() * 10);
		}
		int[] processTime = new int[numberOfProcessors];
	
		for (int i = 0 ; i < numberOfProcessors ; i++) {
			processTime[i] = 1 + (int)(Math.random() * 15);
		}
		
		int[] resources = new int[numberOfProcessors];
		
		for (int i = 0 ; i < numberOfProcessors ; i++) {
			resources[i] = (int) (Math.random() * numberOfResources);
			
		}
		
		for (int i = 0 ; i< numberOfProcessors ; i++) {
			processors[i] = new Processor(i, resources[i], requTimes[i], processTime[i], numberOfProcessors);
		}
		
		int localX = width - (width/4);
		int localY = 0;
		int space = height / (numberOfProcessors+2);
		
		timerText.setLayoutX(100);
		timerText.setLayoutY(70);
		timerText.setId("timer");
		
		MessagesCount.setLayoutX(300);
		MessagesCount.setLayoutY(70);
		MessagesCount.setId("timer");
		
		centerPane.getChildren().addAll(timerText,MessagesCount);
		
		
		
		for (int i = 0 ; i < numberOfProcessors ; i++) {
			localY += space;
			myProcessors[i] = new CustomNode(localX,localY,processors[i]);
			centerPane.getChildren().add(myProcessors[i].pane);
			
			VBox infoBox = new VBox(5);
			Text reqT = new Text("Req : " + processors[i].requestTime);
			Text procT = new Text("Pro : " + processors[i].processTime);
			Text res = new Text("Res : " + processors[i].resourceID);
			infoBox.getChildren().addAll(reqT,procT,res);
			infoBox.setLayoutX(localX - 80);
			infoBox.setLayoutY(localY);
			centerPane.getChildren().add(infoBox);
			infoList.add(infoBox);
			
			HBox queue = new HBox(10);
			queue.setMinHeight(40);
			queue.setMaxHeight(40);
			queue.setMinWidth(150);
			queue.setMaxWidth(150);
			queue.setBorder(new Border(new BorderStroke(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE,
		            BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
		            CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY)));
			queueList.add(queue);
			queue.setLayoutX(localX + 80);
			queue.setLayoutY(localY+10);
			centerPane.getChildren().add(queue);
		}
		
		for (int i = 0 ; i < processors.length ; i++) {
			System.out.println("Processor " + i + " Request Time " + processors[i].requestTime
					+ " Need " + processors[i].processTime);
		}
		Task task = new Task<Void>() {
		    @Override
		  public Void call() throws Exception {
		    	boolean allDone = false;
				int timer = 0;
		while (!allDone){
			int tt = timer;
			Platform.runLater(() -> {
				timerText.setText("Timer : " + tt);
			});
			
			ArrayList<Processor> list = checkSenders(timer, processors);
			for (int i = 0 ; i < processors.length ; i++) {
				int status = processors[i].increment(processors);		
				Platform.runLater(() -> {
					MessagesCount.setText("Number Of Messages : "  + processors[0].messagesCount);
				});
				if (status != -1) {
						Platform.runLater(() -> {
							
							myProcessors[status].circle.setFill(Color.LIGHTGRAY);
							
							centerPane.getChildren().remove(currentWork[status].pane);
							queueList.get(status).getChildren().clear();
						});				
				}
			}
			
			
			
			for (int i = 0 ; i < list.size() ; i++) {
				list.get(i).sendRequestToAll(processors, timer);
				Platform.runLater(() -> {
					MessagesCount.setText("Number Of Messages : "  + processors[0].messagesCount);
				});
				int ii = i;
				Platform.runLater(
						  () -> {
							  if (nod != null) {
								  centerPane.getChildren().remove(nod.pane);
							  }
							  
							  myProcessors[list.get(ii).processorID].circle.setFill(Color.BURLYWOOD);
							  
							 nod = new CustomNode(myProcessors[list.get(ii).processorID].xLoc, 
									 myProcessors[list.get(ii).processorID].yLoc,
									 myProcessors[list.get(ii).processorID].proc);
							 
							centerPane.getChildren().add(nod.pane);
							TranslateTransition tran = new TranslateTransition();
							tran.setDuration(Duration.seconds(2));
							tran.setNode(nod.pane);
							tran.setToX(-width/2);
							tran.setToY((space * (numberOfProcessors+1) / 2) - nod.yLoc);
							nod.toX = nod.xLoc + (-width/2);
							nod.toY = nod.yLoc + ((space * (numberOfProcessors+1) / 2) - nod.yLoc);
							tran.play();
						  }
						  );
				
				Thread.sleep(3000);
				messageList = new ArrayList<>();
				for (int j = 0 ; j < numberOfProcessors; j++) {
					
//					Rectangle rec = new Rectangle(20, 10, Color.RED);
					
					int myX = nod.toX;
					int myY = nod.toY+20;
					Message message = new Message(myX,myY);
					messageList.add(message);
		
					int jj = j;
					Platform.runLater(() -> {
						System.out.println("kaka");
						centerPane.getChildren().add(message.rec);
						TranslateTransition tran = new TranslateTransition();
						tran.setDuration(Duration.seconds(2));
						tran.setNode(message.rec);
						tran.setToX(myProcessors[jj].xLoc - myX);
						tran.setToY(myProcessors[jj].yLoc - myY + 20);
						message.toX = message.xLoc + (myProcessors[jj].xLoc - myX);
						message.toY = message.yLoc + (myProcessors[jj].yLoc - myY + 20);
						tran.play();
					});
				}
				Thread.sleep(3000);

				
				for (int j = 0 ; j < numberOfProcessors ; j++) {
					int jj = j;
					Platform.runLater(() -> {
						TranslateTransition tran = new TranslateTransition();
						
						if (list.get(ii).replys[jj] == true)
							messageList.get(jj).rec.setFill(Color.GREEN);
						else {
							messageList.get(jj).rec.setFill(Color.RED);
							
							StackPane queuedProc = new StackPane();
							Text procNum = new Text("P" + list.get(ii).processorID);
							Rectangle procRec = new Rectangle(30, 30, Color.BISQUE);
							queuedProc.getChildren().addAll(procRec,procNum);
							queueList.get(jj).getChildren().add(0,queuedProc);
						}
						
						tran.setDuration(Duration.seconds(2));
						tran.setNode(messageList.get(jj).rec);
						tran.setToX(0);
						tran.setToY(0);
						tran.play();
					});
				}
				Thread.sleep(3000);
				
				for (int j = 0 ; j < numberOfProcessors ; j++) {
					int jj = j;
					Platform.runLater(() -> {
					centerPane.getChildren().remove(messageList.get(jj).rec);
					});
				}
				
				Platform.runLater(() ->{
					centerPane.getChildren().remove(nod.pane);
				});
				
				
			}
			
			
			
			for (int i = 0 ; i < processors.length ; i++) {
				if (processors[i].check() && !processors[i].done && !processors[i].inUse) {
					currentInProcess++;
					int ii = i;
					Text elapT = new Text("E : " + (timer - processors[i].requestTime));
					Platform.runLater(() -> {
						infoList.get(ii).getChildren().add(elapT);
					});
					
					
					
					myProcessors[i].circle.setFill(Color.GREENYELLOW);
					
					processors[i].inUse = true;		
					
					Platform.runLater(() -> {
						System.out.println("kalalal " + ii);
						currentWork[ii] = new CustomNode(myProcessors[processors[ii].processorID].xLoc,
								myProcessors[processors[ii].processorID].yLoc, 
								myProcessors[processors[ii].processorID].proc);
						
						currentWork[ii].circle.setFill(Color.GREENYELLOW);
						
						centerPane.getChildren().add(currentWork[ii].pane);
						TranslateTransition tran = new TranslateTransition();
						tran.setDuration(Duration.seconds(2));
						tran.setNode(currentWork[ii].pane);
						tran.setToX(-(width - (width/2)) + (currentInProcess * 60) - 130);
						tran.setToY(space - currentWork[ii].yLoc);
						tran.play();
					});
					Thread.sleep(3000);
					
					
				}
			}
			timer++;
			allDone = true;
			for (int i = 0 ; i < processors.length ; i++) {
				if (!processors[i].done)
					allDone = false;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
		    }
		};
		Thread th = new Thread(task);
		th.start();
		
		
		
	}
	
	public ArrayList<Processor> checkSenders(int timer, Processor[] processors) {
		ArrayList<Processor> list = new ArrayList<>();
		for (int i = 0 ; i < processors.length ; i++) {
			if (processors[i].requestTime == timer)
				list.add(processors[i]);
		}
		return list;
	}
	
	


}
