/*******************************************************************************
 * Copyright 2009-2016 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * FBA Inventory Service MWS
 * API Version: 2010-10-01
 * Library Version: 2014-09-30
 * Generated: Mon Mar 21 09:01:27 PDT 2016
 */
package amazon.mws.fulfillment;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSAsyncClient;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSClient;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.FBAInventoryServiceMWSConfig;

import amazon.mws.MWSConfig;

/**
 * Configuration for FBAInventoryServiceMWS
 */
public class MWSFulfillmentConfig extends MWSConfig {
	/**
	 * The endpoint for region service and version. ex: serviceURL =
	 * MWSEndpoint.NA_PROD.toString();
	 */
	private static final String serviceURL = serviceURL_Fulfillment;

	/** The client, lazy initialized. Async client is also a sync client. */
	private static FBAInventoryServiceMWSAsyncClient client = null;

	/**
	 * Get a client connection ready to use.
	 *
	 * @return A ready to use client connection.
	 */
	public static FBAInventoryServiceMWSClient getClient() {
		return getAsyncClient();
	}

	/**
	 * Get an async client connection ready to use.
	 *
	 * @return A ready to use client connection.
	 */
	public static synchronized FBAInventoryServiceMWSAsyncClient getAsyncClient() {
		if (client == null) {
			FBAInventoryServiceMWSConfig config = new FBAInventoryServiceMWSConfig();
			config.setServiceURL(serviceURL);
			// Set other client connection configurations here.
			client = new FBAInventoryServiceMWSAsyncClient(accessKey, secretKey, appName, appVersion, config, null);
		}
		return client;
	}

}
