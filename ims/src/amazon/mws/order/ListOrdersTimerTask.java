/**
 * 
 */
package amazon.mws.order;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.XMLGregorianCalendar;

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

	public static final int RequestQuota = 6;
	public static final int RestoreQuota = 1;
	public static final int RestorePeriod = 60;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	public static final int ScopeHours = -12;

	private XMLGregorianCalendar createdAfter;
	private XMLGregorianCalendar createdBefore;
	private int loopTimes = 1;

	public ListOrdersTimerTask() {
		super();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, ScopeHours);
		createdAfter = Time.getTime(new Timestamp(cal.getTimeInMillis()));

		createdBefore = null;
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
	public MWSTimerTask<Order>.Result getNextResult(String nextToken) {
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
	public int updateDatabase(List<Order> orders) {
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

	@Override
	public boolean isLoop() {
		return loopTimes-- > 0;
	}

}
