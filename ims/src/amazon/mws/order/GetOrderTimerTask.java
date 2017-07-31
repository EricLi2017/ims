/**
 * 
 */
package amazon.mws.order;

import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.mws.MWSTimerTask;

/**
 * Update orders that status changed from pending to non-pending, and call
 * ListOrderItemsTimerTask to insert order items for these orders
 * 
 * Created by Eclipse on Jul 31, 2017 at 2:12:53 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class GetOrderTimerTask extends MWSTimerTask<Order> {
	private static GetOrderTimerTask getOrderTimerTask = new GetOrderTimerTask();

	private GetOrderTimerTask() {
	}

	public static GetOrderTimerTask getInstance() {
		return getOrderTimerTask;
	}

	@Override
	protected void work() throws Exception {
		// TODO Auto-generated method stub

	}

}
