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

import javax.naming.NamingException;

import amazon.mws.order.OrderStatus;
import common.db.DB;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 24, 2017 Time: 10:27:50 PM
 */
public class OrderQuerier {
	/**
	 * Acquire the oldest (by purchase_date) non-pending (status!=pending)
	 * amazonOrderId of orders without order items
	 * 
	 * @param rows
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static List<String> selectOldestNonPendingOrdersWithoutItems(int rows) throws SQLException, NamingException {
		List<String> amazonOrderIds = new ArrayList<>();

		String sql = "select a.amazon_order_id from orders as a "
				+ "where a.order_status!=? and a.amazon_order_id not in (select amazon_order_id from order_items) order by a.purchase_date asc limit ?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, OrderStatus.Pending.name());
			ps.setInt(2, rows);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				amazonOrderIds.add(rs.getString(1));
			}
			rs.close();
			ps.close();
			con.close();
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

		System.out.println(OrderQuerier.class.getName() + ": selectOldestNonPendingOrdersWithoutItems().size()="
				+ amazonOrderIds.size());// TODO
		return amazonOrderIds;
	}

	/**
	 * Acquire the oldest (by purchase_date) pending (status==pending) amazonOrderId
	 * of orders
	 * 
	 * @param rows
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static List<String> selectOldestPendingOrders(int rows) throws SQLException, NamingException {
		List<String> amazonOrderIds = new ArrayList<>();

		String sql = "select amazon_order_id from orders where order_status=? order by purchase_date asc limit ?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, OrderStatus.Pending.name());
			ps.setInt(2, rows);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				amazonOrderIds.add(rs.getString(1));
			}
			rs.close();
			ps.close();
			con.close();
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
		System.out.println(
				OrderQuerier.class.getName() + ": selectOldestPendingOrders().size()=" + amazonOrderIds.size());// TODO
		return amazonOrderIds;
	}

	// public List<Orders> selectByPruchaseDate(Timestamp createdAfter, Timestamp
	// createdBefore) {
	// if (createdAfter == null || createdBefore == null)
	// return null;
	// List<Orders> ordersList = new ArrayList<>();
	//
	// String sql = "select amazon_order_id,seller_order_id,purchase_date,"
	// + "last_update_date,sales_channel,fulfillment_channel," +
	// "is_business_order,is_premium_order,is_prime,"
	// + "buyer_name,buyer_email,order_status,"
	// + "order_total_currency,order_total_amount,ship_service_category,"
	// + "ship_service_level,number_items_shipped,number_items_unshipped from orders
	// "
	// + "where purchase_date >=? and purchase_date<? order by purchase_date asc";
	// DB db = new DB();
	// Connection con = null;
	// try {
	// con = db.getConnection();
	// PreparedStatement ps = con.prepareStatement(sql);
	// ps.setTimestamp(1, createdAfter);
	// ps.setTimestamp(2, createdBefore);
	//
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// Orders orders = new Orders();
	// orders.setAmazonOrderId(rs.getString("amazon_order_id"));
	// orders.setSellerOrderId(rs.getString("seller_order_id"));
	// orders.setPurchaseDate(rs.getTimestamp("purchase_date"));
	// orders.setLastUpdateDate(rs.getTimestamp("last_update_date"));
	// orders.setSalesChannel(rs.getString("sales_channel"));
	// orders.setFulfillmentChannel(rs.getString("fulfillment_channel"));
	// orders.setIsBusinessOrder(rs.getString("is_business_order"));
	// orders.setIsPremiumOrder(rs.getString("is_premium_order"));
	// orders.setIsPrime(rs.getString("is_prime"));
	// orders.setBuyerName(rs.getString("buyer_name"));
	// orders.setBuyerEmail(rs.getString("buyer_email"));
	// orders.setOrderStatus(rs.getString("order_status"));
	// orders.setOrderTotalCurrency(rs.getString("order_total_currency"));
	// orders.setOrderTotalAmount(rs.getBigDecimal("order_total_amount"));
	// orders.setShipServiceCategory(rs.getString("ship_service_category"));
	// orders.setShipServiceLevel(rs.getString("ship_service_level"));
	// orders.setNumberItemsShipped(rs.getInt("number_items_shipped"));
	// orders.setNumberItemsUnshipped(rs.getInt("number_items_unshipped"));
	//
	// ordersList.add(orders);
	// }
	// rs.close();
	// ps.close();
	// con.close();
	// } catch (ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// } finally {
	// boolean flag = true;
	// try {
	// if (con == null || con.isClosed()) {
	// flag = false;
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// if (flag) {
	// try {
	// con.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return ordersList;
	// }

}
