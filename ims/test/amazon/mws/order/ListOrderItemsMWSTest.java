package amazon.mws.order;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

public class ListOrderItemsMWSTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		// String amazonOrderId = "107-5499830-9104262";// 7 units, 1 sku
		String amazonOrderId = "002-1524942-4267460";// a canceled order

		// Make the call.
		ListOrderItemsResponse response = ListOrderItemsMWS.listOrderItems(amazonOrderId);

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
			ListOrderItemsByNextTokenResponse nextTokenResponse = ListOrderItemsMWS
					.listOrderItemsByNextToken(nextToken);
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
