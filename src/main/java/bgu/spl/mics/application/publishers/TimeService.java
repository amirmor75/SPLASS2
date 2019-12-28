package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.TerminationBroadCast;
import bgu.spl.mics.TimeBroadCast;


/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TimeBroadCast Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	/**
	 *  the number of ticks before termination
	 *  time-tick is 100 milliseconds
	 */
	private int duration;
	private int currentDuration;


	public TimeService() {
		super("TimeService");
	}

	public TimeService(int duration){
		super("TimeService");
		this.duration=duration;
	}

	@Override
	protected void initialize() {
		// TODO Implement this
	}

	@Override
	public void run() {
		while(currentDuration<duration) {
			try {
				Thread.sleep(100);
				getSimplePublisher().sendBroadcast(new TimeBroadCast(currentDuration));
				System.out.println("current time tick: "+currentDuration);
				currentDuration = currentDuration + 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Terminating...");
		getSimplePublisher().sendBroadcast(new TerminationBroadCast());
	}

}
