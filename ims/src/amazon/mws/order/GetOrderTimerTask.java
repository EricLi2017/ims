/**
 * 
 */
package amazon.mws.order;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.orders._2013_09_01.model.Order;

import amazon.mws.MWSTimerTask;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 11:01:56 AM
 */
public class GetOrderTimerTask extends MWSTimerTask<Order> {

	@Override
	protected MWSTimerTask<Order>.Result getFirstResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MWSTimerTask<Order>.Result getNextResult(String nextToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int updateDatabase(List<Order> dataList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isLoop() {
		// TODO Auto-generated method stub
		return false;
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
