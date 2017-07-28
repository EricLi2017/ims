/**
 * 
 */
package amazon.mws;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 12:57:59 PM
 */
public abstract class MWSTimerTask<T> extends TimerTask {
	private boolean isReady = true;

	private boolean isReady() {
		return isReady;
	}

	private void unready() {
		isReady = false;
	}

	/**
	 * Set the task to ready for the next scheduled call
	 * 
	 * If there is no new async task produced by this task:
	 * 
	 * The ready() method should be called when the task is complete or ended by an
	 * exception.
	 * 
	 * If there is new async task produced by this task: The ready method also
	 * should be called when all the new tasks are complete or ended by an
	 * exception.
	 * 
	 */
	protected final void ready() {
		isReady = true;
	}

	/**
	 * Action need to do before the work
	 */
	protected abstract void beforeWork();

	/**
	 * Action of the work
	 */
	protected abstract void work() throws Exception;

	/**
	 * Action need to do after the work
	 */
	protected abstract void afterWork();

	/**
	 * 
	 * If the Request quota Call of MWS API is not enough to get all the expected
	 * result, then the additional Restore rate Calls of MWS API will be made
	 * continuously
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public final void run() {
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": run()");
		if (!isReady()) {
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": The task was not ready, it stopped runnig this time!");
			return;
		} else {
			unready();
		}

		try {
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": beforeWork()");
			/** Action need to do before the work */
			beforeWork();

			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": work()");
			/** Action of the work */
			work();
		} catch (Exception e) {
			/** Set the task to ready for the next scheduled call */
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": ready(), task ended for exception");
			ready();
			e.printStackTrace();
		} finally {
			try {
				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ getClass().getName() + ": afterWork()");
				/** Action need to do after the work */
				afterWork();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Get TimeUnit for task
	 * 
	 * @return
	 */
	protected abstract TimeUnit getTimeUnit();

	/**
	 * Get Request quota of the MWS API
	 * 
	 * @return times
	 */
	protected abstract int getRequestQuota();

	/**
	 * Get Restore period of the MWS API
	 * 
	 * Restore rate = Restore quota/Restore period
	 * 
	 * @return time in getTimeUnit()
	 */
	protected abstract int getRestorePeriod();

	/**
	 * Get Restore quota of the MWS API
	 * 
	 * Restore rate = Restore quota/Restore period
	 * 
	 * @return times
	 */
	protected abstract int getRestoreQuota();

}
