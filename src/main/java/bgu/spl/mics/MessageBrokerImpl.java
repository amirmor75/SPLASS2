package bgu.spl.mics;


import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBroker instance=null;
	/**
	 * Hashtable is a synchronized data structure in JAVA
	 * for every subscriber-Key, will be a blockingQueue of Events
	 */
	private Hashtable<Subscriber,LinkedBlockingQueue<? extends Message>> subscribers;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		synchronized (instance) {
			if (instance == null)
				instance = new MessageBrokerImpl();
			return instance;
		}
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		/**impl:
		 * 1.pushes e to a relevant queue.
		 * 2. return Future<T>=new Future();
		 */

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * insert a new subscriber into the hashtable and initialize its queue
	 */
	@Override
	public void register(Subscriber m) {
		subscribers.put(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
