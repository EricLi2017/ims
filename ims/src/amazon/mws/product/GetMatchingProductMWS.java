/**
 * 
 */
package amazon.mws.product;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonservices.mws.products.MarketplaceWebServiceProducts;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ASINListType;
import com.amazonservices.mws.products.model.GetMatchingProductRequest;
import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;

import amazon.mws.SellerConfig;

/**
 * Returns a list of products and their attributes, based on a list of ASIN
 * values.
 * 
 * Throttling
 * 
 * <pre>
 * Operations in the Products API section that send lists of items as input parameters have restore rates that are measured by item.
 * 
 * Maximum request quota	Restore rate	Hourly request quota
 * 20 requests	Two items every second	7200 requests per hour
 * 
 * ASINList Maximum: 10 ASIN values
 * </pre>
 * 
 * Operations in the Products API section that send lists of items as input
 * parameters have restore rates that are measured by item. Created by Eclipse
 * on Aug 3, 2017 at 10:47:50 AM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class GetMatchingProductMWS {
	/**
	 * Throttling
	 */
	public static final int REQUEST_QUOTA = 20;
	public static final int REQUEST_QUOTA_HOURLY = 7200;
	public static final int RESTORE_QUOTA = 2;
	public static final int RESTORE_TYPE = 1;// 0:(requests, default); 1:(items)
	public static final int RESTORE_PERIOD = 1;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * Max size of ASIN list
	 */
	public static final int MAX_SIZE_ASIN_LIST = 10;

	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	private static String marketplaceId = SellerConfig.marketplaceId;

	/**
	 * Call the service, log response and exceptions.
	 *
	 * @param client
	 * @param request
	 *
	 * @return The response.
	 */
	private static GetMatchingProductResponse invokeGetMatchingProduct(MarketplaceWebServiceProducts client,
			GetMatchingProductRequest request) {
		try {
			// Call the service.
			GetMatchingProductResponse response = client.getMatchingProduct(request);
			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every call.
			System.out.println("Response:");
			System.out.println("RequestId: " + rhmd.getRequestId());
			System.out.println("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			System.out.println(responseXml);
			return response;
		} catch (MarketplaceWebServiceProductsException ex) {
			// Exception properties are important for diagnostics.
			System.out.println("Service Exception:");
			ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
			if (rhmd != null) {
				System.out.println("RequestId: " + rhmd.getRequestId());
				System.out.println("Timestamp: " + rhmd.getTimestamp());
			}
			System.out.println("Message: " + ex.getMessage());
			System.out.println("StatusCode: " + ex.getStatusCode());
			System.out.println("ErrorCode: " + ex.getErrorCode());
			System.out.println("ErrorType: " + ex.getErrorType());
			throw ex;
		}
	}

	public static GetMatchingProductResponse getMatchingProduct(List<String> asinList) {
		// Get a client connection.
		// Make sure you've set the variables in
		MarketplaceWebServiceProductsClient client = MWSProductsConfig.getClient();

		// Create a request.
		GetMatchingProductRequest request = new GetMatchingProductRequest();
		request.setSellerId(sellerId);
		request.setMarketplaceId(marketplaceId);
		ASINListType asinListType = new ASINListType();
		asinListType.setASIN(asinList);
		request.setASINList(asinListType);

		// Make the call.
		return GetMatchingProductMWS.invokeGetMatchingProduct(client, request);
	}

	/**
	 * A safe restore period can restore REQUEST_QUOTA * MAX_SIZE_ASIN_LIST items
	 * 
	 * milliseconds
	 * 
	 * @return
	 */
	public static long getSafeRestorePeriod() {
		return (REQUEST_QUOTA * MAX_SIZE_ASIN_LIST / RESTORE_QUOTA) * 1000;
	}
}
