package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	public Q() {
		super("Q");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
//		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEventTask e) ->
//				(/* implementatoin  of call(Event e) function-
//				 whatever we want to happen when SomeEvent is received*/));

		// TODO Implement this
		
	}

}
