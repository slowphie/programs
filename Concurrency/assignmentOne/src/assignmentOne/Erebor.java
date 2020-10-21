package assignmentOne;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Erebor {

	public static void main(String[] args) {
		// semNotFoot is a semaphore that is acquired when the cable car is NOT at the foot at the mountain
		// This is used to signify the location of the cable car and whether the tourists can board or not
		// It is also used to block other tourists as one tourist is assigning its position on the cable car
		Semaphore semNotFoot = new Semaphore(1);
		// Same as above, semNotSummit is a semaphore that is acquired when the cable car is NOT at the summit at the mountain
		Semaphore semNotSummit = new Semaphore(1);
		
		try {
			// Both semNotFoot and semNotSummit are acquired to signify the fact that the cable car is not ready to be boarded
			semNotFoot.acquire(); 
			semNotSummit.acquire();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// There is a maximum limit of 50 people at the summit
		int summitCap = 50;
		// There is a maximum limit of 10 people on the cable car 
		int cableCap = 10;
		// The total number of tourists visiting the mountain in a day is 500 
		int totalTourists = 500;
		
		// These semaphore initialise the capacity for the cable car and summit as their number of permits
		Semaphore cable = new Semaphore(cableCap);
		Semaphore summit = new Semaphore(summitCap);		

		// Initialising and starting the cable car thread
		CableCar cableCar = new CableCar(semNotFoot,semNotSummit,cable, summit, cableCap, summitCap, totalTourists);
		Thread cableThread = new Thread(cableCar);
		cableThread.start();
		
		// There are 500 threads generated to simulated 500 individual people
		Tourist tourists[] = new Tourist[totalTourists];
		Thread touristThread[] = new Thread[totalTourists];
		for(int i = 0; i < totalTourists; i++) {
			// This simulated a random arrival time of the next tourist
			try {
				Thread.sleep(new Random().nextInt(150) + 1);
			} catch (InterruptedException e1) {}
			
			// Initialising and starting a new tourist thread
			tourists[i] = new Tourist(semNotFoot,semNotSummit,cable, summit);
			touristThread[i] = new Thread(tourists[i]);
			touristThread[i].start();	 
		}	
	}
}
