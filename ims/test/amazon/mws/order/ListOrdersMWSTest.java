package amazon.mws.order;

import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonservices.mws.client.MwsUtl;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import common.util.Time;

public class ListOrdersMWSTest {

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
	public void testListOrders() {
		// offset time: minutes calculated from milliseconds
		int offSet = TimeZone.getTimeZone("PST").getRawOffset() / 60000;

		XMLGregorianCalendar createdAfter = MwsUtl.getDTF().newXMLGregorianCalendar();
		createdAfter.setTimezone(offSet);// minutes
		createdAfter.setYear(2017);
		// createdAfter.setMonth(2);
		// createdAfter.setDay(3);
		// createdAfter.setTime(0, 0, 0);
		createdAfter.setMonth(7);
		createdAfter.setDay(28);
		createdAfter.setTime(18, 19, 0);
		createdAfter.setTimezone(8 * 60);
		XMLGregorianCalendar createdBefore = MwsUtl.getDTF().newXMLGregorianCalendar();
		createdBefore.setTimezone(offSet);// minutes
		createdBefore.setYear(2017);
		createdBefore.setMonth(2);
		createdBefore.setDay(4);
		createdBefore.setTime(0, 0, 0);

		// Make the call.
		ListOrdersResponse response = ListOrdersMWS.listOrders(createdAfter, null);

		// Process the received response from MWS
		System.out.println("");
		System.out.println("Begin to process the received response from MWS ");
		ListOrdersResult listOrdersResult = response.getListOrdersResult();
		boolean hasNextOrders = listOrdersResult.isSetNextToken();
		String nextToken = listOrdersResult.getNextToken();
		List<Order> orders = listOrdersResult.getOrders();
		for (Order order : orders) {
			printOrder(order);
		}
		System.out.println("hasNextOrders= " + hasNextOrders);
		System.out.println("nextToken= " + nextToken);

		while (hasNextOrders && nextToken != null) {
			ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
			ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
			hasNextOrders = nextTokenResult.isSetNextToken();
			nextToken = nextTokenResult.getNextToken();
			List<Order> nextOrders = nextTokenResult.getOrders();
			for (Order order : nextOrders) {
				printOrder(order);
			}
			System.out.println("hasNextOrders= " + hasNextOrders);
			System.out.println("nextToken= " + nextToken);
		}

		//
		System.out.println("");
		System.out.println("Complete to process the received response from MWS ");
	}

	public static void printOrder(Order order) {
		System.out.println("");
		System.out.println("amazonOrderId=" + order.getAmazonOrderId());
		System.out.println("sellerOrderId=" + order.getSellerOrderId());
		System.out.println("purchaseDate=" + order.getPurchaseDate());
		System.out.println("--------------time in PST timezone start--------------");
		System.out.println("purchaseDate in Timestamp=" + Time.getTime(order.getPurchaseDate()));
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		System.out.println("purchaseDate(converted to PST timezone)="
				+ Time.getTime(Time.PST, Time.getTime(order.getPurchaseDate())));
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		System.out.println("--------------time in PST timezone end--------------");
		System.out.println("lastUpdateDate=" + order.getLastUpdateDate());
		System.out.println("salesChannel=" + order.getSalesChannel());// :Amazon.com
		System.out.println("fulfillmentChannel=" + order.getFulfillmentChannel());// :AFN
		System.out.println("isBusinessOrder=" + order.getIsBusinessOrder());
		System.out.println("isPremiumOrder=" + order.getIsPremiumOrder());
		System.out.println("isPrime=" + order.getIsPrime());
		System.out.println("buyerName=" + order.getBuyerName());
		System.out.println("buyerEmail=" + order.getBuyerEmail());
		System.out.println("purchaseOrderNumber=" + order.getPurchaseOrderNumber());// NULL
		System.out.println("orderChannel=" + order.getOrderChannel());// NULL
		System.out.println("orderType=" + order.getOrderType());// :StandardOrder
		System.out.println("orderStatus=" + order.getOrderStatus());// Shipped
		System.out.println("orderTotal.currencyCode="
				+ (order.getOrderTotal() == null ? null : order.getOrderTotal().getCurrencyCode()));
		System.out.println(
				"orderTotal.amount=" + (order.getOrderTotal() == null ? null : order.getOrderTotal().getAmount()));
		// :Expedited FreeEconomy NextDay SameDay SecondDay Scheduled
		// Standard
		System.out.println("shipmentServiceLevelCategory=" + order.getShipmentServiceLevelCategory());
		System.out.println("shipServiceLevel=" + order.getShipServiceLevel());// :SecondDay...
		System.out.println("NumberOfItemsShipped=" + order.getNumberOfItemsShipped());
		System.out.println("NumberOfItemsUnshipped=" + order.getNumberOfItemsUnshipped());
		System.out.println("PaymentMethod=" + order.getPaymentMethod());// :other...
		System.out.println("PaymentExecutionDetail=" + order.getPaymentExecutionDetail());// only available for CN and
																							// JP
		System.out.println("ShippingAddress.toXML="
				+ (order.getShippingAddress() == null ? null : order.getShippingAddress().toXML()));// 11 attributes

		System.out.println("");
	}

}
