package amazon.mws.order;

import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amazonservices.mws.client.MwsUtl;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrdersResult;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import common.util.Time;

public class ListOrdersManager {

    /**
     * Call MWS to get amazon orders in Shipped status and insert orders to database
     */
    public static int insertShippedOrders(XMLGregorianCalendar createdAfter, XMLGregorianCalendar createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return 0;
        int rows = 0;

        // Make the call.
        ListOrdersResponse response = ListOrdersMWS.listShippedOrders(createdAfter, createdBefore);

        // insert into database
        ListOrdersResult listOrdersResult = response.getListOrdersResult();
        boolean hasNextOrders = listOrdersResult.isSetNextToken();
        String nextToken = listOrdersResult.getNextToken();
        List<Order> orders = listOrdersResult.getOrders();
        rows += insertDatabase(orders);
        System.out.println("hasNextOrders= " + hasNextOrders);
        System.out.println("nextToken= " + nextToken);

        // insert the others into database
        while (hasNextOrders && nextToken != null) {
            ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
            ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
            hasNextOrders = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            List<Order> nextOrders = nextTokenResult.getOrders();
            rows += insertDatabase(nextOrders);
            System.out.println("hasNextOrders= " + hasNextOrders);
            System.out.println("nextToken= " + nextToken);
        }

        System.out.println("totally " + rows + " rows orders inserted");
        return rows;
    }

    private static int insertDatabase(List<Order> orders) {
        if (orders == null || orders.size() == 0)
            return 0;

        int rows = 0;
        rows += new ListOrdersDatabase().insert(orders);
        System.out.println(rows + " rows orders inserted");
        return rows;
    }

    public static int insertOrder(Order order) {
        return new ListOrdersDatabase().insert(order);
    }

    public static int getCountById(String amazonOrderId) {
        return new ListOrdersDatabase().selectCountById(amazonOrderId);
    }

    public static int getCountFromDB(XMLGregorianCalendar createdAfter,
                                     XMLGregorianCalendar createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return -1;

        return new ListOrdersDatabase().selectCountByPruchaseDate(Time.getTime(createdAfter),
                Time.getTime(createdBefore));
    }


    public static int getCountFromMWS(XMLGregorianCalendar createdAfter,
                                      XMLGregorianCalendar createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return -1;
        int rows = 0;
        // Make the call.
        ListOrdersResponse response = ListOrdersMWS.listShippedOrders(createdAfter, createdBefore);

        // count
        ListOrdersResult listOrdersResult = response.getListOrdersResult();
        boolean hasNextOrders = listOrdersResult.isSetNextToken();
        String nextToken = listOrdersResult.getNextToken();
        rows += listOrdersResult.getOrders().size();
        System.out.println("hasNextOrders= " + hasNextOrders);
        System.out.println("nextToken= " + nextToken);

        // count the others
        while (hasNextOrders && nextToken != null) {
            ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
            ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
            hasNextOrders = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            rows += nextTokenResult.getOrders().size();
            System.out.println("hasNextOrders= " + hasNextOrders);
            System.out.println("nextToken= " + nextToken);
        }

        return rows;
    }


    /**
     * Call MWS to count order and sum items in order
     */
    public OrderSumMWS getSumFromMWS(XMLGregorianCalendar createdAfter,
                                     XMLGregorianCalendar createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return null;
        OrderSumMWS orderSumMWS = new OrderSumMWS();
        int orderNum = 0;
        int itemNum = 0;

        // Make the call to get first batch orders
        ListOrdersResponse response = ListOrdersMWS.listShippedOrders(createdAfter, createdBefore);
        ListOrdersResult listOrdersResult = response.getListOrdersResult();
        boolean hasNextOrders = listOrdersResult.isSetNextToken();
        String nextToken = listOrdersResult.getNextToken();
        if (listOrdersResult.getOrders() != null) {
            for (Order order : listOrdersResult.getOrders()) {
                orderNum++;
                itemNum += (order.getNumberOfItemsShipped() + order.getNumberOfItemsUnshipped());
            }
        }
        System.out.println("hasNextOrders= " + hasNextOrders);
        System.out.println("nextToken= " + nextToken);

        // Make the call to get the other orders
        while (hasNextOrders && nextToken != null) {
            ListOrdersByNextTokenResponse nextTokenResponse = ListOrdersMWS.listOrdersByNextToken(nextToken);
            ListOrdersByNextTokenResult nextTokenResult = nextTokenResponse.getListOrdersByNextTokenResult();
            hasNextOrders = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            if (nextTokenResult.getOrders() != null) {
                for (Order order : nextTokenResult.getOrders()) {
                    orderNum++;
                    itemNum += (order.getNumberOfItemsShipped() + order.getNumberOfItemsUnshipped());
                }
            }
            System.out.println("hasNextOrders= " + hasNextOrders);
            System.out.println("nextToken= " + nextToken);
        }

        //return
        orderSumMWS.setOrderNum(orderNum);
        orderSumMWS.setItemNum(itemNum);
        return orderSumMWS;
    }

    public class OrderSumMWS {
        /**
         * count number of orders
         */
        private int orderNum;
        /**
         * sum items of orders
         */
        private int itemNum;

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public int getItemNum() {
            return itemNum;
        }

        public void setItemNum(int itemNum) {
            this.itemNum = itemNum;
        }

        @Override
        public String toString() {
            return "OrderSumMWS{" +
                    "orderNum=" + orderNum +
                    ", itemNum=" + itemNum +
                    '}';
        }
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

        // int rows = insertShippedOrders(createdAfter, createdBefore);
        // System.out.println("insert " + rows + " rows");
//        int rows = new ListOrdersAndOrderItemsDatabase()
//                .selectCountWithOrderItemsByPruchaseDate(Time.getTimeInPST(createdAfter), Time.getTimeInPST(createdBefore));
//        int noRows = new ListOrdersAndOrderItemsDatabase()
//                .selectCountWithoutOrderItemsByPruchaseDate(Time.getTimeInPST(createdAfter), Time.getTimeInPST(createdBefore));
//
//        List<String> amazonOrderIds = new ListOrdersAndOrderItemsDatabase()
//                .selectOrderIdsWithoutOrderItemsByPruchaseDate(Time.getTimeInPST(createdAfter), Time.getTimeInPST(createdBefore));
//
//        System.out.println("there are " + rows + " rows with order items");
//        System.out.println("there are " + noRows + " rows without order items");
//        System.out.println("there are " + amazonOrderIds.size() + " rows without order items");
    }

}
