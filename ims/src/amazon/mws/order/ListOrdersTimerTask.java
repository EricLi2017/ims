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

import javax.naming.NamingException;
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
 * Insert all status orders into IMS from MWS Orders API
 * 
 * When this task is done each time, it should process all the orders which are
 * in the specified createdAfter.
 * 
 * Use table list_orders_track to track the execution of the task and to provide
 * createdAfter value.
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 9:40:09 PM
 */
public class ListOrdersTimerTask extends MWSTimerTask {

	// the default first createdAfter time span in hour before now
	public static final int DEFAULT_FIRST_CREATED_AFTER_FROM_NOW_HOUR = -24 * 30 * 3;// 90 days before now

	// time condition for getting orders from MWS
	private XMLGregorianCalendar createdAfter;
	private XMLGregorianCalendar createdBefore;

	// indicate if exists a pending task in database
	private boolean hasPerviousPendingId;
	// the id of the table list_orders_track with pending status used for the task
	private int pendingId;

	/**
	 * Used to scheduled new tasks for calling MWS API to get next result
	 */
	private ScheduledExecutorService scheduledExecutorService;

	private static ListOrdersTimerTask ListOrdersTimerTask = new ListOrdersTimerTask();

	private ListOrdersTimerTask() {

	}

	public static ListOrdersTimerTask getInstance() {
		return ListOrdersTimerTask;
	}

	@Override
	protected void work() throws SQLException, NamingException, MarketplaceWebServiceOrdersException {
		// initialization
		init();

		// Get all orders from MWS by createdAfter and save them to database
		saveAllOrdersByCreatedAfter();
	}

	/**
	 * Get the createdAfter for call MWS
	 * 
	 * If latestCompletedCreatedBefore exists in database, return it;
	 * 
	 * else insert a default first completed row, and return its createdBefore
	 * value.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	private XMLGregorianCalendar getCreatedAfter() throws SQLException, NamingException {
		// query latestCompletedCreatedBefore
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

	private void init() throws SQLException, NamingException {
		// reset
		createdAfter = null;
		createdBefore = null;
		hasPerviousPendingId = true;
		pendingId = 0;

		// set values
		createdAfter = getCreatedAfter();
		hasPerviousPendingId = ListOrdersTrackQuerier.hasPendingTask();
		if (!hasPerviousPendingId) {
			// insert a new row and get row id
			pendingId = ListOrdersTrackEditor.insertNewAndGetId(new Timestamp(System.currentTimeMillis()));
		} else {
			// get row id from the pending task
			pendingId = ListOrdersTrackQuerier.getOldestPendingTaskId();
		}

		System.out.println(getLogPrefix() + ": init() hasPerviousPendingId=" + hasPerviousPendingId);
		System.out.println(getLogPrefix() + ": init() pendingId=" + pendingId);
		System.out.println(getLogPrefix() + ": init() createdAfter=" + createdAfter);
		System.out.println(getLogPrefix() + ": init() createdBefore=" + createdBefore);
	}

	/**
	 * Get all orders from MWS by createdAfter and save them to database
	 * 
	 * If any MarketplaceWebServiceOrdersException happened while calling MWS to get
	 * first result, then do not handle the exception and end task will be ended for
	 * this time.
	 * 
	 * If throttling MarketplaceWebServiceOrdersException happened while calling MWS
	 * using a nextToken, then continue to scheduled a new task calling MWS using
	 * the same nextToke until get result from MWS or throttling exception happened
	 * to loop again.
	 * 
	 * @throws SQLException
	 * @throws NamingException
	 * @throws MarketplaceWebServiceOrdersException
	 */
	private void saveAllOrdersByCreatedAfter()
			throws SQLException, NamingException, MarketplaceWebServiceOrdersException {
		// Make the call to get first result
		int mwsCalledTimes = 1;
		System.out.println(getLogPrefix() + ": getFirstResult(), mwsCalledTimes=" + mwsCalledTimes);
		ListOrdersResult result = getFirstResult();// may cause MarketplaceWebServiceOrdersException
		String nextToken = result.getNextToken();

		// Update database according to the result from MWS
		if (result.getOrders() != null && result.getOrders().size() > 0) {
			System.out.println(getLogPrefix() + ": insertOrders(), orders size="
					+ (result.getOrders() == null ? null : result.getOrders().size()) + ", mwsCalledTimes="
					+ mwsCalledTimes);
			insertOrders(result.getOrders());
		}

		boolean isThrottling = false;
		while (nextToken != null) {
			if (++mwsCalledTimes <= ListOrdersMWS.REQUEST_QUOTA && !isThrottling) {
				try {
					// Make the call to get next result by next token
					System.out.println(getLogPrefix() + ": getNextResult(), mwsCalledTimes=" + mwsCalledTimes
							+ ", nextToken=" + nextToken);
					ListOrdersByNextTokenResult nextResult = getNextResult(nextToken);

					// set new nextToken
					nextToken = nextResult.getNextToken();

					// Update database according to the result from MWS
					if (nextResult.getOrders() != null && nextResult.getOrders().size() > 0) {
						System.out.println(getLogPrefix() + ": insertOrders(), orders size="
								+ (nextResult.getOrders() == null ? null : nextResult.getOrders().size())
								+ ", mwsCalledTimes=" + mwsCalledTimes);
						insertOrders(nextResult.getOrders());
					}
				} catch (MarketplaceWebServiceOrdersException ex) {
					// check if it is a throttling exception
					isThrottling = isThrottlingException(ex);
					System.out.println(getLogPrefix() + ": failed to getNextResult(), mwsCalledTimes=" + mwsCalledTimes
							+ ", isThrottling=" + isThrottling + ", nextToken=" + nextToken);
				}
			} else {
				System.out.println(getLogPrefix() + ": callByRestorePeriodAsync(), mwsCalledTimes=" + mwsCalledTimes);
				callByRestorePeriodAsync(nextToken, mwsCalledTimes);
				return;
			}
		}

		// createBefor should be same for every response
		if (nextToken == null) {
			/** Set the task to ready for the next scheduled call */
			System.out.println(getLogPrefix() + ": ready(), mwsCalledTimes=" + mwsCalledTimes);
			ready();

			// Complete the track of ListOrders task
			System.out.println(getLogPrefix() + ": updateTrackToCompleted(), mwsCalledTimes=" + mwsCalledTimes);
			updateTrackToCompleted(Time.getTime(result.getCreatedBefore()), pendingId);
		}
	}

