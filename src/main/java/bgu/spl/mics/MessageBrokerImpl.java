package bgu.spl.mics;


import java.util.Hashtable;
import java.util.LinkedList;
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
	private Hashtable< Subscriber , LinkedBlockingQueue<Message> > subscribers;
	private Hashtable< Class<? extends Message> , LinkedList<Subscriber> > typesMap;
	/**
	 * Retrieves the single instance of this class.
	 */

	public static MessageBroker getInstance() { // STATUS: safe
		return SingletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		//s adds m to the relevant list of this type
		synchronized (this) {//never!!!!!!
			LinkedList<Subscriber> typeSubscribers=typesMap.get(type);
			typeSubscribers.add(m);
			typesMap.replace(type,typeSubscribers);
			//replaces only if type is mapped to some value in the hashtable !!!!
		}
		//f

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		//s adds m to the relevant list of this type
		synchronized (this) {//never!!!!!!
			LinkedList<Subscriber> typeSubscribers=typesMap.get(type);
			typeSubscribers.add(m);
			typesMap.replace(type,typeSubscribers);
			//replaces only if type is mapped to some value in the hashtable !!!!
		}
		//f

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		//dont know what to do with e...
		synchronized (this){//not good
			e.getFuture().resolve(result);
		}


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
		try {
			synchronized (this) {//never !!!!
				LinkedList<Subscriber> subsToType = typesMap.get(e.getClass());
				//inserts e to some queue(depends on our implementation) in SubsToType
				Subscriber receiver = ((subsToType.getLast()));//depends on our implementation
				LinkedBlockingQueue<Message> receiverQueue = subscribers.get(receiver);
				receiverQueue.put(e);
			}
		}catch (InterruptedException msg){
			msg.printStackTrace();
		}
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
		synchronized (this) {
			subscribers.remove(m);//if m is not there returns null, maybe useful in the future
			//s removes m from type map.(if to some key it is not there does nothing)
			Set<Class<? extends Message>> keys = typesMap.keySet();
			for (Class<? extends Message> key: keys) {
				typesMap.get(key).remove(m);
			}
			//f
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return subscribers.get(m).take();
		// will it ever change if at the moment i take queue it is empty?
		//doesnt take in calculation interrupt..

	}

	

}
