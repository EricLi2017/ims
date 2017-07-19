package amazon.mws.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import common.db.DB;

public class ListOrdersAndOrderItemsDatabase {

    /**
     * get sum group by SKU //TODO
     *
     * @param createdAfter
     * @param createdBefore
     * @return
     */
    public List<SkuSum> selectSumByPruchaseDate(Timestamp createdAfter, Timestamp createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return null;
        List<SkuSum> skuSums = new ArrayList<SkuSum>();

        //select b.sku as sku,sum(if(a.is_business_order='false',b.price_amount-b.discount_amount,null)  ) as sales, sum(if(a.is_business_order='true',b.price_amount-b.discount_amount,null)  ) as sales_b2b,sum(if(a.is_business_order='false',b.quantity_ordered,null)) as ordered, sum(if(a.is_business_order='true',b.quantity_ordered,null)) as ordered_b2b from orders as a, order_items as b where a.amazon_order_id=b.amazon_order_id
        String sql = "select b.sku as sku, sum(b.price_amount) as price, sum(b.discount_amount) as discount, sum(b.quantity_ordered) as ordered, sum(b.quantity_shipped) as shipped from orders as a, order_items as b where a.amazon_order_id=b.amazon_order_id and a.purchase_date>=? and a.purchase_date<? group by b.sku";
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, createdAfter);
            ps.setTimestamp(2, createdBefore);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SkuSum skuSum = new SkuSum();
                skuSum.setSku(rs.getString("sku"));
                skuSum.setPriceAmount(rs.getBigDecimal("price"));
                skuSum.setDiscountAmount(rs.getBigDecimal("discount"));
                skuSum.setQuantityOrdered(rs.getInt("ordered"));
                skuSum.setQuantityShipped(rs.getInt("shipped"));

                skuSums.add(skuSum);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

        return skuSums;
    }

    /**
     * count the orders that there are at least one order item related to
     *
     * @param createdAfter
     * @param createdBefore
     * @return
     */
    public int selectCountWithOrderItemsByPruchaseDate(Timestamp createdAfter, Timestamp createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return -1;

//        String sql = "select count(a.amazon_order_id) from orders as a, order_items as b where a.amazon_order_id=b.amazon_order_id and a.purchase_date>=? and a.purchase_date<?";
        String sql = "select count(a.amazon_order_id) from orders as a where a.amazon_order_id in (select b.amazon_order_id from order_items as b ) and a.purchase_date>=? and a.purchase_date<?";

        int rows = selectCountOrderItemsByPurchaseDate(sql, createdAfter, createdBefore);
        return rows;

    }

    /**
     * count the orders that there is no any order items related to
     *
     * @param createdAfter
     * @param createdBefore
     * @return
     */
    public int selectCountWithoutOrderItemsByPruchaseDate(Timestamp createdAfter, Timestamp createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return -1;

        String sql = "select count(a.amazon_order_id) from orders as a where a.amazon_order_id not in (select b.amazon_order_id from order_items as b ) and a.purchase_date>=? and a.purchase_date<?";
        int rows = selectCountOrderItemsByPurchaseDate(sql, createdAfter, createdBefore);

        return rows;
    }

    private int selectCountOrderItemsByPurchaseDate(String sql, Timestamp createdAfter, Timestamp createdBefore) {
        int rows = -1;

        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, createdAfter);
            ps.setTimestamp(2, createdBefore);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rows = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

    /**
     * select the amazon order id list for those orders which has no order items
     * related
     *
     * @param createdAfter
     * @param createdBefore
     * @return
     */
    public List<String> selectOrderIdsWithoutOrderItemsByPruchaseDate(Timestamp createdAfter, Timestamp createdBefore) {
        if (createdAfter == null || createdBefore == null)
            return null;
        List<String> orderIds = new ArrayList<String>();

        String sql = "select a.amazon_order_id as id from orders as a where a.amazon_order_id not in (select b.amazon_order_id from order_items as b ) and a.purchase_date>=? and a.purchase_date<?";
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, createdAfter);
            ps.setTimestamp(2, createdBefore);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);

                orderIds.add(id);
            }
            rs.close();
            ps.close();
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
        return orderIds;
    }
}
