/**
 * 
 */
package amazon.mws;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 21, 2017 Time: 11:40:32 AM
 */
public class SellerConfig {
	/** Seller Seller ID. */
	public static final String sellerId;
	/** Seller Marketplace ID. */
	public static final String marketplaceId;

	/**
	 * Read the attributes value from the properties file
	 */
	private static Properties config;
	static {
		config = new Properties();
		try {
			config.load(SellerConfig.class.getResourceAsStream("SellerConfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		sellerId = config.getProperty("sellerId");
		marketplaceId = config.getProperty("marketplaceId");
	}

}
