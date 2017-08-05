/**
 * 
 */
package amazon.mws;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 12:57:59 PM
 */
public abstract class MWSTimerTask extends TimerTask {
	private static final Log log = LogFactory.getLog(MWSTimerTask.class);

	private boolean isReady = true;

	private synchronized boolean isReady() {
		if (isReady) {
			isReady = false;
			return true;
		} else {
			return false;
		}
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
	 * Get log prefix with its implementation class
	 * 
	 * @return
	 */
	private String getLogPrefixOfImplCls() {
		return getClass().getName();
	}

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
		log.info(getLogPrefixOfImplCls() + "run() started");
		if (!isReady()) {
			log.warn(getLogPrefixOfImplCls() + "The task was not ready, it stopped runnig this time!");
			return;
		}

		try {
			log.info(getLogPrefixOfImplCls() + "work() started");
			/** Action of the work */
			work();
			log.info(getLogPrefixOfImplCls() + "work() ended");
		} catch (Exception e) {
			/** Set the task to ready for the next scheduled call */
			log.error(getLogPrefixOfImplCls() + "work() ended for exception, call ready()");
			ready();
			e.printStackTrace();
		} finally {
			afterWork();
		}

		log.info(getLogPrefixOfImplCls() + "run() ended");
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
	 * Action must be done after the work
	 * 
	 * Default is an empty method, and can be override when needed.
	 * 
	 * Note : Please confirm whether it is safe to do some actions here especially
	 * in the case asynchronous thread may be produced by work method.
	 */
	protected void afterWork() {

	}
}