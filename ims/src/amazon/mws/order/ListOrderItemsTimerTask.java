/**
 * 
 */
package amazon.mws.order;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

	private void init() {
		amazonOrderIdList = null;
		amazonOrderId = null;
	}

	protected void beforeWork() {
		init();
		// Get the oldest orders that without order items
		// amazonOrderIdList = new
		// OrderQuerier().selectOldestOrdersWithoutItems(RequestQuota);
		amazonOrderIdList = new OrderQuerier().selectOldestOrdersWithoutItems(RequestQuota * 2);// TODO
		amazonOrderId = null;
	}

	protected void afterWork() {
		init();
	}

	// @Override
	// protected void work() {
	// if (amazonOrderIdList == null || amazonOrderIdList.size() < 1) {
	// System.out.println(
	// getClass().getName() + ": There is no any orders without related order items
	// at this time");
	// return;
	// }
	//
	// int mwsCalledTimes = 1;
	// String nextToken = null;// TODO
	// List<OrderItem> dataList = new ArrayList<>();
	// MWSTimerTask<OrderItem>.Result result = new Result();
	// for (String id : amazonOrderIdList) {
	// amazonOrderId = id;
	// if (nextToken != null) {
	// throw new IllegalArgumentException(
	// "A new amazonOrderId should start with a null nextToken! amazonOrderId=" +
	// amazonOrderId
	// + ", nextToken=" + nextToken);
	// }
	//
	// // Condition:mwsCalledTimes<=getRequestQuota() && nextToken==null
	// if (mwsCalledTimes++ < getRequestQuota()) {
	// // get first result
	// System.out.println(getClass().getName() + ": getFirstResult(),
	// mwsCalledTimes=" + mwsCalledTimes);// TODO
	// // Make the call to get first result
	// result = getFirstResult();
	// nextToken = result.getNextToken();
	// dataList = result.getDataList();
	//
	// System.out.println(getClass().getName() + ": updateDatabaseAsync(),
	// mwsCalledTimes=" + mwsCalledTimes);// TODO
	// // Update database according to the result from MWS, Asynchronously
	// updateDatabaseAsync(result.getDataList(), mwsCalledTimes);
	//
	// // Condition:mwsCalledTimes<=getRequestQuota() && nextToken!=null
	// // Make the call to get next result by next token
	// while (mwsCalledTimes++ < getRequestQuota() && nextToken != null) {
	// System.out.println(getClass().getName() + ": getNextResult(),
	// mwsCalledTimes=" + mwsCalledTimes);// TODO
	// Result nextResult = getNextResult(nextToken);
	//
	// nextToken = nextResult.getNextToken();
	// dataList.addAll(nextResult.getDataList());
	//
	// System.out.println(
	// getClass().getName() + ": updateDatabaseAsync(), mwsCalledTimes=" +
	// mwsCalledTimes);// TODO
	// // Update database according to the result from MWS, Asynchronously
	// updateDatabaseAsync(nextResult.getDataList(), mwsCalledTimes);
	// }
	// //
	// // mwsCalledTimes>=getRequestQuota() || nextToken==null
	// //
	// // Condition:mwsCalledTimes>getRequestQuota() && nextToken==null
	// if (mwsCalledTimes > getRequestQuota() && nextToken != null) {
	// // Reach request quota and there is nextToken still
	// callByRestorePeriodAsync(nextToken, mwsCalledTimes);
	// }
	//
	// // Change result
	// result.setDataList(dataList);
	// result.setNextToken(nextToken);
	// } else {// mwsCalledTimes>=getRequestQuota() && nextToken==null
	// // Condition:mwsCalledTimes>=getRequestQuota() && nextToken==null
	//
	// }
	//
	// // Reach request quota and there is nextToken still
	// callByRestorePeriodAsync(nextToken, mwsCalledTimes);
	//
	// // // Reach request quota and there is nextToken still
	// // if (mwsCalledTimes >= getRequestQuota() && nextToken != null) {
	// //
	// // callByRestorePeriodAsync(nextToken);
	// //
	// // }
	//
	// // reset for next amazonOrderId to call getOrderItems
	// nextToken = null;// TODO
	// dataList = new ArrayList<>();
	// result = new Result();
	// }
	//
	// }
	//
	// @Override
	// public MWSTimerTask<OrderItem>.Result getFirstResult() {
	// // Make the call ListOrderItems
	// ListOrderItemsResponse response =
	// ListOrderItemsMWS.listOrderItems(amazonOrderId);
	// ListOrderItemsResult listOrderItemsResult =
	// response.getListOrderItemsResult();
	//
	// // Construct result
	// MWSTimerTask<OrderItem>.Result result = new Result();
	// result.setDataList(listOrderItemsResult.getOrderItems());
	// result.setNextToken(listOrderItemsResult.getNextToken());
	//
	// // Return result
	// return result;
	// }
	//
	// @Override
	// public MWSTimerTask<OrderItem>.Result getNextResult(String nextToken) {
	// // Make the call ListOrderItemsByNextToken
	// ListOrderItemsByNextTokenResponse nextTokenResponse =
	// ListOrderItemsMWS.listOrderItemsByNextToken(nextToken);
	// ListOrderItemsByNextTokenResult nextTokenResult =
	// nextTokenResponse.getListOrderItemsByNextTokenResult();
	//
	// // Construct result
	// MWSTimerTask<OrderItem>.Result result = new Result();
	// result.setDataList(nextTokenResult.getOrderItems());
	// result.setNextToken(nextTokenResult.getNextToken());
	//
	// // Return result
	// return result;
	// }
	//
	// @Override
	// protected int updateDatabase(List<OrderItem> dataList) {
	// return new ListOrderItemsDatabase().insert(dataList, amazonOrderId);
	// }

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
	protected void work() {
		// TODO Auto-generated method stub

	}

}
