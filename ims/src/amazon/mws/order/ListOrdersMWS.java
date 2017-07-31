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
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;

import amazon.mws.SellerConfig;

/**
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
 * The ListOrders and ListOrdersByNextToken operations together share a maximum
 * request quota of six and a restore rate of one request every minute.
 * 
 * MaxResultsPerPage: A number that indicates the maximum number of orders that
 * can be returned per page. Value must be 1 - 100. Default: 100
 */
public class ListOrdersMWS {
	/**
	 * Throttling
	 */
	public static final int REQUEST_QUOTA = 6;
	public static final int RESTORE_QUOTA = 1;
	public static final int RESTORE_PERIOD = 60;
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * MaxResultsPerPage
	 */
	public static final int MAX_RESULTS_PER_PAGE = 10;// TODO for test

	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	private static String marketplaceId = SellerConfig.marketplaceId;

	/**
	 * Call the service, log response and exceptions.
	 *
	 * @param client
	 * @param request
	 * @return The response.
	 */
	private static ListOrdersResponse invokeListOrders(MarketplaceWebServiceOrders client, ListOrdersRequest request) {
		try {
			// Call the service.
			ListOrdersResponse response = client.listOrders(request);
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
	 * @return The response.
	 */
	private static ListOrdersByNextTokenResponse invokeListOrdersByNextToken(MarketplaceWebServiceOrders client,
			ListOrdersByNextTokenRequest request) {
		try {
			// Call the service.
			ListOrdersByNextTokenResponse response = client.listOrdersByNextToken(request);
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
	 * MWS system uses T time zone (UTC+0); Amazon.com system uses PST time zone
	 * (UTC-8)
	 *
	 * @param createdAfter
	 *            Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
	 *            range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
	 *            minutes).
	 * @param createdBefore
	 *            Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
	 *            range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
	 *            minutes).
	 * @return
	 */
	public static ListOrdersResponse listShippedOrders(XMLGregorianCalendar createdAfter,
			XMLGregorianCalendar createdBefore) throws MarketplaceWebServiceOrdersException {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrdersRequest request = new ListOrdersRequest();
		request.setSellerId(sellerId);
		System.out.println("createdAfter: " + createdAfter);
		System.out.println("createdBefore: " + createdBefore);
		request.setCreatedAfter(createdAfter);
		request.setCreatedBefore(createdBefore);
		List<String> orderStatus = new ArrayList<String>();
		orderStatus.add(OrderStatus.Shipped.value());
		request.setOrderStatus(orderStatus);
		List<String> marketplaceIds = new ArrayList<String>();
		marketplaceIds.add(marketplaceId);
		request.setMarketplaceId(marketplaceIds);
		// List<String> fulfillmentChannel = new ArrayList<String>();
		// request.setFulfillmentChannel(fulfillmentChannel);
		// String buyerEmail = "example";
		// request.setBuyerEmail(buyerEmail);
		// String sellerOrderId = "example";
		// request.setSellerOrderId(sellerOrderId);
		request.setMaxResultsPerPage(MAX_RESULTS_PER_PAGE);

		// Make the call.
		ListOrdersResponse response = invokeListOrders(client, request);
		return response;
	}

	/**
	 * MWS system uses T time zone (UTC+0); Amazon.com system uses PST time zone
	 * (UTC-8)
	 *
	 * @param createdAfter
	 *            Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
	 *            range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
	 *            minutes).
	 * @param createdBefore
	 *            Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
	 *            range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
	 *            minutes).
	 * @return
	 */
	public static ListOrdersResponse listOrders(XMLGregorianCalendar createdAfter, XMLGregorianCalendar createdBefore)
			throws MarketplaceWebServiceOrdersException {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrdersRequest request = new ListOrdersRequest();
		request.setSellerId(sellerId);
		request.setCreatedAfter(createdAfter);
		request.setCreatedBefore(createdBefore);
		// List<String> orderStatus = new ArrayList<String>();
		// orderStatus.add(OrderStatus.Shipped.value());
		// request.setOrderStatus(orderStatus);
		List<String> marketplaceIds = new ArrayList<String>();
		marketplaceIds.add(marketplaceId);
		request.setMarketplaceId(marketplaceIds);
		// List<String> fulfillmentChannel = new ArrayList<String>();
		// request.setFulfillmentChannel(fulfillmentChannel);
		// String buyerEmail = "example";
		// request.setBuyerEmail(buyerEmail);
		// String sellerOrderId = "example";
		// request.setSellerOrderId(sellerOrderId);
		request.setMaxResultsPerPage(MAX_RESULTS_PER_PAGE);

		// Make the call.
		ListOrdersResponse response = invokeListOrders(client, request);
		return response;
	}

	/**
	 * @param nextToken
	 * @return
	 */
	public static ListOrdersByNextTokenResponse listOrdersByNextToken(String nextToken)
			throws MarketplaceWebServiceOrdersException {
		// Get a client connection.
		// Make sure you've set the variables in MWSOrderConfig.
		MarketplaceWebServiceOrdersClient client = MWSOrderConfig.getClient();

		// Create a request.
		ListOrdersByNextTokenRequest request = new ListOrdersByNextTokenRequest();
		request.setSellerId(sellerId);
		request.setNextToken(nextToken);

		// Make the call.
		ListOrdersByNextTokenResponse response = invokeListOrdersByNextToken(client, request);
		return response;
	}
}