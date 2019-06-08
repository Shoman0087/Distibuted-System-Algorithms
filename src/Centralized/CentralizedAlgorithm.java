package Centralized;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

public class CentralizedAlgorithm {

	Stage myStage = new Stage();
	StackPane rootOfRoot = new StackPane();
	BorderPane root = new BorderPane();
	ArrayList<Circle> processorsList = new ArrayList<>();
	ArrayList<StackPane> stackList = new ArrayList<>();
	ArrayList<VBox> queueList = new ArrayList<>();
	ArrayList<Text> coordTimerList = new ArrayList<>();
	ArrayList<Text> elapsedTime = new ArrayList<>();

	
	public CentralizedAlgorithm(int numberOfProcessors, int numberOfResources) {
		// TODO Auto-generated constructor stub
		
		
		rootOfRoot.getChildren().add(root);
		Scene scene = new Scene(rootOfRoot, 1000, 800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		myStage.setScene(scene);
		myStage.show();
		centralizedAlgorithm(numberOfProcessors, numberOfResources);
		
		
	}
	
	public void centralizedAlgorithm(int numberOfProcessors, int numberOfResources) {
		Processor[] processors = new Processor[numberOfProcessors-1];
		
		
		// the last processor is the coordinator
		
		Coordinitor coord = new Coordinitor(numberOfResources);
		
		// initialize the processors
		for (int i = 0 ; i < numberOfProcessors-1 ; i++) {
			processors[i] = new Processor();
			processors[i].processorID = i;
		}
		
		int[] times = generateDifferentRandomlyTimes(numberOfProcessors-1);
		int[] resources = generateResourcesToUse(numberOfResources,numberOfProcessors-1);
		// give each processor different randomly time
		for (int i = 1 ; i < numberOfProcessors-1 ; i++) {
			processors[i].requestTime = times[i];		
		}
		
		for (int i = 0 ; i < numberOfProcessors-1 ; i++)
			processors[i].resourceToUse = resources[i];
		
		processors[0].requestTime = 1;
		
		for (int i = 0 ; i < numberOfProcessors - 1 ; i++) {
			processors[i].timeOfUse = 5 + (int)(Math.random() * 15);
		}
		
		Queue<Processor>[] queue = new Queue[numberOfResources];
		for (int i = 0 ; i < numberOfResources ; i++)
			queue[i] = new LinkedList<>();
		
		queue[processors[0].resourceToUse].add(processors[0]);
		processors[0].inQueue = true;
		
		
		
		/* UI work */
		ArrayList<VBox> processorsBox = new ArrayList<>();
		HBox topBox = new HBox(30);
		topBox.setAlignment(Pos.CENTER);
		topBox.setPadding(new Insets(30, 0, 0, 0));
		for (int i = 0 ; i < numberOfProcessors-1 ; i++) {
			VBox vbox = new VBox(10);
			Circle circle = new Circle();
			processorsList.add(circle);
			circle.setRadius(30);
			circle.setStroke(Color.RED);
			Text text = new Text("P" + i);
			text.setBoundsType(TextBoundsType.VISUAL);
			StackPane stack = new StackPane();
			stack.getChildren().addAll(circle, text);
			stackList.add(stack);
			circle.setFill(Color.WHITE);
			Text rt = new Text("Req. : " + processors[i].requestTime + "ms");
			Text pt = new Text("Proc. : " + processors[i].timeOfUse + "ms");
			Text ru = new Text("Resource. : " + processors[i].resourceToUse);
			Text et = new Text("Elap. Time. : " + "∞");
			elapsedTime.add(et);
			vbox.getChildren().addAll(stack,rt,pt,ru,et);
			processorsBox.add(vbox);
		}
		topBox.getChildren().addAll(processorsBox);
		root.setTop(topBox);
				
		
		StackPane coordinator = new StackPane();
		Circle cir = new Circle();
		cir.setRadius(70);
		cir.setStroke(Color.BLACK);
		cir.setFill(Color.WHITE);
		cir.setStrokeWidth(10);
		HBox hb = new HBox(30);
		Text t1 = new Text("Coordinator 0");
		Text t2 = new Text("Mutex Processor : ");
		Text t3 = new Text("Time : ");
		VBox vb = new VBox(20);
		vb.getChildren().addAll(t2,t3);
		vb.setAlignment(Pos.CENTER_LEFT);
		coordinator.getChildren().addAll(cir,t1);
		
		hb.getChildren().addAll(coordinator,vb);
		hb.setAlignment(Pos.CENTER);
		root.setCenter(hb);
		
		
		HBox bottomBox = new HBox(60);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setPadding(new Insets(0,50,0,0));
		for (int i = 0 ; i < numberOfResources ; i++) {
			VBox myQueue = new VBox();
			VBox queueWraper = new VBox(20);
			queueList.add(myQueue);
			myQueue.setAlignment(Pos.BOTTOM_CENTER);
			myQueue.setMinWidth(80);
			myQueue.setMinHeight(200);
			myQueue.setMaxWidth(80);
			myQueue.setMaxHeight(200);
			myQueue.setBorder(new Border(new BorderStroke(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE,
		            BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
		            CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY)));
			Text resID = new Text("Resource " + i);
			queueWraper.getChildren().addAll(myQueue,resID);
			bottomBox.getChildren().add(queueWraper);
			
		}
		root.setBottom(bottomBox);
		
		StackPane help = new StackPane();
		
		help.setMinWidth(200);
		help.setPadding(new Insets(100,50,0,0));
		
		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);
		Circle green = new Circle();
		green.setRadius(30);
		green.setFill(Color.LIGHTGREEN);
		Text ht1 = new Text("Hold the Resource ");
		h1.getChildren().addAll(green,ht1);
		
		HBox h2 = new HBox(10);
		h2.setAlignment(Pos.CENTER);
		Circle gray = new Circle();
		gray.setRadius(30);
		gray.setFill(Color.DARKGRAY);
		Text ht2 = new Text("Done with Resource");
		h2.getChildren().addAll(gray,ht2);
		
		HBox h3 = new HBox(10);
		h3.setAlignment(Pos.CENTER);
		Circle orange = new Circle();
		orange.setRadius(30);
		orange.setFill(Color.ANTIQUEWHITE);
		Text ht3 = new Text("  Wait in Queue       ");
		h3.getChildren().addAll(orange,ht3);
		
		VBox helpVbox = new VBox(30);
		helpVbox.getChildren().addAll(h1,h2,h3);
		
		help.getChildren().add(helpVbox);
		
		
		
		root.setRight(help);
		
		/* done with UI work */
		processorsList.get(0).setFill(Color.ANTIQUEWHITE);
		for (int i = 0 ; i < numberOfProcessors-1 ; i++) {
			System.out.println("Processor " + processors[i].processorID + " request at time " + processors[i].requestTime 
					+ " And need " + processors[i].timeOfUse + " Resource to use " + processors[i].resourceToUse);
		}
		
		StackPane sp = new StackPane();
		Rectangle rec = new Rectangle(90, 50, Color.WHITE);
		Text pt = new Text("P" + 0);
		pt.setBoundsType(TextBoundsType.VISUAL);
		sp.getChildren().addAll(rec,pt);
		
		queueList.get(processors[0].resourceToUse).getChildren().add(sp);
		
		
		Task task = new Task<Void>() {
		    @Override
		    public Void call() throws Exception {
		    	int usedProcessors = 0;
			int timer = 1;
			boolean allResourceDone = false;
				while (usedProcessors != numberOfProcessors-1 || !allResourceDone) {
					t3.setText("Time : " + timer);
					for (int i = 0 ; i < numberOfResources ; i++) {
						int finishID = coord.increment(timer,i);
						if (finishID != -1) {
							processorsList.get(finishID).setFill(Color.DARKGRAY);
						}
					}
				for (int i = 0 ; i < numberOfResources ; i++) {
					System.out.println("queue " + i + " at Time " + timer);
					if (!queue[i].isEmpty()) {
						Processor p = queue[i].peek();
						System.out.println("Proc " + p.processorID + " check from queue " + i + " at time " + timer);
						if (coord.request(p.resourceToUse, p)) {
							System.out.println("Processor  " + p.processorID + " Hold Resource "+ p.resourceToUse +"At time " + timer);
							
							processorsList.get(p.processorID).setFill(Color.LIGHTGREEN);
							usedProcessors++;
							queue[i].poll();
							Platform.runLater(
									  () -> {
										  queueList.get(p.resourceToUse).getChildren().remove(queueList.get(p.resourceToUse).getChildren().size()-1);
									  }
									  );							
							p.responseTime = timer;
							p.resourceInUse = p.resourceToUse;
							p.inQueue = false;
							p.inUse = true;
							elapsedTime.get(p.processorID).setText("Elap. Time. " + (p.responseTime - p.timeInQueue));
						}
					}
					
					
				}
				if (checkRequests(processors, timer) != -1) {
					int processorId = checkRequests(processors, timer);

					if (coord.request(processors[processorId].resourceToUse, processors[processorId])) {
						elapsedTime.get(processorId).setText("Elap. Time. " + 0);
						System.out.println("Processor  " + processorId + " Hold Resource "+ processors[processorId].resourceToUse + "At time " + timer);
						processorsList.get(processorId).setFill(Color.LIGHTGREEN);
						usedProcessors++;
						processors[processorId].responseTime = timer;
						processors[processorId].resourceInUse = processors[processorId].resourceToUse ;
						processors[processorId].inQueue = false;
						processors[processorId].inUse = true;
					} else if (!processors[processorId].inQueue && !processors[processorId].inUse) {
						System.out.println("Processor  " + processorId + " is added to the queue at time " + timer);
						processorsList.get(processorId).setFill(Color.ANTIQUEWHITE);
						queue[processors[processorId].resourceToUse].add(processors[processorId]);
						processors[processorId].inQueue = true;	
						processors[processorId].timeInQueue = timer;
						StackPane spp = new StackPane();
						Rectangle recc = new Rectangle(70, 30, Color.WHITE);
						Text ptt = new Text("P" + processorId);
						
						
						spp.getChildren().addAll(recc,ptt);
						Platform.runLater(
								  () -> {
									  queueList.get(processors[processorId].resourceToUse).getChildren().add(0,spp);
								  }
								);

					}
				}	
					timer++;
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					allResourceDone = true;
					for (int j = 0 ; j < numberOfResources ; j++) {
						if (coord.resource[j].inUse == true)
							allResourceDone = false;
					}
					
				}
				return null;
		    }
		};
		Thread th = new Thread(task);
		th.start();
			
	}
	
	public int[] generateResourcesToUse(int numOfResources, int n) {
		int[] arr = new int[n];
		for (int i = 0 ; i < n ; i++) {
			int rand = (int) (Math.random() * numOfResources);
			arr[i] = rand;
		}
		return arr;
	}
	
	public int checkRequests(Processor[] processors, int time) {
		for (int i = 1 ; i  < processors.length ; i++) {
			if (processors[i].requestTime == time)
				return i;
		}
		return -1;
	}
	
	public int[] generateDifferentRandomlyTimes(int n) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
		    arr[i] = 2 + (int)(Math.random()*n * 2);//note, this generates numbers from [0,9]

		    for (int j = 0; j < i; j++) {
		        if (arr[i] == arr[j]) {
		            i--; //if a[i] is a duplicate of a[j], then run the outer loop on i again
		            break;
		        }
		    }  
		}
		return arr;
	}
}
