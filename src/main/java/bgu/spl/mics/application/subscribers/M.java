package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private SimplePublisher mypublisher;

	public M() {
		super("Change_This_Name");
		// TODO Implement this
	}

	@Override
	protected synchronized void initialize() {// provokes rgister, defines callback
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
		// TODO Implement this
		
	}

}
