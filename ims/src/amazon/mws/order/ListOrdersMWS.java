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
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.*;
import com.amazonservices.mws.orders._2013_09_01.*;
import com.amazonservices.mws.orders._2013_09_01.model.*;

import amazon.mws.SellerConfig;

/**
 * Sample call for ListOrders.
 */
public class ListOrdersMWS {
	/** Seller Seller ID. */
	private static String sellerId = SellerConfig.sellerId;
	/** Seller Marketplace ID. */
	private static String marketplaceId = SellerConfig.marketplaceId;
	
    // 该 ListOrders 和 ListOrdersByNextToken 操作的最大请求限额为 6 个，恢复速率为每分钟 1 个请求
    private static Integer maxResultsPerPage = 100;// 1 to 100//TODO

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
     * @param createdAfter  Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
     *                      range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
     *                      minutes).
     * @param createdBefore Number of minutes or DatatypeConstants.FIELD_UNDEFINED. Value
     *                      range from -14 hours (-14 * 60 minutes) to 14 hours (14 * 60
     *                      minutes).
     * @return
     */
    public static ListOrdersResponse listShippedOrders(XMLGregorianCalendar createdAfter, XMLGregorianCalendar createdBefore) {
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
        request.setMaxResultsPerPage(maxResultsPerPage);

        // Make the call.
        ListOrdersResponse response = invokeListOrders(client, request);
        return response;
    }

    /**
     * @param nextToken
     * @return
     */
    public static ListOrdersByNextTokenResponse listOrdersByNextToken(String nextToken) {
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

    /**
     * Command line entry point.
     */
    public static void main(String[] args) {

        XMLGregorianCalendar createdAfter = MwsUtl.getDTF().newXMLGregorianCalendar();
        createdAfter.setTimezone(-8 * 60);// PST time zone UTC-8
        createdAfter.setYear(2017);
        createdAfter.setMonth(2);
        createdAfter.setDay(3);
        createdAfter.setTime(0, 0, 0);
        XMLGregorianCalendar createdBefore = MwsUtl.getDTF().newXMLGregorianCalendar();
        createdBefore.setTimezone(-8 * 60);// PST time zone UTC-8
        createdBefore.setYear(2017);
        createdBefore.setMonth(2);
        createdBefore.setDay(4);
        createdBefore.setTime(0, 0, 0);

        // Make the call.
        ListOrdersResponse response = ListOrdersMWS.listShippedOrders(createdAfter, createdBefore);

        ListOrdersResult listOrdersResult = response.getListOrdersResult();
        boolean hasNextOrders = listOrdersResult.isSetNextToken();
        String nextToken = listOrdersResult.getNextToken();
        List<Order> orders = listOrdersResult.getOrders();
        for (Order order : orders) { // TODO
            System.out.println(order.getAmazonOrderId());
            System.out.println(order.getSellerOrderId());
            System.out.println(order.getPurchaseDate());
            System.out.println(order.getLastUpdateDate());
            System.out.println(order.getSalesChannel());// :Amazon.com
            System.out.println(order.getFulfillmentChannel());// :AFN
            System.out.println(order.getIsBusinessOrder());
            System.out.println(order.getIsPremiumOrder());
            System.out.println(order.getIsPrime());
            System.out.println(order.getBuyerName());
            System.out.println(order.getBuyerEmail());
            // System.out.println(order.getPurchaseOrderNumber());// NULL
            // System.out.println(order.getOrderChannel());// NULL
            // System.out.println(order.getOrderType());//:StandardOrder
            System.out.println(order.getOrderStatus());// Shipped
            System.out.println(order.getOrderTotal().getCurrencyCode());
            System.out.println(order.getOrderTotal().getAmount());
            // :Expedited FreeEconomy NextDay SameDay SecondDay Scheduled
            // Standard
            System.out.println(order.getShipmentServiceLevelCategory());
            System.out.println(order.getShipServiceLevel());// :SecondDay...
            System.out.println(order.getNumberOfItemsShipped());
            System.out.println(order.getNumberOfItemsUnshipped());
            // System.out.println(order.getPaymentMethod());//:other...
            // System.out.println(order.getPaymentExecutionDetail());//only
            // available for CN and JP
            System.out.println(order.getShippingAddress().toXML());// 11
            // attributes
        }
        System.out.println("hasNextOrders= " + hasNextOrders);
        System.out.println("nextToken= " + nextToken);

        while (hasNextOrders && nextToken != null) {
            ListOrdersByNextTokenResponse nextTokenResponse = listOrdersByNextToken(nextToken);
            ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
            hasNextOrders = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            List<Order> nextOrders = nextTokenResult.getOrders();
            for (Order order : nextOrders) {// TODO
                System.out.println(order.getAmazonOrderId());
                System.out.println(order.getSellerOrderId());
                System.out.println(order.getPurchaseDate());
                System.out.println(order.getLastUpdateDate());
                System.out.println(order.getSalesChannel());// :Amazon.com
                System.out.println(order.getFulfillmentChannel());// :AFN
                System.out.println(order.getIsBusinessOrder());
                System.out.println(order.getIsPremiumOrder());
                System.out.println(order.getIsPrime());
                System.out.println(order.getBuyerName());
                System.out.println(order.getBuyerEmail());
                // System.out.println(order.getPurchaseOrderNumber());// NULL
                // System.out.println(order.getOrderChannel());// NULL
                // System.out.println(order.getOrderType());//:StandardOrder
                System.out.println(order.getOrderStatus());// Shipped
                System.out.println(order.getOrderTotal().getCurrencyCode());
                System.out.println(order.getOrderTotal().getAmount());
                // :Expedited FreeEconomy NextDay SameDay SecondDay Scheduled
                // Standard
                System.out.println(order.getShipmentServiceLevelCategory());
                System.out.println(order.getShipServiceLevel());// :SecondDay...
                System.out.println(order.getNumberOfItemsShipped());
                System.out.println(order.getNumberOfItemsUnshipped());
                // System.out.println(order.getPaymentMethod());//:other...
                // System.out.println(order.getPaymentExecutionDetail());//only
                // available for CN and JP
                System.out.println(order.getShippingAddress().toXML());// 11
                // attributes

            }
            System.out.println("hasNextOrders= " + hasNextOrders);
            System.out.println("nextToken= " + nextToken);
        }

        // try {
        // Thread.currentThread();
        // Thread.sleep(10000);
        // } catch (InterruptedException ie) {
        // ie.printStackTrace();
        // }

    }
}