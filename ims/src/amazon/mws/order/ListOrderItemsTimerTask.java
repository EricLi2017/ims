/**
 * 
 */
package amazon.mws.order;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

import amazon.db.query.OrderQuerier;
import amazon.mws.MWSTimerTask;

/**
 * Insert order items into IMS from MWS Orders API
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 10:23:48 PM
 */
public class ListOrderItemsTimerTask extends MWSTimerTask<OrderItem> {
	public static final int RequestQuota = 30;
	public static final int RestoreQuota = 1;
	public static final int RestorePeriod = 2;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	private List<String> amazonOrderIdList;
	private String amazonOrderId;

	public ListOrderItemsTimerTask() {
		super();
		amazonOrderIdList = new OrderQuerier().selectOldestOrdersWithoutItems(RequestQuota);
	}

	@Override
	public boolean isLoop() {
		// Check if there is any other orders without order items
		if (amazonOrderIdList != null && amazonOrderIdList.size() > 0) {
			amazonOrderId = amazonOrderIdList.get(0);
			amazonOrderIdList.remove(0);
			return true;
		}
		return false;
	}

	@Override
	public MWSTimerTask<OrderItem>.Result getFirstResult() {
		// Make the call ListOrderItems
		ListOrderItemsResponse response = ListOrderItemsMWS.listOrderItems(amazonOrderId);
		ListOrderItemsResult listOrderItemsResult = response.getListOrderItemsResult();

		// Construct result
		MWSTimerTask<OrderItem>.Result result = new Result();
		result.setDataList(listOrderItemsResult.getOrderItems());
		result.setNextToken(listOrderItemsResult.getNextToken());

		// Return result
		return result;
	}

	@Override
	public MWSTimerTask<OrderItem>.Result getNextResult(String nextToken) {
		// Make the call ListOrderItemsByNextToken
		ListOrderItemsByNextTokenResponse nextTokenResponse = ListOrderItemsMWS.listOrderItemsByNextToken(nextToken);
		ListOrderItemsByNextTokenResult nextTokenResult = nextTokenResponse.getListOrderItemsByNextTokenResult();

		// Construct result
		MWSTimerTask<OrderItem>.Result result = new Result();
		result.setDataList(nextTokenResult.getOrderItems());
		result.setNextToken(nextTokenResult.getNextToken());

		// Return result
		return result;
	}

	@Override
	public int updateDatabase(List<OrderItem> dataList) {
		return new ListOrderItemsDatabase().insert(dataList, amazonOrderId);
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
