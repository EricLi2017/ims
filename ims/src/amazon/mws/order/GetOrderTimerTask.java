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
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 11:01:56 AM
 */
public class GetOrderTimerTask extends MWSTimerTask<Order> {

	@Override
	protected void work() {
		// TODO Auto-generated method stub

	}
}
