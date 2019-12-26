package bgu.spl.mics;


import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static class SingletonHolder {
		private static MessageBroker instance = new MessageBrokerImpl();
	}

	/**
	 * Hashtable is a synchronized data structure in JAVA
	 * for every subscriber-Key, will be a blockingQueue of Events
	 */

	private Hashtable< Subscriber , LinkedBlockingQueue<Message> > subscribers=new Hashtable<>();
	private Hashtable< Class<? extends Event<?>> , LinkedBlockingQueue<Subscriber>> eventSubscriberMap=new Hashtable<>();
	private Hashtable< Class<? extends Broadcast> , LinkedBlockingQueue<Subscriber>> broadcastSubscriberMap=new Hashtable<>();
	private Hashtable<Event<?>,Future> futureMap=new Hashtable<>();



	/**
	 * Retrieves the single instance of this class.
	 */


	public static MessageBroker getInstance() {//safe
		return SingletonHolder.instance;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {//safe
		if (eventSubscriberMap.get(type) == null)
			eventSubscriberMap.putIfAbsent(type, new LinkedBlockingQueue<>());
		eventSubscriberMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {//safe
		if (broadcastSubscriberMap.get(type) == null)
			broadcastSubscriberMap.putIfAbsent(type, new LinkedBlockingQueue<>());
		broadcastSubscriberMap.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (this) {
			futureMap.get(e).resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (this) {
			try {
				LinkedBlockingQueue<Subscriber> callTo = broadcastSubscriberMap.get(b.getClass());
				if (callTo != null) {
					for (Subscriber s : callTo) {
						if (subscribers.get(s) == null)
							subscribers.putIfAbsent(s, new LinkedBlockingQueue<>());
						subscribers.get(s).put(b);
					}
				}
			} catch (InterruptedException ignore) {Thread.currentThread().interrupt(); }
		}
	}

	/**impl:
	 * 1.pushes e to a relevant queue.
	 * 2. return Future<T>=new Future();
	 */
	@Override

	public <T> Future<T> sendEvent(Event<T> e) {//safe
		synchronized (this) {
			try {
				LinkedBlockingQueue<Subscriber> subsToType = eventSubscriberMap.get(e.getClass());
				if (subsToType != null && !subsToType.isEmpty()) {
					Subscriber roundRobined = subsToType.take();
					if(subscribers.get(roundRobined)!=null) {
						subscribers.get(roundRobined).put(e);
						subsToType.put(roundRobined);
					}
				}
			} catch (InterruptedException ignored) { Thread.currentThread().interrupt();}
			Future<T> future = new Future<>();
			futureMap.put(e, future);
			return future;
		}
	}

	/**
	 * insert a new subscriber into the hashtable and initialize its queue
	 */
	@Override
	public void register(Subscriber m){//safe
		LinkedBlockingQueue<Message> mQueue=new LinkedBlockingQueue<>();
		subscribers.putIfAbsent(m, mQueue);
	}

	@Override
	public void unregister(Subscriber m) {//not really safe (safe with synchronized (this) )
		synchronized (this) {//because deleting is not safe for the use of other threads
			Set<Class<? extends Event<?>>> Ekeys = eventSubscriberMap.keySet();
			for (Class<? extends Event<?>> key : Ekeys)
				eventSubscriberMap.get(key).remove(m);

			Set<Class<? extends Broadcast>> Bkeys = broadcastSubscriberMap.keySet();
			for (Class<? extends Broadcast> key : Bkeys)
				broadcastSubscriberMap.get(key).remove(m);

			subscribers.remove(m);
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subscribers.get(m).take();
	}
}
