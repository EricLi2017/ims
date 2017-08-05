/*******************************************************************************
 * Copyright 2009-2015 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Marketplace Web Service Orders
 * API Version: 2013-09-01
 * Library Version: 2015-09-24
 * Generated: Fri Sep 25 20:06:20 GMT 2015
 */
package amazon.mws.order;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;

import amazon.mws.SellerConfig;

/**
 * The following response elements are not available for orders with an
 * OrderStatus of Pending but are available for orders with an OrderStatus of
 * Unshipped, Partially Shipped, or Shipped state:
 * 
 * <pre>
 * ItemTax
 * GiftWrapPrice
 * ItemPrice
 * PromotionDiscount
 * GiftWrapTax
 * ShippingTax
 * ShippingPrice
 * ShippingDiscount
 * </pre>
 * 
 * 
 * Throttling
 * 
 * The ListOrderItems and ListOrderItemsByNextToken operations together share a
 * maximum request quota of 30 and a restore rate of one request every two
 * seconds.
 */
public class ListOrderItemsMWS {
	private static final Log log = LogFactory.getLog(ListOrderItemsMWS.class);
	/**
	 * Throttling
	 */
	public static final int REQUEST_QUOTA = 30;
	public static final int RESTORE_QUOTA = 1;
	public static final int RESTORE_PERIOD = 2;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

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
	private static ListOrderItemsResponse invokeListOrderItems(MarketplaceWebServiceOrders client,
			ListOrderItemsRequest request) {
		try {
			// Call the service.
			ListOrderItemsResponse response = client.listOrderItems(request);
			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every
			// call.
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

	/**
	 * Call the service, log response and exceptions.
	 *
	 * @param client
	 * @param request
	 *
	 * @return The response.
	 */
	private static ListOrderItemsByNextTokenResponse invokeListOrderItemsByNextToken(MarketplaceWebServiceOrders client,
			ListOrderItemsByNextTokenRequest request) {
		try {
			// Call the service.
			ListOrderItemsByNextTokenResponse response = client.listOrderItemsByNextToken(request);
			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
			// We recommend logging every the request id and timestamp of every
			// call.
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

	public static ListOrderItemsResponse listOrderItems(String amazonOrderId) {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrderItemsRequest request = new ListOrderItemsRequest();
		request.setSellerId(sellerId);
		request.setAmazonOrderId(amazonOrderId);
		log.info("amazonOrderId= " + amazonOrderId);

		// Make the call.
		ListOrderItemsResponse response = ListOrderItemsMWS.invokeListOrderItems(client, request);
		return response;
	}

	public static ListOrderItemsByNextTokenResponse listOrderItemsByNextToken(String nextToken) {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrderItemsByNextTokenRequest request = new ListOrderItemsByNextTokenRequest();
		request.setSellerId(sellerId);
		request.setNextToken(nextToken);

		// Make the call.
		ListOrderItemsByNextTokenResponse response = invokeListOrderItemsByNextToken(client, request);
		return response;
	}
}
