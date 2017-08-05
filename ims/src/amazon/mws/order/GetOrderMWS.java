/**
 * 
 */
package amazon.mws.order;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrders;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersException;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderRequest;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ResponseHeaderMetadata;

import amazon.mws.SellerConfig;

/**
 * 
 * The following response elements are not available for orders with an
 * OrderStatus of Pending but are available for orders with an OrderStatus of
 * Unshipped, Partially Shipped, or Shipped state:
 * 
 * <pre>
 * BuyerEmail, 
 * BuyerName, 
 * ShippingAddress, 
 * OrderTotal
 * </pre>
 * 
 * 
 * Throttling
 * 
 * The GetOrder operation has a maximum request quota of six and a restore rate
 * of one request every minute.
 * 
 * Maximum size of amazonOrderId list: 50
 * 
 * Created by Eclipse on Jul 31, 2017 at 1:10:00 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class GetOrderMWS {
	private static final Log log = LogFactory.getLog(GetOrderMWS.class);
	/**
	 * Throttling
	 */
	public static final int REQUEST_QUOTA = 6;
	public static final int RESTORE_QUOTA = 1;
	public static final int RESTORE_PERIOD = 60;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * Maximum size of amazonOrderId list
	 */
	public static final int MAX_SIZE_AMAZON_ORDER_ID_LIST = 50;

	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	// private static String marketplaceId = SellerConfig.marketplaceId;

	/**
	 * Call the service, log response and exceptions.
	 *
	 * @param client
	 * @param request
	 *
	 * @return The response.
	 */
	private static GetOrderResponse invokeGetOrder(MarketplaceWebServiceOrders client, GetOrderRequest request) {
		try {
			// Call the service.
			GetOrderResponse response = client.getOrder(request);
			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every call.
			log.info("Response:");
			log.info("RequestId: " + rhmd.getRequestId());
			log.info("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			log.info(responseXml);
			return response;
		} catch (MarketplaceWebServiceOrdersException ex) {
			// Exception properties are important for diagnostics.
			log.error("Service Exception:");
			ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
			if (rhmd != null) {
				log.error("RequestId: " + rhmd.getRequestId());
				log.error("Timestamp: " + rhmd.getTimestamp());
			}
			log.error("Message: " + ex.getMessage());
			log.error("StatusCode: " + ex.getStatusCode());
			log.error("ErrorCode: " + ex.getErrorCode());
			log.error("ErrorType: " + ex.getErrorType());
			throw ex;
		}
	}

	public static GetOrderResponse getOrder(List<String> amazonOrderIdList) {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		GetOrderRequest request = new GetOrderRequest();
		request.setSellerId(sellerId);
		request.setAmazonOrderId(amazonOrderIdList);

		// Make the call.
		return invokeGetOrder(client, request);
	}
}
