package bgu.spl.mics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 *
 * @inv none
 */
public class Future<T> {

	private T result;
	private volatile boolean isDone;
	

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		result=null;
		isDone=false;
	}

	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
	 * @pre none
	 * @post isDone()==true
	 * @return return the result of type T if it is available, if not wait until it is available.
     */

	public synchronized T  get() {
			while (!isDone) {
				/**
				 *  result.wait()
				 */
			}	
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
	 *
	 * @pre  T!=null
	 * @post get()=={@param result}
     */
	public void resolve (T result) {
		//TODO : implement this correctly
		if(result!=null) {
			this.result = result; //maybe i should clone it- so it will be safe thread
			isResolved = true;
		}
	}
	
	/**
	 * @pre none
	 * @post trivial
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return isResolved;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
	 * @pre none
	 * @post  {@param timeout}>={@code timeout} && isDone()==true
	 * @post  {@param timeout}<{@code timeout} && isDone()==false
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) {
		Lock lock=new ReentrantLock();
		try {
			if (lock.tryLock(timeout, unit)) {
				if(!isDone())
					resolve(result);
				return result;
			}
		} catch (InterruptedException e) {
			//do nothing
		} finally {
			lock.unlock();
		}
		return null;
	}

	//await function
}