	private ListOrdersResult getFirstResult() throws MarketplaceWebServiceOrdersException {
		// Make the call ListOrders
		ListOrdersResponse response = ListOrdersMWS.listOrders(createdAfter, createdBefore);
		ListOrdersResult listOrdersResult = response.getListOrdersResult();

		// Return result
		return listOrdersResult;
	}

	private ListOrdersByNextTokenResult getNextResult(String nextToken) throws MarketplaceWebServiceOrdersException {
		// Make the call ListOrdersByNextToken
		ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
		ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();

		// Return result
		return nextTokenResult;
	}

	private int insertOrders(List<Order> orders) throws SQLException {
		return new ListOrdersDatabase().insert(orders);
	}

	private int updateTrackToCompleted(Timestamp createdBefore, int id) throws SQLException, NamingException {
		return ListOrdersTrackEditor.updateToCompleted(createdBefore, new Timestamp(System.currentTimeMillis()), id);
	}

	/**
	 * 
	 * @param nextToken
	 * @param mwsCalledTimes
	 * @return the nextToken when this execution complete in the future
	 */
	private ScheduledFuture<String> callByRestorePeriodAsync(String nextToken, int mwsCalledTimes) {
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
					System.out.println(getLogPrefix() + ": getNextResult(), mwsCalledTimes=" + mwsCalledTimes
							+ ", nextToken=" + nextToken);
					ListOrdersByNextTokenResult result = getNextResult(nextToken);
					newNextToken = result.getNextToken();

					// Update database according to the result from MWS
					if (result.getOrders() != null && result.getOrders().size() > 0) {
						System.out.println(getLogPrefix() + ": insertOrders(), orders size="
								+ (result.getOrders() == null ? null : result.getOrders().size()) + ", mwsCalledTimes="
								+ mwsCalledTimes);
						insertOrders(result.getOrders());
					}

					// task end
					if (newNextToken == null) {
						/** Set the task to ready for the next scheduled call */
						System.out.println(getLogPrefix() + ": ready(), mwsCalledTimes=" + mwsCalledTimes);
						ready();

						// Complete the track of ListOrders task
						System.out.println(
								getLogPrefix() + ": updateTrackToCompleted(), mwsCalledTimes=" + mwsCalledTimes);
						updateTrackToCompleted(Time.getTime(result.getCreatedBefore()), pendingId);
					}
				} catch (Exception e) {
					// use old nextToken as the new nextToken
					newNextToken = nextToken;

					System.out.println(
							getLogPrefix() + ": processing failed in callByRestorePeriodAsync(), mwsCalledTimes="
									+ mwsCalledTimes + ", nextToken=" + nextToken);
				}

				// process new async task
				if (newNextToken != null) {
					// Loop call use the new nextToken
					System.out.println(
							getLogPrefix() + ": callByRestorePeriodAsync(), mwsCalledTimes=" + (mwsCalledTimes + 1));
					callByRestorePeriodAsync(newNextToken, mwsCalledTimes + 1);
				}

				return newNextToken;
			}
		};

		return scheduledExecutorService.schedule(callable, ListOrdersMWS.RESTORE_PERIOD, ListOrdersMWS.TIME_UNIT);
	}

}
