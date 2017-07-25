/**
 * 
 */
package amazon.mws;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 12:57:59 PM
 */
public abstract class MWSTimerTask<T> extends TimerTask {

	/**
	 * The times that MWS API is called
	 */
	private int mwsCalledTimes;
	/**
	 * Used to update database, Asynchronously
	 */
	private ExecutorService executorService;
	/**
	 * Used to call MWS API to get next result
	 */
	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * Action need to do before the work
	 */
	protected abstract void beforeWork();

	/**
	 * If the work is need to loop
	 * 
	 * @return
	 */
	protected abstract boolean isWorkLoop();

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
			System.out.println(getClass().getName() + ": beforeWork()");// TODO
			/** Action need to do before the work */
			beforeWork();

			/** If the work is need to loop */
			while (isWorkLoop()) {
				// Call MWS API according to the Request quota
				System.out.println(getClass().getName() + ": callByRequestQuota()");// TODO
				Result result = callByRequestQuota();
				String nextToken = result.getNextToken();

				// Call MWS API according to the Restore rate
				if (nextToken != null) {
					try {
						System.out.println(getClass().getName() + ": callByRestorePeriod()");// TODO
						callByRestorePeriod(nextToken);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// finally {
					// // Shutdown ScheduledExecutorService
					// if (scheduledExecutorService != null) {
					// scheduledExecutorService.shutdown();
					// System.out.println(getClass().getName() + ":
					// scheduledExecutorService.shutdown()");// TODO
					// }
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println(getClass().getName() + ": afterWork()");// TODO
				/** Action need to do after the work */
				afterWork();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			//
			// // Shutdown ExecutorService
			// if (executorService != null) {
			// executorService.shutdown();
			// System.out.println(getClass().getName() + ": executorService.shutdown()");//
			// TODO
			// }
		}
	}

	private Result callByRequestQuota() {
		System.out.println(getClass().getName() + ": getFirstResult() in callByRequestQuota()");// TODO
		// Make the call to get first result
		MWSTimerTask<T>.Result result = getFirstResult();
		mwsCalledTimes++;
		String nextToken = result.getNextToken();
		List<T> dataList = result.getDataList();

		System.out.println(getClass().getName() + ": updateDatabaseAsync() in callByRequestQuota()");// TODO
		// Update database according to the result from MWS, Asynchronously
		updateDatabaseAsync(result.getDataList());

		// Make the call to get next result by next token
		while (mwsCalledTimes++ < getRequestQuota() && nextToken != null) {
			System.out.println(getClass().getName() + ": getNextResult() in callByRequestQuota()");// TODO
			Result nextResult = getNextResult(nextToken);

			nextToken = nextResult.getNextToken();
			dataList.addAll(nextResult.getDataList());

			System.out.println(getClass().getName() + ": updateDatabaseAsync() in callByRequestQuota()");// TODO
			// Update database according to the result from MWS, Asynchronously
			updateDatabaseAsync(nextResult.getDataList());
		}

		// Change result
		result.setDataList(dataList);
		result.setNextToken(nextToken);

		// Return result
		return result;
	}

	private void callByRestorePeriod(String nextToken) {
		if (scheduledExecutorService == null) {
			// Create ScheduledExecutorService
			scheduledExecutorService = Executors.newScheduledThreadPool(2);
		}

		scheduledExecutorService.schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println(getClass().getName() + ": getNextResult() in callByRestorePeriod()");// TODO
				// Call
				Result result = getNextResult(nextToken);

				System.out.println(getClass().getName() + ": updateDatabaseAsync() in callByRestorePeriod()");// TODO
				// Update database according to the result from MWS, Asynchronously
				updateDatabaseAsync(result.getDataList());

				// Loop call
				if (result.getNextToken() != null) {
					callByRestorePeriod(result.getNextToken());
				}
			}
		}, getRestorePeriod(), getTimeUnit());
	}

	/**
	 * Update database according to the result from MWS, Asynchronously
	 * 
	 * @param executorService
	 * @param result
	 */
	private void updateDatabaseAsync(List<T> dataList) {
		if (executorService == null) {
			// Create ExecutorService
			executorService = Executors.newCachedThreadPool();
		}

		executorService.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return updateDatabase(dataList);
			}
		});
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
	protected abstract Result getNextResult(String nextToken);

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
