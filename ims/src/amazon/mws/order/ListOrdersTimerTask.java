/**
 * 
 */
package amazon.mws.order;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.db.edit.ListOrdersTrackEditor;
import amazon.db.query.ListOrdersTrackQuerier;
import amazon.mws.MWSTimerTask;
import common.util.Time;

/**
 * Insert orders into IMS from MWS Orders API
 * 
 * When this task is done each time, it should process all the orders which are
 * in the specified time.
 * 
 * In other words, there should be no pending task after this task is done each
 * time. If there is a pending task, it means there is a task still running or a
 * task ended in an unexpected error.
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 9:40:09 PM
 */
public class ListOrdersTimerTask extends MWSTimerTask<Order> {

	// according to Amazon MWS throttling
	public static final int RequestQuota = 6;
	public static final int RestoreQuota = 1;
	public static final int RestorePeriod = 60;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	// the default first createdAfter time span in hour before now
	public static final int DEFAULT_FIRST_CREATED_AFTER_FROM_NOW_HOUR = -24 * 30 * 3;// 90 days before now

	// time condition for getting orders from MWS
	private XMLGregorianCalendar createdAfter;
	private XMLGregorianCalendar createdBefore;

	// indicate if exists a pending task in database
	private boolean hasPerviousPendingId;
	// the id of the table list_orders_track with pending status
	private int pendingId;

	/**
	 * Used to call MWS API to get next result
	 */
	protected ScheduledExecutorService scheduledExecutorService;

	// get createdAfter : if does not exist, insert a default
	private XMLGregorianCalendar getCreatedAfterAndId() throws ClassNotFoundException, SQLException

	{
		Timestamp latestCompletedCreatedBefore = ListOrdersTrackQuerier.queryLatestCompletedCreatedBefore();
		if (latestCompletedCreatedBefore != null) {
			return Time.getTime(latestCompletedCreatedBefore);
		} else {
			// get default
			Calendar cal = Calendar.getInstance();
			// cal.add(Calendar.HOUR_OF_DAY, ScopeHours);
			cal.add(Calendar.HOUR_OF_DAY, DEFAULT_FIRST_CREATED_AFTER_FROM_NOW_HOUR);

			// insert the default first row
			Timestamp now = new Timestamp(System.currentTimeMillis());
			ListOrdersTrackEditor.insertDefaultFirst(new Timestamp(cal.getTimeInMillis()), now, now);

			return Time.getTime(new Timestamp(cal.getTimeInMillis()));
		}
	}

