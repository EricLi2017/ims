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

import java.util.*;
import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;

/** Sample call for ListOrderItems. */
public class ListOrderItemsMWS {
	private static String sellerId = "ASPJUC5MEFXYK";
	// private static String marketplaceId = "ATVPDKIKX0DER";
	// 该 ListOrderItems 和 ListOrderItemsByNextToken 操作共享的最大请求限额为 30 个，恢复速率为每 2
	// 秒钟 1 个请求

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
			System.out.println("Response:");
			System.out.println("RequestId: " + rhmd.getRequestId());
			System.out.println("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			System.out.println(responseXml);
			return response;
		} catch (MarketplaceWebServiceOrdersException ex) {
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
			System.out.println("Response:");
			System.out.println("RequestId: " + rhmd.getRequestId());
			System.out.println("Timestamp: " + rhmd.getTimestamp());
			String responseXml = response.toXML();
			System.out.println(responseXml);
			return response;
		} catch (MarketplaceWebServiceOrdersException ex) {
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

	public static ListOrderItemsResponse listOrderItems(String amazonOrderId) {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrderItemsRequest request = new ListOrderItemsRequest();
		request.setSellerId(sellerId);
		request.setAmazonOrderId(amazonOrderId);
		System.out.println("amazonOrderId= " + amazonOrderId);

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

	/**
	 * Command line entry point.
	 */
	public static void main(String[] args) {

		// String amazonOrderId = "107-5499830-9104262";// 7 units, 1 sku
		String amazonOrderId = "002-1524942-4267460";// a canceled order

		// Make the call.
		ListOrderItemsResponse response = listOrderItems(amazonOrderId);

		ListOrderItemsResult listOrderItemsResult = response.getListOrderItemsResult();
		boolean hasNextOrderItems = listOrderItemsResult.isSetNextToken();
		String nextToken = listOrderItemsResult.getNextToken();
		List<OrderItem> orderItems = listOrderItemsResult.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			System.out.println(orderItem.toXML());

			System.out.println(orderItem.getASIN());
			System.out.println(orderItem.getSellerSKU());
			System.out.println(orderItem.getItemPrice().getCurrencyCode());
			System.out.println(orderItem.getItemPrice().getAmount());
			System.out.println(orderItem.getPromotionDiscount().getCurrencyCode());
			System.out.println(orderItem.getPromotionDiscount().getAmount());
			System.out.println(orderItem.getItemTax().getCurrencyCode());
			System.out.println(orderItem.getItemTax().getAmount());
			System.out.println(orderItem.getOrderItemId());
			System.out.println(orderItem.getTitle());
			System.out.println(orderItem.getQuantityOrdered());
			System.out.println(orderItem.getQuantityShipped());

		}
		System.out.println("hasNextOrderItems= " + hasNextOrderItems);
		System.out.println("nextToken= " + nextToken);

		while (hasNextOrderItems && nextToken != null) {
			ListOrderItemsByNextTokenResponse nextTokenResponse = listOrderItemsByNextToken(nextToken);
			ListOrderItemsByNextTokenResult nextTokenResult = nextTokenResponse.getListOrderItemsByNextTokenResult();
			hasNextOrderItems = nextTokenResult.isSetNextToken();
			nextToken = nextTokenResult.getNextToken();
			List<OrderItem> nextOrderItems = nextTokenResult.getOrderItems();
			for (OrderItem orderItem : nextOrderItems) {
				System.out.println(orderItem.toXML());

				System.out.println(orderItem.getASIN());
				System.out.println(orderItem.getSellerSKU());
				System.out.println(orderItem.getItemPrice().getCurrencyCode());
				System.out.println(orderItem.getItemPrice().getAmount());
				System.out.println(orderItem.getPromotionDiscount().getCurrencyCode());
				System.out.println(orderItem.getPromotionDiscount().getAmount());
				System.out.println(orderItem.getItemTax().getCurrencyCode());
				System.out.println(orderItem.getItemTax().getAmount());
				System.out.println(orderItem.getOrderItemId());
				System.out.println(orderItem.getTitle());
				System.out.println(orderItem.getQuantityOrdered());
				System.out.println(orderItem.getQuantityShipped());
			}
			System.out.println("hasNextOrderItems= " + hasNextOrderItems);
			System.out.println("nextToken= " + nextToken);
		}

	}

}
