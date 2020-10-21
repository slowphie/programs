package assignmentOne;

import java.util.concurrent.Semaphore;

public class CableCar implements Runnable{
	
	int total = 0; // Total passengers taken a full trip round
	int capacity, summitCapacity, totalTourists;
	int currentLoad = 0; // The current amount of passenger the cable car is carrying
	Semaphore semNotFoot, semNotSummit, cable, summit;
	
	
	
	CableCar(Semaphore sNotFoot, Semaphore sNotSummit, Semaphore cable, Semaphore summit, int cap, int sumCap, int totTou){
		this.semNotFoot = sNotFoot; // Acquired when cable car not at the foot 
		this.semNotSummit = sNotSummit; // Acquired when cable car not at the summit
		this.cable = cable; // Has a number of permits available equal to the cable car capacity
		this.summit = summit; // Has a number of permits available equal to the capacity of the summit
		this.capacity = cap; // The capacity of the cable car
		this.summitCapacity = sumCap; // The capacity of the summit 
		this.totalTourists = totTou; // The total number of tourists that equates to the end of a day
	}


	public void run() {
		
		semNotFoot.release(); // The cable car arrives at the base of the mountain
		try {
			Thread.sleep(500); // Waiting for tourists thread to begin being created
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (total<totalTourists) {
			// Continuously loops until total tourists for a day is met 
			// Data of one round trip is printed in blocks to easily see what happens during that trip
			System.out.println("The cable car arrives at the foot of the mountain");
			try {
				Thread.sleep(100); //Waits for tourists to board at the foot of the mountain
				
				semNotFoot.acquire(); // Leaves the foot of the mountain
				Thread.sleep(50); // Simulating half of the travelling time to the summit
				currentLoad = capacity - cable.availablePermits();
				System.out.println("The cable car leaves with " + currentLoad +" passengers to the summit of the mountain");
				Thread.sleep(50); // Still travelling to the summit
				
				cable.release(currentLoad); // Releasing the number of permits the passengers have acquired on the cable car
				currentLoad = 0;
				semNotSummit.release(); // Arrives at summit
				System.out.println("The cable car arrives at the summit of the mountain");
				if ((summitCapacity - summit.availablePermits())<summitCapacity) {
					System.out.println("There are " + (summitCapacity - summit.availablePermits()) + " tourists are currently at the summit");
				}else {
					// This is when the summit is at capacity and no more tourists can travel fromt he foot to the summit, they can only leave the mountain
					System.out.println("Summit at capacity: " + (summitCapacity - summit.availablePermits()));
				}

				
				Thread.sleep(100); // Waits for tourists to board at summit
				

				semNotSummit.acquire(); // Leaves the summit station
				
				currentLoad = capacity - cable.availablePermits();
				System.out.println("The cable car leaves with " + currentLoad +" passengers to the foot of the mountain");
				
				if (currentLoad > 0) {
					System.out.println("There are now " + (summitCapacity - summit.availablePermits() - currentLoad) + " tourists at the summit");
				}
				
				Thread.sleep(100); // Travels to foot
				
				total+=currentLoad; // The total tourists are those that have done a round trip, therefore increases by its load travelling to the foot
				cable.release(currentLoad); // Releases the number of permits the tourists acquired while travelling to the foot
				summit.release(currentLoad); // The number that leave the mountain release their position at the summit 
				currentLoad = 0;
				semNotFoot.release(); // Now at the foot, ready for tourists to board
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Total: " + total);
			System.out.println("");
			// One round trip has been completed
		
		}
		
		System.out.println(totalTourists + " people have visited the mountain, Erebor is now closed to visitors");
		
	}

}
