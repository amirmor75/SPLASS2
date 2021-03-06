package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private int currentDuration;

	public Q() {
		super("Q");
		currentDuration=0;
	}

	private void subscriveToTimeBroadCast(){
		//This callback update the current duration of the program when a timeBroadCast received.
		Callback<TimeBroadCast> timeCall=(TimeBroadCast e)->{
			if(Thread.currentThread().isInterrupted())
				terminate();
			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);
	}

	private void subscribeToGadgetAvailable(){
		Callback<GadgetAvailableEvent> callback=(GadgetAvailableEvent e)->{
			if(Thread.currentThread().isInterrupted())
				terminate();
			else {
				System.out.println("Q got GadgetAvailableEvent");
				String gadget = e.getEventInformation();//requested gadget
				boolean availToMe = Inventory.getInstance().getItem(gadget);//checks if the gadget available, returns true and remove it from list if so
				FutureResult<Integer, Integer> result = new FutureResult<>(availToMe, currentDuration, currentDuration); //The time-tick in which Q receives a GadgetAvailableEvent will be printed in the report
				complete(e, result);//resolves the event
				if (Thread.currentThread().isInterrupted()) {
					complete(e, result);//resolves the event
					terminate();
				} else {
					complete(e, result);//resolves the event
					System.out.println("Q need to find gadget: " + gadget + ". isExist: " + availToMe);
				}
			}
		};
		this.subscribeEvent(GadgetAvailableEvent.class, callback);
	}

	private void subscribeToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			//terminate When the program duration over
			terminate();
		};
		subscribeBroadcast(TerminationBroadCast.class,terminateCall);
	}

	@Override
	protected void initialize() {
		subscribeToTermination();
		subscribeToGadgetAvailable();
		subscriveToTimeBroadCast();
	}

}
