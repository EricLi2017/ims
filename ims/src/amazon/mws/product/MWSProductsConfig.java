/**
 * 
 */
package amazon.mws.product;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;

import amazon.mws.MWSConfig;

/**
 * Configuration for MarketplaceWebServiceProducts
 * 
 * Created by Eclipse on Aug 3, 2017 at 11:14:26 AM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class MWSProductsConfig extends MWSConfig {
	/**
	 * The endpoint for region service and version. ex: serviceURL =
	 * MWSEndpoint.NA_PROD.toString();
	 */
	private static final String serviceURL = serviceURL_Products;

	/** The client, lazy initialized. Async client is also a sync client. */
	private static MarketplaceWebServiceProductsAsyncClient client = null;

	/**
	 * Get a client connection ready to use.
	 *
	 * @return A ready to use client connection.
	 */
	public static MarketplaceWebServiceProductsAsyncClient getClient() {
		return getAsyncClient();
	}

	/**
	 * Get an async client connection ready to use.
	 *
	 * @return A ready to use client connection.
	 */
	public static synchronized MarketplaceWebServiceProductsAsyncClient getAsyncClient() {
		if (client == null) {
			MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
			config.setServiceURL(serviceURL);
			// Set other client connection configurations here.
			client = new MarketplaceWebServiceProductsAsyncClient(accessKey, secretKey, appName, appVersion, config,
					null);
		}
		return client;
	}
}
