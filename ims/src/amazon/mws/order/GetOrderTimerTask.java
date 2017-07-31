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
public class GetOrderTimerTask extends MWSTimerTask<Order> {
	private final ScheduledExecutorService scheduledExecutorService;

	private static GetOrderTimerTask getOrderTimerTask = new GetOrderTimerTask();

	private GetOrderTimerTask() {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
	}

	public static GetOrderTimerTask getInstance() {
		return getOrderTimerTask;
	}

	@Override
	protected void work() throws Exception {
		// init
		int mwsCalledTimes = 0;
		List<String> pendingAmazonOrderIds = OrderQuerier
				.selectOldestPendingOrders(GetOrderMWS.REQUEST_QUOTA * GetOrderMWS.MAX_SIZE_AMAZON_ORDER_ID_LIST);

		// Update orders that status changed from pending to non-pending, and schedule
		// ListOrderItemsTimerTask to asynchronously insert order items for these orders
		while (++mwsCalledTimes <= GetOrderMWS.REQUEST_QUOTA) {
			// get subPendingAmazonOrderIds
			List<String> subPendingAmazonOrderIds = getSub(pendingAmazonOrderIds, mwsCalledTimes,
					GetOrderMWS.MAX_SIZE_AMAZON_ORDER_ID_LIST);

			// call
			GetOrderResponse response = GetOrderMWS.getOrder(subPendingAmazonOrderIds);
			List<Order> nonPendingOrders = getNonPendingOrders(response.getGetOrderResult().getOrders());
			List<String> nonPendingAmazonOrderIds = getAmazonOrderIds(nonPendingOrders);

			// update orders
			updateOrders(nonPendingOrders);

			// schedule async ListOrderItemsTimerTask to insert order items
			scheduleListOrderItemsTimerTask(nonPendingAmazonOrderIds);
		}

	}

	/**
	 * index starts from 1
	 * 
	 * @param list
	 * @param index
	 * @param maxSize
	 * @return
	 */
	private static List<String> getSub(List<String> list, int index, int maxSize) {
		if (list == null)
			return null;

		return list.subList((index - 1) * maxSize, index * maxSize);
	}

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
		// TODO
		return ListOrdersDatabase.deleteAndInsert(orders);
	}

	private void scheduleListOrderItemsTimerTask(List<String> amazonOrderIdList) {
		// TODO
		ListOrderItemsTimerTask listOrderItemsTimerTask = ListOrderItemsTimerTask
				.getInstance(WorkType.INSERT_BY_EXTERNAL_SET_AMAZON_ORDER_ID);
		listOrderItemsTimerTask.setAmazonOrderIdList(amazonOrderIdList);
		scheduledExecutorService.schedule(listOrderItemsTimerTask, 1, TimeUnit.SECONDS);
	}
}