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

			System.out.println("Q got time: "+e.getCurrentDuration());

			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);
	}

	private void subscribeToGadgetAvailable(){
		Callback<GadgetAvailableEvent> callback=(GadgetAvailableEvent e)->{
			System.out.println("Q got GadgetAvailableEvent");

			String gadget= e.getEventInformation();//requested gadget
			boolean availToMe= Inventory.getInstance().getItem(gadget);//checks if the gadget available, returns true and remove it from list if so

			System.out.println("Q need to find gadget: "+gadget+ ". isExist: "+ availToMe);

			FutureResult result=new FutureResult(availToMe,currentDuration,currentDuration); //The time-tick in which Q receives a GadgetAvailableEvent will be printed in the report
			complete(e,result);//resolves the event
		};
		this.subscribeEvent(GadgetAvailableEvent.class, callback);
	}

	private void subscribeToTermination(){
		Callback<TerminationBroadCast> terminateCall=(TerminationBroadCast timeDuration)->{
			System.out.println("Q terminating...");
			//terminate When the program duration over
			terminate();
		};
		subscribeBroadcast(TerminationBroadCast.class,terminateCall);
	}

	@Override
	protected void initialize() {
		subscriveToTimeBroadCast();
		subscribeToGadgetAvailable();
		subscribeToTermination();
	}

}
