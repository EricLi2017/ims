package amazon.mws.order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.MwsUtl;

import com.amazonservices.mws.orders._2013_09_01.model.*;
import common.util.Time;

public class ListOrderAndOrderItemsManager {

	/**
	 * get sum group by SKU
	 */
	public static List<SkuSum> getSumByPruchaseDate(XMLGregorianCalendar createdAfter,
			XMLGregorianCalendar createdBefore) {
		if (createdAfter == null || createdBefore == null)
			return null;

		return new ListOrdersAndOrderItemsDatabase().selectSumByPruchaseDate(Time.getTime(createdAfter),
				Time.getTime(createdBefore));
	}

	/**
	 * query database, call MWS to get amazon order items and insert into database
	 */
	public static int insertOrderItems(XMLGregorianCalendar createdAfter, XMLGregorianCalendar createdBefore) {
		if (createdAfter == null || createdBefore == null)
			return 0;
		int rows = 0;

		// query database to get amazon order id list for those order that has
		// no related order items
		List<String> amazonOrderIds = new ListOrdersAndOrderItemsDatabase()
				.selectOrderIdsWithoutOrderItemsByPruchaseDate(Time.getTime(createdAfter), Time.getTime(createdBefore));
		if (amazonOrderIds == null || amazonOrderIds.size() == 0) {
			return rows;
		}
		System.out.println("there are " + amazonOrderIds.size() + " orders need to insert order items.");

		// loop to call MWS and insert order items to database
		for (String amazonOrderId : amazonOrderIds) {
			rows += ListOrderItemsManager.insertOrderItems(amazonOrderId);
		}
		System.out.println("totally insert " + rows + " order items");
		return rows;
	}

	/**
	 * Call mws for order and order items and insert them into ims database 1. call
	 * MWS to get batch orders 2.1. call MWS to get order items of this order 2.2.
	 * insert order items into database 2.3. insert order into database 2.4. go to
	 * 2.1 for loop until batch orders end 3. call MWS to get other orders 4. go to
	 * 2.1 for loop until batch orders end
	 *
	 * @return List<InsertResult>
	 * @throws Exception
	 */
	public List<InsertResult> insertOrderAndOrderItemsFromMWS(XMLGregorianCalendar createdAfter,
			XMLGregorianCalendar createdBefore) throws Exception {
		if (createdAfter == null || createdBefore == null)
			return null;
		List<InsertResult> insertResults = new ArrayList<>();

		// Make the call to get first batch orders
		ListOrdersResponse response = ListOrdersMWS.listShippedOrders(createdAfter, createdBefore);
		ListOrdersResult listOrdersResult = response.getListOrdersResult();
		boolean hasNextOrders = listOrdersResult.isSetNextToken();
		String nextToken = listOrdersResult.getNextToken();
		insertResults.addAll(insertOrderAndOrderItemsFromMWS(listOrdersResult.getOrders()));
		System.out.println("hasNextOrders= " + hasNextOrders);
		System.out.println("nextToken= " + nextToken);

		// Make the calls to get the others
		while (hasNextOrders && nextToken != null) {
			ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
			ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
			hasNextOrders = nextTokenResult.isSetNextToken();
			nextToken = nextTokenResult.getNextToken();
			insertResults.addAll(insertOrderAndOrderItemsFromMWS(nextTokenResult.getOrders()));
			System.out.println("hasNextOrders= " + hasNextOrders);
			System.out.println("nextToken= " + nextToken);
		}

		return insertResults;
	}

	// 1. call MWS to get order items of this order
	// 2. insert order items into database
	// 3. insert order into database
	private InsertResult insertOrderAndOrderItemsFromMWS(Order order) throws SQLException {
		String amazonOrderId = order.getAmazonOrderId();

		// check if order exist
		int countExist = ListOrdersManager.getCountById(amazonOrderId);
		System.out.println(amazonOrderId + " : " + countExist);// TODO for debug
		if (countExist > 0) {
			return null;
		}

		// 1. call MWS to get order items of this order
		List<OrderItem> orderItems = ListOrderItemsManager.getOrderItems(amazonOrderId);
		// 2. insert order items into database
		int rowItem = ListOrderItemsManager.insertOrderItems(orderItems, amazonOrderId);
		// 3. insert order into database
		int rowOrder = ListOrdersManager.insertOrder(order);

		// return
		InsertResult insertResult = new InsertResult();
		insertResult.setAmazonOrderId(amazonOrderId);
		insertResult.setInsertOrderSuccess(rowOrder);
		insertResult.setInsertOrderItemsSuccess(rowItem);
		System.out.println(insertResult);// todo for debug
		return insertResult;
	}

