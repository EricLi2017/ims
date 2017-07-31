/**
 * 
 */
package amazon.mws.order;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResponse;
import com.amazonservices.mws.orders._2013_09_01.model.GetOrderResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 30, 2017 Time: 9:47:45 PM
 */
public class GetOrderMWSTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link amazon.mws.order.GetOrderMWS#getOrder(java.util.List)}.
	 */
	@Test
	public void testGetOrder() {
		List<String> amazonOrderIdList = new ArrayList<>();
		amazonOrderIdList.add("113-7774973-1713855");
		amazonOrderIdList.add("112-7357018-8110664");
		amazonOrderIdList.add("113-6350917-2337061");

		GetOrderResponse response = GetOrderMWS.getOrder(amazonOrderIdList);
		GetOrderResult result = response.getGetOrderResult();
		List<Order> orders = result.getOrders();
		for (Order order : orders) {
			ListOrdersMWSTest.printOrder(order);
		}
	}

}
