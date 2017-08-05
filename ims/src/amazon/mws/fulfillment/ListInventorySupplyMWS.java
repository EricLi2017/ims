package amazon.mws.fulfillment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWS;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSClient;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSException;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyRequest;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ResponseHeaderMetadata;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.SellerSkuList;

import amazon.mws.SellerConfig;

/**
 * Throttling
 * 
 * The ListInventorySupply operation has a maximum request quota of 30 and a
 * restore rate of two requests every second.
 * 
 * Seller SKUs for items that you have shipped to an Amazon fulfillment center.
 * Maximum: 50
 * 
 * Note: There is no NextToken if the request SKU number is not more than the
 * Maximum 50
 */
public class ListInventorySupplyMWS {
	private static final Log log = LogFactory.getLog(ListInventorySupplyMWS.class);
	/**
	 * Throttling
	 */
	public static final int REQUEST_QUOTA = 30;
	public static final int RESTORE_QUOTA = 2;
	public static final int RESTORE_PERIOD = 1;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	/**
	 * A list of seller SKUs for items that you want inventory availability
	 * information about.
	 * 
	 * Seller SKUs for items that you have shipped to an Amazon fulfillment center.
	 * Maximum: 50
	 */
	public static final int SKU_MAX_NUM = 50;

	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	private static String marketplaceId = SellerConfig.marketplaceId;

	/**
	 * Indicates whether or not you want the ListInventorySupply operation to return
	 * the SupplyDetail element.
	 * 
	 * ResponseGroup values:
	 * 
	 * Basic - Does not include the SupplyDetail element in the response
	 * 
	 * Detailed - Includes the SupplyDetail element in the response
	 * 
	 * Default: Basic
	 */
	private static String responseGroup = "Basic";

	/**
	 * Call the service, log response and exceptions.
	 *
	 * @param client
	 * @param request
	 *
	 * @return The response.
	 */
	private static ListInventorySupplyResponse invokeListInventorySupply(FBAInventoryServiceMWS client,
			ListInventorySupplyRequest request) {
		try {
			// Call the service.
			ListInventorySupplyResponse response = client.listInventorySupply(request);
			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every
			// call.
			log.info("Response:");
			log.info("RequestId: " + rhmd.getRequestId());
			log.info("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			log.info(responseXml);
			return response;
		} catch (FBAInventoryServiceMWSException ex) {
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

	/**
	 * 
	 * @param skus
	 *            max size 50
	 * @return
	 */
	public static ListInventorySupplyResponse listInventory(List<String> skus) throws FBAInventoryServiceMWSException {
		// Get a client connection.
		// Make sure you've set the variables in
		// FBAInventoryServiceMWSSampleConfig. (changed to MWSConfig)
		FBAInventoryServiceMWSClient client = MWSFulfillmentConfig.getClient();

		// Create a request.
		ListInventorySupplyRequest request = new ListInventorySupplyRequest();
		request.setSellerId(sellerId);
		request.setMarketplaceId(marketplaceId);
		SellerSkuList sellerSkus = new SellerSkuList(skus);// max 50
		request.setSellerSkus(sellerSkus);
		request.setResponseGroup(responseGroup);

		// Make the call.
		ListInventorySupplyResponse response = ListInventorySupplyMWS.invokeListInventorySupply(client, request);
		return response;
	}
}
