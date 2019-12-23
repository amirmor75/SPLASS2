package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;

import javax.swing.*;
import java.util.List;

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

	@Override
	protected void initialize() {

		//This callback update the current duration of the program when a timeBroadCast received.
		Callback<TimeBroadCast> timeCall=(TimeBroadCast e)->{
			currentDuration=e.getCurrentDuration();
		};
		this.subscribeBroadcast(TimeBroadCast.class,timeCall);

		Callback<GadgetAvailableEvent> callback=(GadgetAvailableEvent e)->{
			String gadget= e.getEventInformation();//requested gadget
			boolean availToMe= Inventory.getInstance().getItem(gadget);//checks if the gadget available, returns true and remove it from list if so
			MessageBrokerImpl.getInstance().complete(e,availToMe);//resolves the event

			//The time-tick in which Q receives a
			//GadgetAvailableEvent will be printed in the report (if the mission was eventually executed)

		};
		this.subscribeEvent(GadgetAvailableEvent.class, callback);
	}

}
