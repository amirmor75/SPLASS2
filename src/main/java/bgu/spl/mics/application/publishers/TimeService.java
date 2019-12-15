package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	/**
	 * create only one instance of this class
	 */
	private int numOfInstances=0;
	/**
	 *  the number of ticks before termination
	 *  time-tick is 100 milliseconds
	 */
	private int duration;
	/**
	 * Timer for measuring the time-ticks
	 */
	private Timer timer;

	private TimeService() {
		super("TimeService");
	}

	private TimeService(int duration){
		super("TimeService");
		this.duration=duration;
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		
	}

	@Override
	public void run() {
		// TODO Implement this
	}

}
