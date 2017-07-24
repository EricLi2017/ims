/**
 * 
 */
package amazon.db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import amazon.db.model.Orders;
import common.db.DB;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 10:27:50 PM
 */
public class OrderQuerier {
	public List<Orders> selectOldestPendingOrders(int rows) {

		List<Orders> ordersList = new ArrayList<>();

		String sql = "select (amazon_order_id,seller_order_id,purchase_date,"
				+ "last_update_date,sales_channel,fulfillment_channel," + "is_business_order,is_premium_order,is_prime,"
				+ "buyer_name,buyer_email,order_status,"
				+ "order_total_currency,order_total_amount,ship_service_category,"
				+ "ship_service_level,number_items_shipped,number_items_unshipped) from orders "
				+ "where order by purchase_date asc limit ?";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, rows);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Orders orders = new Orders();
				orders.setAmazonOrderId(rs.getString("amazon_order_id"));
				orders.setSellerOrderId(rs.getString("seller_order_id"));
				orders.setPurchaseDate(rs.getTimestamp("purchase_date"));
				orders.setLastUpdateDate(rs.getTimestamp("last_update_date"));
				orders.setSalesChannel(rs.getString("sales_channel"));
				orders.setFulfillmentChannel(rs.getString("fulfillment_channel"));
				orders.setIsBusinessOrder(rs.getString("is_business_order"));
				orders.setIsPremiumOrder(rs.getString("is_premium_order"));
				orders.setIsPrime(rs.getString("is_prime"));
				orders.setBuyerName(rs.getString("buyer_name"));
				orders.setBuyerEmail(rs.getString("buyer_email"));
				orders.setOrderStatus(rs.getString("order_status"));
				orders.setOrderTotalCurrency(rs.getString("order_total_currency"));
				orders.setOrderTotalAmount(rs.getBigDecimal("order_total_amount"));
				orders.setShipServiceCategory(rs.getString("ship_service_category"));
				orders.setShipServiceLevel(rs.getString("ship_service_level"));
				orders.setNumberItemsShipped(rs.getInt("number_items_shipped"));
				orders.setNumberItemsUnshipped(rs.getInt("number_items_unshipped"));

				ordersList.add(orders);
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

		return ordersList;
	}

}
