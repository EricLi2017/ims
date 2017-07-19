package amazon.mws.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.amazonservices.mws.orders._2013_09_01.model.OrderItem;

import common.db.DB;

public class ListOrderItemsDatabase {

    public int insert(List<OrderItem> orderItems, String amazonOrderId) {
        int rows = 0;
        if (orderItems == null || orderItems.size() == 0)
            return 0;

        String sql = "insert into order_items(amazon_order_id,sku,asin,"
                + "price_currency,price_amount,discount_currency," + "discount_amount,tax_currency,tax_amount,"
                + "order_item_id,title,quantity_ordered," + "quantity_shipped) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            PreparedStatement ps;
            for (OrderItem orderItem : orderItems) {
                if (orderItem == null) continue;
                // catch to continue for loop to complete
                try {
                    ps = con.prepareStatement(sql);
                    ps.setString(1, amazonOrderId);
                    ps.setString(2, orderItem.getSellerSKU());
                    ps.setString(3, orderItem.getASIN());
                    ps.setString(4, orderItem.getItemPrice() == null ? null : orderItem.getItemPrice().getCurrencyCode());
                    ps.setString(5, orderItem.getItemPrice() == null ? null : orderItem.getItemPrice().getAmount());
                    ps.setString(6, orderItem.getPromotionDiscount() == null ? null : orderItem.getPromotionDiscount().getCurrencyCode());
                    ps.setString(7, orderItem.getPromotionDiscount() == null ? null : orderItem.getPromotionDiscount().getAmount());
                    ps.setString(8, orderItem.getItemTax() == null ? null : orderItem.getItemTax().getCurrencyCode());
                    ps.setString(9, orderItem.getItemTax() == null ? null : orderItem.getItemTax().getAmount());
                    ps.setString(10, orderItem.getOrderItemId());
                    ps.setString(11, orderItem.getTitle());
                    ps.setInt(12, orderItem.getQuantityOrdered());
                    ps.setInt(13, orderItem.getQuantityShipped());

                    rows += ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            boolean flag = true;
            try {
                if (con == null || con.isClosed()) {
                    flag = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (flag) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return rows;
    }

}