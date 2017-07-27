/**
 * 
 */
package amazon.mws.order;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.mws.MWSTimerTask;
import common.util.Time;

/**
 * Insert orders into IMS from MWS Orders API
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 9:40:09 PM
 */
public class ListOrdersTimerTask extends MWSTimerTask<Order> {

	// according to Amazon MWS throttling
	public static final int RequestQuota = 6;
	public static final int RestoreQuota = 1;
	public static final int RestorePeriod = 60;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	// time span in hour for getting orders from MWS
	// public static final int TimeSpanInHour = -12;
	public static final int TimeSpanInHour = -24 * 30 * 3;

	// time condition for getting orders from MWS
	private XMLGregorianCalendar createdAfter;
	private XMLGregorianCalendar createdBefore;

	private void reset() {
		createdAfter = null;
		createdBefore = null;
	}

	@Override
	protected void beforeWork() {
		reset();

		// set the createdAfter
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.HOUR_OF_DAY, ScopeHours);
		cal.add(Calendar.HOUR_OF_DAY, TimeSpanInHour);// TODO
		createdAfter = Time.getTime(new Timestamp(cal.getTimeInMillis()));

		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() createdAfter=" + createdAfter);
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": beforeWork() createdBefore=" + createdBefore);
	}

	@Override
	protected void afterWork() {
		reset();
	}

	@Override
	protected void work() {
		/**
		 * if MarketplaceWebServiceOrdersException happened while calling MWS to get
		 * first result, then do not handle the exception and end the times task for
		 * this time
		 */
		// Make the call to get first result
		int mwsCalledTimes = 1;
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": getFirstResult(), mwsCalledTimes=" + mwsCalledTimes);// TODO
		MWSTimerTask<Order>.Result result = getFirstResult();
		String nextToken = result.getNextToken();
		// List<Order> dataList = result.getDataList();

		// Update database according to the result from MWS, Asynchronously
		System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
				+ getClass().getName() + ": updateDatabaseAsync(), mwsCalledTimes=" + mwsCalledTimes);// TODO
		updateDatabaseAsync(result.getDataList(), mwsCalledTimes);

		while (nextToken != null) {
			if (mwsCalledTimes++ < getRequestQuota()) {
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
					Result nextResult = getNextResult(nextToken);

					// set new nextToken
					nextToken = nextResult.getNextToken();

					// Update database according to the result from MWS, Asynchronously
					System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
							+ getClass().getName() + ": updateDatabaseAsync(), mwsCalledTimes=" + mwsCalledTimes);// TODO
					updateDatabaseAsync(nextResult.getDataList(), mwsCalledTimes);
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

	}

	@Override
	public MWSTimerTask<Order>.Result getFirstResult() {
		// Make the call ListOrders
		ListOrdersResponse response = ListOrdersMWS.listOrders(createdAfter, createdBefore);
		ListOrdersResult listOrdersResult = response.getListOrdersResult();

		// Construct result
		MWSTimerTask<Order>.Result result = new Result();
		result.setDataList(listOrdersResult.getOrders());
		result.setNextToken(listOrdersResult.getNextToken());

		// Return result
		return result;
	}

	@Override
	public MWSTimerTask<Order>.Result getNextResult(String nextToken) throws MarketplaceWebServiceOrdersException {
		// Make the call ListOrdersByNextToken
		ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
		ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();

		// Construct result
		MWSTimerTask<Order>.Result result = new Result();
		result.setDataList(nextTokenResult.getOrders());
		result.setNextToken(nextTokenResult.getNextToken());

		// Return result
		return result;
	}

	@Override
	protected int updateDatabase(List<Order> orders) {
		return new ListOrdersDatabase().insert(orders);
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

}
