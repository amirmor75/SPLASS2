package bgu.spl.mics;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

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
	private Hashtable< Subscriber , LinkedBlockingQueue<Message>> subscribers;
	private Hashtable< Class<? extends Message> , LinkedList<Subscriber>> typesMap;

	private Object lockSubscribers,lockMsg;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() { // STATUS: safe
		return SingletonHolder.instance;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		//s adds m to the relevant list of this type
		synchronized (lockMsg) {
			if (!typesMap.containsKey(type))
				typesMap.put(type, new LinkedList<>());
			LinkedList<Subscriber> typeSubscribers = typesMap.get(type);
			typeSubscribers.add(m);
			typesMap.replace(type, typeSubscribers);
		}
		//f
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		//s adds m to the relevant list of this type
		synchronized (lockMsg) {
			if(!typesMap.containsKey(type))
				typesMap.put(type,new LinkedList<>());
			LinkedList<Subscriber> typeSubscribers=typesMap.get(type);
			typeSubscribers.add(m);
			typesMap.replace(type,typeSubscribers);
		}
		//f
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (lockMsg) {
			synchronized (lockSubscribers) {
				try {
					Iterator<Subscriber> subs = typesMap.get(b.getClass()).iterator();
					while (subs.hasNext()) {
						subscribers.get(subs.next()).put(b);
					}
				} catch (Exception ignored) { }
			}
		}
	}

	private <T> Subscriber getSubByType(Event<T> e){
		synchronized (lockMsg){
			synchronized (lockSubscribers) {
				LinkedList<Subscriber> subsToType = typesMap.get(e.getClass());
				Subscriber subMinTasks = subsToType.getFirst();
				for (Subscriber sub : subsToType) {
					if (subscribers.get(sub).size()<subscribers.get(subMinTasks).size())
						subMinTasks=sub;
				}
				return subMinTasks;
			}
		}
	}

	/**impl:
	 * 1.pushes e to a relevant queue.
	 * 2. return Future<T>=new Future();
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		try {
			synchronized (lockSubscribers) {
				Subscriber sub=getSubByType(e);
				LinkedBlockingQueue<Message> receiverQueue = subscribers.get(sub);
				receiverQueue.put(e);
			}
		}catch (InterruptedException ignored){ }
		return e.getFuture();
	}

	/**
	 * insert a new subscriber into the hashtable and initialize its queue
	 */
	@Override
	public void register(Subscriber m){// thread safe
		LinkedBlockingQueue<Message> mQueue=new LinkedBlockingQueue<>();
		subscribers.putIfAbsent(m, mQueue);
	}


	@Override
	public void unregister(Subscriber m) {//not safe
		synchronized (lockMsg) {
			synchronized (lockSubscribers) {
				subscribers.remove(m);//if m is not there returns null, maybe useful in the future
				//s removes m from type map.(if to some key it is not there does nothing)
				Set<Class<? extends Message>> keys = typesMap.keySet();
				for (Class<? extends Message> key : keys) {
					typesMap.get(key).remove(m);
				}
				//f
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subscribers.get(m).take();
	}

	

}
