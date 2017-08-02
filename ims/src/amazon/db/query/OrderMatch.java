package amazon.db.query;

import common.db.DB;
import common.util.Time;

import javax.xml.datatype.XMLGregorianCalendar;

import amazon.mws.order.OrderStatus;

import java.sql.*;

/**
 * Check if orders and order items are matched in database of IMS
 * Created by Eric Li on 3/3/2017.
 */
public class OrderMatch {

    /**
     * check ims sum: count() in orders,sum(sku units) in orders,sum(sku units) in order_items
     *
     * @return OrderMatchResult
     */
    public OrderMatchResult check(XMLGregorianCalendar createdAfter,
                                  XMLGregorianCalendar createdBefore) {

        return check(Time.getTime(createdAfter), Time.getTime(createdBefore));
    }

    /**
     * check ims sum: count() in orders,sum(sku units) in orders,sum(sku units) in order_items
     *
     * @return OrderMatchResult
     */
    private OrderMatchResult check(Timestamp createdAfter, Timestamp createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return null;


        OrderMatchResult orderMatchResult = new OrderMatchResult();
        String sql1 = "SELECT COUNT(a.amazon_order_id), SUM(a.number_items_shipped+a.number_items_unshipped) FROM orders a WHERE a.purchase_date >=? AND a.purchase_date<? AND a.order_status=?";
        String sql2 = "SELECT COUNT(DISTINCT b.amazon_order_id),SUM(b.quantity_ordered) FROM orders a, order_items b WHERE a.amazon_order_id=b.amazon_order_id AND a.purchase_date >=? AND a.purchase_date<? AND a.order_status=?";
        Connection con = null;
        try {
			con = DB.getConnection();
            //query orders
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setTimestamp(1, createdAfter);
            ps1.setTimestamp(2, createdBefore);
            ps1.setString(3, OrderStatus.Shipped.value());
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                orderMatchResult.setOrderNum(rs1.getInt(1));
                orderMatchResult.setItemNum(rs1.getInt(2));
            }
            rs1.close();
            ps1.close();

            //query order_items and orders
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setTimestamp(1, createdAfter);
            ps2.setTimestamp(2, createdBefore);
            ps2.setString(3, OrderStatus.Shipped.value());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                orderMatchResult.setOrderNum2(rs2.getInt(1));
                orderMatchResult.setItemNum2(rs2.getInt(2));
            }
            rs2.close();
            ps2.close();

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
        return orderMatchResult;

    }

    public class OrderMatchResult {
        /**
         * count number of orders
         */
        private int orderNum;

        /**
         * sum items of orders
         */
        private int itemNum;
        /**
         * count distinct amazon_order_id of orders
         */
        private int orderNum2;
        /**
         * sum items of order_items
         */
        private int itemNum2;

        public int getOrderNum() {
            return orderNum;
        }

        void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public int getItemNum() {
            return itemNum;
        }

        void setItemNum(int itemNum) {
            this.itemNum = itemNum;
        }

        public int getOrderNum2() {
            return orderNum2;
        }

        void setOrderNum2(int orderNum2) {
            this.orderNum2 = orderNum2;
        }

        public int getItemNum2() {
            return itemNum2;
        }

        void setItemNum2(int itemNum2) {
            this.itemNum2 = itemNum2;
        }


        @Override
        public String toString() {
            return "OrderMatchResult{" +
                    "orderNum=" + orderNum +
                    ", itemNum=" + itemNum +
                    ", orderNum2=" + orderNum2 +
                    ", itemNum2=" + itemNum2 +
                    '}';
        }
    }


}