	/**
	 * call mws and insert these orders and order items into database
	 * 
	 * @throws SQLException
	 * @throws Exception
	 */
	private List<InsertResult> insertOrderAndOrderItemsFromMWS(List<Order> orders) throws SQLException {
		if (orders == null)
			return null;

		System.out.println(orders.size());// TODO
		List<InsertResult> insertResults = new ArrayList<>();
		for (Order order : orders) {
			InsertResult insertResult = insertOrderAndOrderItemsFromMWS(order);
			if (insertResult != null)
				insertResults.add(insertResult);
		}
		return insertResults;
	}

	public class InsertResult {
		private String amazonOrderId = null;
		private int insertOrderSuccess = 0;
		private int insertOrderItemsSuccess = 0;

		public String getAmazonOrderId() {
			return amazonOrderId;
		}

		void setAmazonOrderId(String amazonOrderId) {
			this.amazonOrderId = amazonOrderId;
		}

		public int getInsertOrderSuccess() {
			return insertOrderSuccess;
		}

		void setInsertOrderSuccess(int insertOrderSuccess) {
			this.insertOrderSuccess = insertOrderSuccess;
		}

		public int getInsertOrderItemsSuccess() {
			return insertOrderItemsSuccess;
		}

		void setInsertOrderItemsSuccess(int insertOrderItemsSuccess) {
			this.insertOrderItemsSuccess = insertOrderItemsSuccess;
		}

		@Override
		public String toString() {
			return "InsertResult{" + "amazonOrderId='" + amazonOrderId + '\'' + ", insertOrderSuccess="
					+ insertOrderSuccess + ", insertOrderItemsSuccess=" + insertOrderItemsSuccess + '}';
		}
	}

	/**
	 * count the orders that there are at least one order item related to
	 */
	public static int getCountWithOrderItemsByPurchaseDateFromDB(XMLGregorianCalendar createdAfter,
			XMLGregorianCalendar createdBefore) {
		if (createdAfter == null || createdBefore == null)
			return -1;
		return new ListOrdersAndOrderItemsDatabase().selectCountWithOrderItemsByPruchaseDate(Time.getTime(createdAfter),
				Time.getTime(createdBefore));
	}

	/**
	 * count the orders that there is no any order items related to
	 */
	public static int getCountWithoutOrderItemsByPurchaseDateFromMWS(XMLGregorianCalendar createdAfter,
			XMLGregorianCalendar createdBefore) {
		if (createdAfter == null || createdBefore == null)
			return -1;

		return new ListOrdersAndOrderItemsDatabase()
				.selectCountWithoutOrderItemsByPruchaseDate(Time.getTime(createdAfter), Time.getTime(createdBefore));
	}

	public static void main(String... strings) {
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
		createdBefore.setDay(20);
		createdBefore.setTime(0, 0, 0);

		System.out.println(createdAfter);
		System.out.println(common.util.Time.getTime(createdAfter));

		// int rows = insertOrders(createdAfter, createdBefore);
		// System.out.println("insert " + rows + " rows");
		int rows = getCountWithOrderItemsByPurchaseDateFromDB(createdAfter, createdBefore);
		int noRows = getCountWithoutOrderItemsByPurchaseDateFromMWS(createdAfter, createdBefore);

		List<SkuSum> skuSums = getSumByPruchaseDate(createdAfter, createdBefore);

		System.out.println("there are " + rows + " rows with order items");
		System.out.println("there are " + noRows + " rows without order items");
		System.out.println(skuSums);
		System.out.println("there are " + skuSums.size() + " rows order items");
	}

}
