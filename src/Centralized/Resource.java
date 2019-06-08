package Centralized;

public class Resource {
	int resourceID;
	boolean inUse;
	int processorID;
	int timeInUse;
	
	public Resource() {
		// TODO Auto-generated constructor stub
	}
	
	public Resource(int id) {
		this.resourceID = id;
	}
	
	public void use(int processorID, int timeInUse) {
		this.processorID = processorID;
		this.timeInUse = timeInUse;
	}
}
