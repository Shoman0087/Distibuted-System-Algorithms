package Ricart;

import java.util.LinkedList;
import java.util.Queue;

public class Processor {
	
	int processorID;
	boolean[] replys;
	Queue<Integer> onWait;
	int resourceID;
	int requestTime;
	int processTime;
	boolean inUse;
	boolean done;
	boolean inQueue;
	static int messagesCount = 0;
	
	public Processor() {
		// TODO Auto-generated constructor stub
		onWait = new LinkedList<>();
	}
	
	public Processor(int processorID, int resourceID, int requestTime, int processTime, int numberOfProcessors) {
		this.processorID = processorID;
		this.resourceID = resourceID;
		this.requestTime = requestTime;
		this.processTime = processTime;
		onWait = new LinkedList<>();
		replys = new boolean[numberOfProcessors];
	}
	
	public boolean check() {
		for (int i = 0 ; i < this.replys.length ; i++) {
			if (this.replys[i] == false)
				return false;
		}
		return true;
	}
	
	public void sendRequestToAll(Processor[] processors, int timer) {
		for (int i = 0 ; i < processors.length ; i++) {
			messagesCount++;
			boolean ok = processors[i].send(this.resourceID, this, timer);
			if (ok)
				this.replys[i] = true;
			else {
				this.replys[i] = false;
			}
		}
	}
	
	public void sendOK(Processor p) {
		p.replys[this.processorID] = true;
	}
	
	public boolean send(int resourceID, Processor p, int timer) {
		return reply(resourceID, p, timer);
	}
	
	public boolean reply(int resourceID, Processor p, int timer) {
		if (this.processorID == p.processorID)
			return true;
		if (this.resourceID == resourceID) {
			if (!this.inUse && this.requestTime != timer && !this.inQueue)
				return true;
			else if (!this.inUse && this.requestTime == timer) {
				int min = Math.min(this.processTime,p.processTime);
				if (min == p.processTime)
					return true;
				else {
					this.onWait.add(p.processorID);
					p.inQueue = true;
					return false;
				}
			}else if (this.inUse) {
				this.onWait.add(p.processorID);
				p.inQueue = true;
				return false;
			} else if (this.inQueue) {
				this.onWait.add(p.processorID);
				p.inQueue = true;
				return false;
			}
		}
		return true;
	}
	
	public int increment(Processor[] processors) {
		if (this.inUse) {
			this.processTime--;
			if (this.processTime == 0) {
				while (!this.onWait.isEmpty()) {
					int id = this.onWait.poll();
					messagesCount++;
					sendOK(processors[id]);
					processors[id].inQueue = false;
					for (int i = 0 ; i < processors[id].replys.length ; i++) {
						if (processors[id].replys[i] == false)
							processors[id].inQueue = true;
					}
				}
				System.out.println("test from " + this.processorID);
				this.inUse = false;
				this.done = true;
				return this.processorID;			
			}
		}
		return -1;
	}

}
