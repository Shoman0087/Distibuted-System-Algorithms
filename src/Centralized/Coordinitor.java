package Centralized;

public class Coordinitor {
	
	int processorID;
	Resource[] resource ;
	
	public Coordinitor() {
		// TODO Auto-generated constructor stub
		resource = new Resource[1];
		resource[0] = new Resource();
	}
	
	public Coordinitor(int numOfResources) {
		resource = new Resource[numOfResources];
		for (int i = 0 ; i < numOfResources ; i++)
			resource[i] = new Resource(i);
	}
	
	public boolean request(int resource, Processor processor) {	
		System.out.println("from processor " + processor.processorID + " resource " + resource + " is " + this.resource[resource].inUse);
		if (!this.resource[resource].inUse) {
			this.resource[resource].processorID = processor.processorID;
			this.resource[resource].timeInUse = processor.timeOfUse;
			this.resource[resource].inUse = true;
			return true;
		} else {
			return false;
		}		
	}
	
	public int increment(int time, int resource) {
		this.resource[resource].timeInUse--;
		
		// if the processor i done with resource just release it and make if free to use
		if (this.resource[resource].timeInUse == 0) {
			System.out.println("Prcessor  " + this.resource[resource].processorID 
					+ " Done with Resource " + this.resource[resource].resourceID
					+ " At time " + time);
			int id = this.resource[resource].processorID;
			this.resource[resource].inUse = false;
			this.resource[resource].processorID = -1;
			return id;
		}
		return -1;
	}
	

}