	@Override
	protected void beforeWork() {
		// reset
		createdAfter = null;
		createdBefore = null;
		hasPerviousPendingId = true;
		pendingId = 0;

		// set values
		// Use exception to accurately determine the current situation
		try {
			createdAfter = getCreatedAfterAndId();
			hasPerviousPendingId = ListOrdersTrackQuerier.hasPendingTask();
			if (!hasPerviousPendingId) {
				// insert a new row and get row id
				pendingId = ListOrdersTrackEditor.insertNewAndGetId(new Timestamp(System.currentTimeMillis()));
			} else {
				// get row id from the pending task
				pendingId = ListOrdersTrackQuerier.getOldestPendingTaskId();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() hasPerviousPendingId=" + hasPerviousPendingId);
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() pendingId=" + pendingId);
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() createdAfter=" + createdAfter);
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() createdBefore=" + createdBefore);

	}

	@Override
	protected void afterWork() {
	}

	@Override
	protected void work() throws SQLException, ClassNotFoundException {
		/**
		 * if MarketplaceWebServiceOrdersException happened while calling MWS to get
		 * first result, then do not handle the exception and end the times task for
		 * this time
		 */
		// Make the call to get first result
		int mwsCalledTimes = 1;
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": getFirstResult(), mwsCalledTimes=" + mwsCalledTimes);// TODO
		ListOrdersResult result = getFirstResult();
		String nextToken = result.getNextToken();
		// List<Order> dataList = result.getDataList();

		// Update database according to the result from MWS, Asynchronously
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": updateDatabase(), mwsCalledTimes=" + mwsCalledTimes);// TODO
		updateDatabase(result.getOrders());

		while (nextToken != null) {
			if (++mwsCalledTimes <= getRequestQuota()) {
				/**
				 * if MarketplaceWebServiceOrdersException happened while calling MWS using a
				 * nextToken, then continue to calling MWS using the same nextToke until get
				 * result from MWS or reach request quota
				 */
				try {
					// Make the call to get next result by next token
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": getNextResult(), mwsCalledTimes=" + mwsCalledTimes
							+ ", nextToken=" + nextToken);// TODO
					ListOrdersByNextTokenResult nextResult = getNextResult(nextToken);

					// set new nextToken
					nextToken = nextResult.getNextToken();

					// Update database according to the result from MWS, Asynchronously
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": updateDatabase(), mwsCalledTimes=" + mwsCalledTimes);// TODO
					updateDatabase(nextResult.getOrders());
				} catch (MarketplaceWebServiceOrdersException ex) {
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": failed to getNextResult(), mwsCalledTimes=" + mwsCalledTimes
							+ ", nextToken=" + nextToken);// TODO
				}
			} else {
				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ getClass().getName() + ": callByRestorePeriodAsync(), mwsCalledTimes=" + mwsCalledTimes);// TODO
				callByRestorePeriodAsync(nextToken, mwsCalledTimes);
				break;
			}
		}

		// createBefor should be same for every response
		if (nextToken == null) {
			/** Set the task to ready for the next scheduled call */
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": ready(), mwsCalledTimes=" + mwsCalledTimes);// TODO
			ready();

			// Complete the track of ListOrders task
			System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
					+ getClass().getName() + ": updateToCompleted(), mwsCalledTimes=" + mwsCalledTimes);// TODO
			updateToCompleted(Time.getTime(result.getCreatedBefore()), pendingId);
		}
	}

	private ListOrdersResult getFirstResult() {
		// Make the call ListOrders
		ListOrdersResponse response = ListOrdersMWS.listOrders(createdAfter, createdBefore);
		ListOrdersResult listOrdersResult = response.getListOrdersResult();

		// Construct result
		// MWSTimerTask<Order>.Result result = new Result();
		// result.setDataList(listOrdersResult.getOrders());
		// result.setNextToken(listOrdersResult.getNextToken());

		// Return result
		return listOrdersResult;
	}

	private ListOrdersByNextTokenResult getNextResult(String nextToken) throws MarketplaceWebServiceOrdersException {
		// Make the call ListOrdersByNextToken
		ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
		ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();

		// Construct result
		// MWSTimerTask<Order>.Result result = new Result();
		// result.setDataList(nextTokenResult.getOrders());
		// result.setNextToken(nextTokenResult.getNextToken());

		// Return result
		return nextTokenResult;
	}

	private int updateDatabase(List<Order> orders) throws SQLException {
		return new ListOrdersDatabase().insert(orders);
	}

	private int updateToCompleted(Timestamp createdBefore, int id) throws ClassNotFoundException, SQLException {
		return ListOrdersTrackEditor.updateToCompleted(createdBefore, new Timestamp(System.currentTimeMillis()), id);
	}

	@Override
	public TimeUnit getTimeUnit() {
		return TIME_UNIT;
	}

	@Override
	public int getRequestQuota() {
		return RequestQuota;
	}

	@Override
	public int getRestorePeriod() {
		return RestorePeriod;
	}

	@Override
	public int getRestoreQuota() {
		return RestoreQuota;
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
				String newNextToken = nextToken;
				try {
					// Call
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": getNextResult() in callByRestorePeriodAsync(), mwsCalledTimes="
							+ mwsCalledTimes + ", nextToken=" + nextToken);// TODO
					ListOrdersByNextTokenResult result = getNextResult(nextToken);
					newNextToken = result.getNextToken();

					// Update database according to the result from MWS, Asynchronously
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": updateDatabase() in callByRestorePeriodAsync(), mwsCalledTimes="
							+ mwsCalledTimes);// TODO
					updateDatabase(result.getOrders());

					// task end
					if (newNextToken == null) {
						/** Set the task to ready for the next scheduled call */
						System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName()
								+ ": " + getClass().getName() + ": ready(), mwsCalledTimes=" + mwsCalledTimes);// TODO
						ready();

						// Complete the track of ListOrders task
						System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName()
								+ ": " + getClass().getName()
								+ ": updateToCompleted() in callByRestorePeriodAsync(), mwsCalledTimes="
								+ mwsCalledTimes);// TODO
						updateToCompleted(Time.getTime(result.getCreatedBefore()), pendingId);
					}
				} catch (Exception e) {
					// use old nextToken as the new nextToken
					newNextToken = nextToken;

					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName()
							+ ": processing failed in callByRestorePeriodAsync(), mwsCalledTimes=" + mwsCalledTimes
							+ ", nextToken=" + nextToken);// TODO
				}

				// process new async task
				if (newNextToken != null) {
					// Loop call use the new nextToken
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": callByRestorePeriodAsync(), mwsCalledTimes="
							+ (mwsCalledTimes + 1));// TODO
					callByRestorePeriodAsync(newNextToken, mwsCalledTimes + 1);
				}

				return newNextToken;
			}
		};

		return scheduledExecutorService.schedule(callable,

				getRestorePeriod(), getTimeUnit());
	}
}
