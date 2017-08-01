/**
 * 
 */
package amazon.mws.order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.db.query.OrderQuerier;
import amazon.mws.MWSTimerTask;
import amazon.mws.order.ListOrderItemsTimerTask.WorkType;
import common.util.Page;

/**
 * Update orders that status changed from pending to non-pending, and schedule
 * ListOrderItemsTimerTask to asynchronously insert order items for these orders
 * 
 * Created by Eclipse on Jul 31, 2017 at 2:12:53 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class GetOrderTimerTask extends MWSTimerTask {
	private final ScheduledExecutorService scheduledExecutorService;

	private static GetOrderTimerTask getOrderTimerTask = new GetOrderTimerTask();

	private GetOrderTimerTask() {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
	}

	public static GetOrderTimerTask getInstance() {
		return getOrderTimerTask;
	}

	@Override
	protected void work() throws ClassNotFoundException, SQLException {
		// init
		int mwsCalledTimes = 0;
		List<String> pendingAmazonOrderIds = OrderQuerier
				.selectOldestPendingOrders(GetOrderMWS.REQUEST_QUOTA * GetOrderMWS.MAX_SIZE_AMAZON_ORDER_ID_LIST);
		System.out.println(getLogPrefix() + ": " + pendingAmazonOrderIds.size() + " pendingAmaonOderIds are selected.");

		// Update orders that status changed from pending to non-pending, and schedule
		// ListOrderItemsTimerTask to asynchronously insert order items for these orders
		int total = pendingAmazonOrderIds.size();
		int subSize = GetOrderMWS.MAX_SIZE_AMAZON_ORDER_ID_LIST;
		int subMaxIndex = total % subSize == 0 ? total / subSize : total / subSize + 1;
		int subIndex = 0;
		while (++mwsCalledTimes <= GetOrderMWS.REQUEST_QUOTA && ++subIndex <= subMaxIndex) {
			// get subPendingAmazonOrderIds
			List<String> subPendingAmazonOrderIds = Page.getSub(pendingAmazonOrderIds, subIndex, subSize);
			System.out.println(getLogPrefix() + ": (" + subIndex + "/" + subMaxIndex + ") process the sub "
					+ subPendingAmazonOrderIds.size() + " pendingAmaonOderIds");

			// call
			GetOrderResponse response = GetOrderMWS.getOrder(subPendingAmazonOrderIds);
			List<Order> nonPendingOrders = getNonPendingOrders(response.getGetOrderResult().getOrders());
			List<String> nonPendingAmazonOrderIds = getAmazonOrderIds(nonPendingOrders);
			System.out.println(getLogPrefix() + ": found " + nonPendingAmazonOrderIds.size() + "/"
					+ subPendingAmazonOrderIds.size() + " orders in MWS changed from pending to non-pending");

			// update orders
			if (nonPendingOrders != null && nonPendingOrders.size() > 0) {
				int update = updateOrders(nonPendingOrders);
				System.out.println(getLogPrefix() + ": updated " + update + "/" + nonPendingOrders.size()
						+ " orders in IMS from pending to non-pending");
			}

			// schedule async ListOrderItemsTimerTask to insert order items
			if (nonPendingAmazonOrderIds != null && nonPendingAmazonOrderIds.size() > 0) {
				System.out.println(
						getLogPrefix() + ": scheduleListOrderItemsTimerTask(), nonPendingAmazonOrderIds.size()="
								+ nonPendingAmazonOrderIds.size());
				scheduleListOrderItemsTimerTask(nonPendingAmazonOrderIds);
			}
		}

		// set ready for the next scheduled task running
		System.out.println(getLogPrefix() + ": ready()");
		ready();
	}

	/**
	 * index starts from 1
	 * 
	 * @param list
	 * @param index
	 * @param subSize
	 * @return
	 */
	// private static List<String> getSub(List<String> list, int index, int subSize)
	// {
	// if (list == null)
	// return null;
	// int maxIndex = list.size() % subSize == 0 ? list.size() / subSize :
	// list.size() / subSize + 1;
	// int endIndex = (index == maxIndex ? list.size() : index * subSize);
	//
	// return list.subList((index - 1) * subSize, endIndex);
	// }

	private static List<Order> getNonPendingOrders(List<Order> orders) {
		if (orders == null)
			return null;

		List<Order> nonPendingOrders = new ArrayList<>();
		for (Order order : orders) {
			if (!OrderStatus.Pending.name().equalsIgnoreCase(order.getOrderStatus())) {
				nonPendingOrders.add(order);
			}
		}
		return nonPendingOrders;
	}

	private static List<String> getAmazonOrderIds(List<Order> orders) {
		if (orders == null)
			return null;

		List<String> amazonOrderIds = new ArrayList<>();
		for (Order order : orders) {
			amazonOrderIds.add(order.getAmazonOrderId());
		}
		return amazonOrderIds;
	}

	private static int updateOrders(List<Order> orders) throws SQLException {
		return ListOrdersDatabase.deleteAndInsert(orders);
	}

	private void scheduleListOrderItemsTimerTask(List<String> amazonOrderIdList) {
		ListOrderItemsTimerTask listOrderItemsTimerTask = ListOrderItemsTimerTask
				.getInstance(WorkType.INSERT_BY_EXTERNAL_SET_AMAZON_ORDER_ID);
		listOrderItemsTimerTask.setAmazonOrderIdList(amazonOrderIdList);
		scheduledExecutorService.schedule(listOrderItemsTimerTask, 1, TimeUnit.SECONDS);
	}
}