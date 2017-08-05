/**
 * 
 */
package amazon.mws.order;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

import amazon.db.query.OrderQuerier;
import amazon.mws.MWSTimerTask;

/**
 * Insert order items that order is not pending status and order has no related
 * order items into IMS from MWS Orders API
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 10:23:48 PM
 */
public class ListOrderItemsTimerTask extends MWSTimerTask {
	private static final Log log = LogFactory.getLog(ListOrderItemsTimerTask.class);
	private static final ListOrderItemsTimerTask listOrderItemsTimerTask = new ListOrderItemsTimerTask();

	private int mwsCalledTimes;
	private List<String> amazonOrderIdList;
	private WorkType workType = WorkType.INSERT_BY_INTERNAL_SET_AMAZON_ORDER_ID;

	public enum WorkType {
		INSERT_BY_INTERNAL_SET_AMAZON_ORDER_ID, INSERT_BY_EXTERNAL_SET_AMAZON_ORDER_ID
	}

	private ListOrderItemsTimerTask() {
	}

	public void setAmazonOrderIdList(List<String> amazonOrderIdList) {
		this.amazonOrderIdList = amazonOrderIdList;
	}

	public static ListOrderItemsTimerTask getInstance(WorkType workType) {
		// set value for workType
		listOrderItemsTimerTask.workType = workType;
		return listOrderItemsTimerTask;
	}

	/**
	 * No asynchronous thread will be produced by this method
	 * 
	 * @throws SQLException
	 * @throws NamingException
	 */
	@Override
	protected void work() throws SQLException, NamingException {
		// initialization
		log.info("workType is " + workType.name());
		mwsCalledTimes = 0;
		if (workType == WorkType.INSERT_BY_INTERNAL_SET_AMAZON_ORDER_ID) {
			// amazonOrderIdList set from internal
			setAmazonOrderIdList(
					OrderQuerier.selectOldestNonPendingOrdersWithoutItems(ListOrderItemsMWS.REQUEST_QUOTA));
			if (amazonOrderIdList == null || amazonOrderIdList.size() < 1) {
				log.info("All non-pending orders have related order items.");
			}
		} else if (workType == WorkType.INSERT_BY_EXTERNAL_SET_AMAZON_ORDER_ID) {
			// amazonOrderIdList should be set from the external caller
			if (amazonOrderIdList == null || amazonOrderIdList.size() < 1) {
				log.info("External caller didn't select any non-pending orders without related order items.");
			}
		}

		// save all orderItems by amazonOrderId list
		if (!(amazonOrderIdList == null || amazonOrderIdList.size() < 1)) {
			log.info(amazonOrderIdList.size() + " non-pending orders without related order items are selected.");
			for (String amazonOrderId : amazonOrderIdList) {
				if (++mwsCalledTimes <= ListOrderItemsMWS.REQUEST_QUOTA) {
					log.info(getLogPrefix(amazonOrderId) + "insertOrderItemsByOrderId started");
					insertOrderItemsByOrderId(amazonOrderId);
					log.info(getLogPrefix(amazonOrderId) + "insertOrderItemsByOrderId ended");
				} else {
					break;
				}
			}
		}

		/** Set the task to ready for the next scheduled call */
		log.info("ready()");
		ready();
	}

	private void insertOrderItemsByOrderId(String amazonOrderId) {
		// Make the call to get first result
		log.info(getLogPrefix(amazonOrderId) + "getFirstResult()");
		ListOrderItemsResult result = getFirstResult(amazonOrderId);// may cause MarketplaceWebServiceOrdersException
		List<OrderItem> orderItems = result.getOrderItems();
		String nextToken = result.getNextToken();

		while (nextToken != null) {
			if (++mwsCalledTimes <= ListOrderItemsMWS.REQUEST_QUOTA) {
				// Make the call to get next result by next token
				log.info(getLogPrefix(amazonOrderId) + "getNextResult(), nextToken=" + nextToken);
				ListOrderItemsByNextTokenResult nextResult = getNextResult(nextToken);

				// add orderItems
				orderItems.addAll(nextResult.getOrderItems());

				// set new nextToken
				nextToken = nextResult.getNextToken();
			} else {
				log.info(getLogPrefix(amazonOrderId) + "reached request quota");
				return;
			}
		}

		// createBefor should be same for every response
		if (nextToken == null) {
			log.info(getLogPrefix(amazonOrderId) + "insertOrderItemsIntoDatabase()");
			insertOrderItemsIntoDatabase(orderItems, amazonOrderId);
		}
	}

	private int insertOrderItemsIntoDatabase(List<OrderItem> orderItems, String amazonOrderId) {
		return new ListOrderItemsDatabase().insert(orderItems, amazonOrderId);
	}

	private ListOrderItemsResult getFirstResult(String amazonOrderId) {
		// Make the call ListOrderItems
		ListOrderItemsResponse response = ListOrderItemsMWS.listOrderItems(amazonOrderId);
		ListOrderItemsResult listOrderItemsResult = response.getListOrderItemsResult();

		// Return result
		return listOrderItemsResult;
	}

	private ListOrderItemsByNextTokenResult getNextResult(String nextToken) {
		// Make the call ListOrderItemsByNextToken
		ListOrderItemsByNextTokenResponse nextTokenResponse = ListOrderItemsMWS.listOrderItemsByNextToken(nextToken);
		ListOrderItemsByNextTokenResult nextTokenResult = nextTokenResponse.getListOrderItemsByNextTokenResult();

		// Return result
		return nextTokenResult;
	}

	private String getLogPrefix(String amazonOrderId) {
		return "mwsCalledTimes=" + mwsCalledTimes + ", amazonOrderId=" + amazonOrderId;
	}

	/**
	 * It is safe to reset because no asynchronous thread will be produced by work
	 * method
	 */
	@Override
	protected void afterWork() {
		// reset
		log.info("reset()");
		reset();
	}

	private void reset() {
		mwsCalledTimes = 0;
		amazonOrderIdList = null;
		workType = WorkType.INSERT_BY_INTERNAL_SET_AMAZON_ORDER_ID;
	}
}
