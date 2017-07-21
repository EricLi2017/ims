package amazon.mws.fulfillment;

import java.util.ArrayList;
import java.util.List;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWS;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSClient;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSException;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyRequest;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ResponseHeaderMetadata;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.SellerSkuList;

import amazon.mws.SellerConfig;

public class ListInventoryMWS {
	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	private static String marketplaceId = SellerConfig.marketplaceId;

	// 该 ListInventorySupply 和 ListInventorySupplyByNextToken 操作的 最大请求限额为 30 个，恢复速率为每秒钟 2 个请求。
	//
	// 为您已经发运至亚马逊配送中心的商品指定的卖家 SKU。最大值：50
	// 如果每次按最大值请求，就可以一次全部返回，没有NextToken
	public static int SKU_MAX_NUM = 50;
	// Basic or Detailed
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
			System.out.println("Response:");
			System.out.println("RequestId: " + rhmd.getRequestId());
			System.out.println("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			System.out.println(responseXml);
			return response;
		} catch (FBAInventoryServiceMWSException ex) {
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

	/**
	 * 
	 * @param skus
	 *            max size 50
	 * @return
	 */
	public static ListInventorySupplyResponse listInventory(List<String> skus) {
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
		ListInventorySupplyResponse response = ListInventoryMWS.invokeListInventorySupply(client, request);
		return response;
	}

	/**
	 * Command line entry point.
	 */
	public static void main(String[] args) {

		List<String> skus = new ArrayList<String>();
		skus.add("0B-K1QZ-4RUE");
		ListInventorySupplyResponse response = ListInventoryMWS.listInventory(skus);

		List<InventorySupply> member = response.getListInventorySupplyResult().getInventorySupplyList().getMember();
		for (InventorySupply supply : member) {
			System.out.println(supply.getASIN());
			System.out.println(supply.getCondition());
			System.out.println(supply.getEarliestAvailability() == null ? null
					: supply.getEarliestAvailability().getTimepointType());
			System.out.println(supply.getFNSKU());
			System.out.println(supply.getInStockSupplyQuantity());
			System.out.println(supply.getSellerSKU());
			System.out.println(supply.getTotalSupplyQuantity());
		}
	}

}
