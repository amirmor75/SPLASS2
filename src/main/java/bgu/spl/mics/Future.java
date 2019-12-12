package bgu.spl.mics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private AtomicBoolean isDone;


	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		result=null;
		isDone=new AtomicBoolean(false);
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

	public T  get() {
		synchronized (result) {
			while (!isDone.get()) {
				try {
					result.wait();
				} catch (InterruptedException ignored) { }
			}
			result.notifyAll();
		}
		return result;
	}

	/**
	 * Resolves the result of this Future object.
	 *
	 * @pre  T!=null
	 * @post get()=={@param result}
	 */
	public synchronized void resolve (T result) {
		if(result!=null) {
			this.result = result;
			isDone.set(true);
		}
	}

	/**
	 * @pre none
	 * @post trivial
	 * @return true if this object has been resolved, false otherwise
	 */
	public boolean isDone() {
		return isDone.get();
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved,
	 * This method is non-blocking, it has a limited amount of time determined
	 * by {@code timeout}
	 * <p>
	 * @param timeout 	the maximal amount of time units to wait for the result.
	 * @param unit		the {@link TimeUnit} time units to wait.
	 * @pre {@param unit}!=null
	 * @post  {@param timeout}>={@code timeout} && isDone()==true
	 * @post  {@param timeout}<{@code timeout} && isDone()==false
	 * @return return the result of type T if it is available, if not,
	 * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
	 *         elapsed, return null.
	 */
	public T get(long timeout, TimeUnit unit) {
		synchronized(result) {
			if (unit != null) {
				TimeUnit milliTime=TimeUnit.MILLISECONDS;
				while (!isDone()) {
					try {
						result.wait(milliTime.convert(timeout,unit)); //waiting for {@param timeout} milliseconds
					} catch (InterruptedException ignored) { }
				}
				result.notifyAll();
			}
		}
		return result;
	}

}