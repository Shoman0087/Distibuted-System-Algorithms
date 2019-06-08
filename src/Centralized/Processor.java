package Centralized;

public class Processor {
	int processorID;
	boolean inUse;
	int requestTime;
	int responseTime;
	int timeOfUse;
	boolean inQueue;
	int numberOfMessages;
	int resourceInUse;
	int resourceToUse;
	int timeInQueue;
	
	public void run() {
		System.out.println("Processor " + this.processorID + " started use " + this.resourceInUse);
		while (timeOfUse != 0) {
			timeOfUse--;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Processor " + this.processorID + " is done with " + this.resourceInUse);
	}
	
}
