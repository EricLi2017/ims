/**
 * 
 */
package amazon.mws;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;

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
	 * Indicates if an MarketplaceWebServiceOrdersException is a Throttling
	 * Exception
	 * 
	 * If a Throttling Exception happened, usually should wait a while for MWS to
	 * restore
	 * 
	 * @param mwsoe
	 * @return
	 */
	protected final boolean isThrottlingException(MarketplaceWebServiceOrdersException mwsoe) {
		return mwsoe != null && (ErrorCode.QuotaExceeded.value() == mwsoe.getStatusCode()
				|| ErrorCode.RequestThrottled.value() == mwsoe.getStatusCode());

	}

	/**
	 * Action need to do before the work
	 */
	// protected abstract void beforeWork() throws Exception;

	/**
	 * Action of the work
	 */
	protected abstract void work() throws Exception;

	/**
	 * Action need to do after the work
	 */
	// protected abstract void afterWork();

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
				+ getClass().getName() + ": run() started");
		if (!isReady()) {
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": The task was not ready, it stopped runnig this time!");
			return;
		} else {
			unready();
		}

		try {
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": work() started");
			/** Action of the work */
			work();
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": work() ended");
		} catch (Exception e) {
			/** Set the task to ready for the next scheduled call */
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": work() ended for exception, call ready()");
			ready();
			e.printStackTrace();
		}

		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": run() ended");
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
