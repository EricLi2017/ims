/**
 * 
 */
package amazon.mws;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 19, 2017 Time: 3:32:22 PM
 */
public class MWSConfig {

	/** Developer AWS access key. */
	public static final String accessKey;

	/** Developer AWS secret key. */
	public static final String secretKey;

	/** The client application name. */
	public static final String appName;

	/** The client application version. */
	public static final String appVersion;

	/**
	 * The endpoint for region service and version. ex: serviceURL =
	 * MWSEndpoint.NA_PROD.toString();
	 */
	// # Fulfillment service URL
	public static final String serviceURL_Fulfillment;
	// # Order service URL
	public static final String serviceURL_Order;

	/**
	 * Read the attributes value from the properties file 
	 */
	private static Properties config;
	static {
		config = new Properties();
		try {
			config.load(MWSConfig.class.getResourceAsStream("MWSConfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// common
		accessKey = config.getProperty("accessKey");
		secretKey = config.getProperty("secretKey");
		appName = config.getProperty("appName");
		appVersion = config.getProperty("appVersion");
		// different service
		serviceURL_Fulfillment = config.getProperty("serviceURL_Fulfillment");
		serviceURL_Order = config.getProperty("serviceURL_Order");
	}

}
