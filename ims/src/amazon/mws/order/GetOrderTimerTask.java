/**
 * 
 */
package amazon.mws.order;

import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.mws.MWSTimerTask;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 11:01:56 AM
 */
public class GetOrderTimerTask extends MWSTimerTask<Order> {

	@Override
	protected void beforeWork() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void work() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterWork() {
		// TODO Auto-generated method stub

	}

	@Override
	protected TimeUnit getTimeUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getRequestQuota() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getRestorePeriod() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getRestoreQuota() {
		// TODO Auto-generated method stub
		return 0;
	}

}
