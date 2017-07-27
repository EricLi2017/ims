/**
 * 
 */
package amazon.mws;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 12:57:59 PM
 */
public abstract class MWSTimerTask<T> extends TimerTask {
	/**
	 * Used to update database, Asynchronously
	 */
	private ExecutorService executorService;
	/**
	 * Used to call MWS API to get next result
	 */
	protected ScheduledExecutorService scheduledExecutorService;

	/**
	 * Action need to do before the work
	 */
	protected abstract void beforeWork();

	/**
	 * Action of the work
	 */
	protected abstract void work();

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
	public void run() {
		try {
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": beforeWork()");// TODO
			/** Action need to do before the work */
			beforeWork();

			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": work()");// TODO
			/** Action of the work */
			work();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ getClass().getName() + ": afterWork()");// TODO
				/** Action need to do after the work */
				afterWork();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param nextToken
	 * @param mwsCalledTimes
	 * @return the nextToken when this execution complete in the future
	 */
	protected final ScheduledFuture<String> callByRestorePeriodAsync(String nextToken, int mwsCalledTimes) {
		if (scheduledExecutorService == null) {
			// Create ScheduledExecutorService
			scheduledExecutorService = Executors.newScheduledThreadPool(2);
		}

		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				// Call
				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ getClass().getName() + ": getNextResult() in callByRestorePeriodAsync(), mwsCalledTimes="
						+ mwsCalledTimes + ", nextToken=" + nextToken);// TODO
				Result result = null;
				boolean isThrottled;
				try {
					result = getNextResult(nextToken);
					isThrottled = false;
				} catch (MarketplaceWebServiceOrdersException ex) {
					isThrottled = (ex.getStatusCode() == ErrorCode.QuotaExceeded.value()
							|| ex.getStatusCode() == ErrorCode.RequestThrottled.value());
				}

				// check if isThrottled
				if (!isThrottled) {
					// Update database according to the result from MWS, Asynchronously
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName()
							+ ": updateDatabaseAsync() in callByRestorePeriodAsync(), mwsCalledTimes="
							+ mwsCalledTimes);// TODO
					updateDatabaseAsync(result.getDataList(), mwsCalledTimes);

					if (result.getNextToken() != null) {
						// Loop call use the new nextToken
						callByRestorePeriodAsync(result.getNextToken(), mwsCalledTimes + 1);
					}
					return result.getNextToken();
				} else {
					// re call use the same nextToken
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName()
							+ ": failed to getNextResult() in callByRestorePeriodAsync(), mwsCalledTimes="
							+ mwsCalledTimes + ", nextToken=" + nextToken);// TODO
					callByRestorePeriodAsync(nextToken, mwsCalledTimes + 1);
					return nextToken;
				}

			}
		};

		return scheduledExecutorService.schedule(callable, getRestorePeriod(), getTimeUnit());
	}

	/**
	 * Update database according to the result from MWS, Asynchronously
	 * 
	 * @param dataList
	 * @return the affected rows of database when this execution complete in the
	 *         future
	 */
	protected final Future<Integer> updateDatabaseAsync(List<T> dataList, int mwsCalledTimes) {
		if (executorService == null) {
			// Create ExecutorService
			executorService = Executors.newScheduledThreadPool(6);// for Orders API request quota is 6
		}

		return executorService.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ getClass().getName() + ": updateDatabase() in updateDatabaseAsync(), mwsCalledTimes="
						+ mwsCalledTimes);// TODO
				return updateDatabase(dataList);
			}
		});
	}

	protected final Future<Integer> updateCallConditionAsync(List<T> dataList, int mwsCalledTimes) {
		// TODO
		return null;
	}

	/**
	 * Call MWS API to get fist result
	 * 
	 * @return
	 */
	protected abstract Result getFirstResult();

	/**
	 * Call MWS API to get next result by next token
	 * 
	 * @return
	 */
	protected abstract Result getNextResult(String nextToken) throws MarketplaceWebServiceOrdersException;

	/**
	 * Update database according to the result from MWS
	 * 
	 * @param result
	 * @return affected rows in database
	 */
	protected abstract int updateDatabase(List<T> dataList);

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

	public class Result {
		private List<T> dataList;
		private String nextToken;

		public List<T> getDataList() {
			return dataList;
		}

		public void setDataList(List<T> dataList) {
			this.dataList = dataList;
		}

		public String getNextToken() {
			return nextToken;
		}

		public void setNextToken(String nextToken) {
			this.nextToken = nextToken;
		}

		@Override
		public String toString() {
			return "Result [dataList=" + dataList + ", nextToken=" + nextToken + "]";
		}

	}

}
