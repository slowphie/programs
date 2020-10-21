package assignmentOne;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* NOTE: The assignment brief states to use one thread for the tourists going up and one thread for the tourists going down.
 * I believed it was more logical to use one tourist thread and create 500 instances of it, as this simulated the 500 people
 * individually on their trip up and down the mountain and deciding when to board the cable car. Hans assured me doing it 
 * this way should be an acceptable way of completing this assignment, therefore this is the way I have submitted it. 
 */



public class Tourist implements Runnable {
	Semaphore semNotFoot, semNotSummit, cable, summit; 
	boolean visited = false;
	
	
	Tourist(Semaphore sNotFoot, Semaphore sNotSummit, Semaphore cable, Semaphore summit){
		this.semNotFoot = sNotFoot; // Acquired when cable car not at the foot 
		this.semNotSummit = sNotSummit; // Acquired when cable car not at the summit
		this.cable = cable; // Has a number of permits available equal to the cable car capacity
		this.summit = summit; // Has a number of permits available equal to the capacity of the summit
	}

	public void run() {
		// A print statement would be placed here (as outlined in the marking scheme) to signify a tourist has arrived at the base of the mountain
		// I have commented this out as there are 500 tourists arriving and this causes the general workings of the cable car to be hard to read
		//System.out.println("A tourist arrives at the base station of the mountain");
		
		while (!visited) {		
			try {
				if (semNotFoot.tryAcquire(100, TimeUnit.MILLISECONDS)) {
					// Acquired now the cable car is at the foot of the mountain
					// semNotFoot will remain acquired by this tourist thread while assigning it's positions at the summit and on the cable car
					try {
						if (summit.tryAcquire(100, TimeUnit.MILLISECONDS)){
							if(cable.tryAcquire(100, TimeUnit.MILLISECONDS)) {
								// semNotFoot is release as positions have now been acquired and another tourist is now free to do the same	
								semNotFoot.release();
								try {
									Thread.sleep(new Random().nextInt(2000 + 1)  + 500); // Looking around at the summit for a random amount of time
								} catch (InterruptedException e1) {}
								
								//System.out.println("A tourist arrives at the summit station");
								while(!visited) {
									// Will continuously try to get on the cable car now they are finished looking around at the summit
									if(semNotSummit.tryAcquire(100, TimeUnit.MILLISECONDS)) {
										// As above, semNotSummit is acquired as it assigns it's position on the cable car
										if(cable.tryAcquire(100, TimeUnit.MILLISECONDS)) {
											// Position on the cable car has been successful and can now allow another tourist to do the same
											semNotSummit.release();
											
											while(!visited) {
												// Uses the visited variable again to continuously check for when the cable car reaches the foot of the mountain
												// It will reach the foot when semNotFoot becomes available (is released by the cable car)
												if(semNotFoot.availablePermits()>0) {
													// Cable car is at the bottom and the tourist has completed their journey
													visited = true;
												}
											}
										} else {
											// There were no free positions on the cable car and therefore semNotSummit is released for the cable car to acquire it and leave the summit
											semNotSummit.release();
										}
									}
									
								}
							}else {
								summit.release(); // Acquired top position but not enough room on cable car so it is unassigning it's position 
								semNotFoot.release(); // The tourist has finished trying to acquire it's positions
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
