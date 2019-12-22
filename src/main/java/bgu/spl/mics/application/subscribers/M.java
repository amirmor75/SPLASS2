package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private  String serialNumber;

	public M(String num) {
		super("M");
		serialNumber=num;
	}

	@Override
	protected synchronized void initialize() {

		//Lambda implementation of call(Event e) function-
		//				 whatever we want to happen when SomeEvent is received
//		this.subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEventTask e) ->
//
//				());


		// provokes register, defines callback
		//our callback wait() for the AgentsAvailableEvent we sent to Moneypenny
		/**
		 * callback impl:
		 * 1. Future<T> F = mypublisher.sendEvent( new AgentsAvailableEvent(myEvent.getSerials()) of our relevant serialnumber) AgentsAvailableEvent we send to Moneypenny
		 * 2. while(Future !isDone){Future.wait()}
		 * 3. when future is resolved we call GadgetAvailableEvent to Q
		 * 4. while(Future !isDone){Future.wait()}
		 * 5. when future is resolved we executed our event and
		 *
		 */
		
	}

}
