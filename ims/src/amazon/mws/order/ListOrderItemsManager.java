package amazon.mws.order;

import java.util.ArrayList;
import java.util.List;

import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsByNextTokenResult;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResponse;
import com.amazonservices.mws.orders._2013_09_01.model.ListOrderItemsResult;
import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

public class ListOrderItemsManager {

    /**
     * Call MWS to get order items of the amazon order and insert them into
     * database
     *
     * @param amazonOrderId
     * @return
     */
    public static int insertOrderItems(String amazonOrderId) {
        if (amazonOrderId == null || "".equals(amazonOrderId.trim()))
            return 0;
        int rows = 0;

        // Make the call.
        ListOrderItemsResponse response = ListOrderItemsMWS.listOrderItems(amazonOrderId);

        // insert into database
        ListOrderItemsResult listOrderItemsResult = response.getListOrderItemsResult();
        boolean hasNextOrderItems = listOrderItemsResult.isSetNextToken();
        String nextToken = listOrderItemsResult.getNextToken();
        List<OrderItem> orderItems = listOrderItemsResult.getOrderItems();
        rows += insertOrderItems(orderItems, amazonOrderId);
        System.out.println("hasNextOrderItems= " + hasNextOrderItems);
        System.out.println("nextToken= " + nextToken);

        // insert the others into database
        while (hasNextOrderItems && nextToken != null) {
            ListOrderItemsByNextTokenResponse nextTokenResponse = ListOrderItemsMWS
                    .listOrderItemsByNextToken(nextToken);
            ListOrderItemsByNextTokenResult nextTokenResult = nextTokenResponse.getListOrderItemsByNextTokenResult();
            hasNextOrderItems = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            List<OrderItem> nextOrderItems = nextTokenResult.getOrderItems();
            rows += insertOrderItems(nextOrderItems, amazonOrderId);
            System.out.println("hasNextOrderItems= " + hasNextOrderItems);
            System.out.println("nextToken= " + nextToken);
        }
        System.out.println("totally " + rows + " rows order items inserted for the amazonOrderId " + amazonOrderId);
        return rows;
    }

    /**
     * Call MWS to get order items of the amazon order
     *
     * @param amazonOrderId
     * @return
     */
    public static List<OrderItem> getOrderItems(String amazonOrderId) {
        if (amazonOrderId == null || "".equals(amazonOrderId.trim()))
            return null;
        List<OrderItem> orderItems = new ArrayList<>();

        // Make the call to get first batch order items of this order
        ListOrderItemsResponse response = ListOrderItemsMWS.listOrderItems(amazonOrderId);
        ListOrderItemsResult listOrderItemsResult = response.getListOrderItemsResult();
        boolean hasNextOrderItems = listOrderItemsResult.isSetNextToken();
        String nextToken = listOrderItemsResult.getNextToken();
        orderItems.addAll(listOrderItemsResult.getOrderItems());//add
        System.out.println("hasNextOrderItems= " + hasNextOrderItems);
        System.out.println("nextToken= " + nextToken);

        // Make the call to get other order items of this order
        while (hasNextOrderItems && nextToken != null) {
            ListOrderItemsByNextTokenResponse nextTokenResponse = ListOrderItemsMWS
                    .listOrderItemsByNextToken(nextToken);
            ListOrderItemsByNextTokenResult nextTokenResult = nextTokenResponse.getListOrderItemsByNextTokenResult();
            hasNextOrderItems = nextTokenResult.isSetNextToken();
            nextToken = nextTokenResult.getNextToken();
            orderItems.addAll(listOrderItemsResult.getOrderItems());//add
            System.out.println("hasNextOrderItems= " + hasNextOrderItems);
            System.out.println("nextToken= " + nextToken);
        }

        return orderItems;
    }

    public static int insertOrderItems(List<OrderItem> orderItems, String amazonOrderId) {
        if (orderItems == null || orderItems.size() == 0)
            return 0;

        int rows = 0;
        rows += new ListOrderItemsDatabase().insert(orderItems, amazonOrderId);
        System.out.println(rows + " rows order items inserted for amazonOrderId " + amazonOrderId);
        return rows;
    }

    public static void main(String... strings) {
        String amazonOrderId = "107-5499830-9104262";
        ListOrderItemsManager.insertOrderItems(amazonOrderId);

    }

}
