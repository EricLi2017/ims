package amazon.mws.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.amazonservices.mws.orders._2013_09_01.model.Address;
import com.amazonservices.mws.orders._2013_09_01.model.Order;

import common.db.DB;
import common.util.Time;

public class ListOrdersDatabase {

	public int insert(List<Order> orders) throws SQLException {
		int rows = 0;// insert into table orders numbers
		int rows2 = 0;// insert into table order_shipping_address numbers
		if (orders == null || orders.size() == 0)
			return 0;

		String sql = "insert IGNORE into orders(amazon_order_id,seller_order_id,purchase_date,"
				+ "last_update_date,sales_channel,fulfillment_channel," + "is_business_order,is_premium_order,is_prime,"
				+ "buyer_name,buyer_email,order_status,"
				+ "order_total_currency,order_total_amount,ship_service_category,"
				+ "ship_service_level,number_items_shipped,number_items_unshipped) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql2 = "insert IGNORE into order_shipping_address(amazon_order_id,name,address_line1,"
				+ "address_line2,address_line3,city," + "country,district,state_or_regin,"
				+ "postal_code,country_code,phone) values(?,?,?,?,?,?,?,?,?,?,?,?)";

		System.out.println(sql2);// TODO
		System.out.println(sql);// TODO
		System.out.println("orders.size()=" + orders.size());// TODO
		Connection con = null;
		try {
			con = DB.getConnection();
			con.setAutoCommit(false);// cancel auto commit
			PreparedStatement ps;
			PreparedStatement ps2;
			for (Order order : orders) {
				System.out.print(order.getAmazonOrderId());// TODO
				System.out.print("\t");
				System.out.print(order.getOrderStatus());
				System.out.print("\t");
				System.out.println(order.getPurchaseDate());
				// insert into table order_shipping_address
				Address address = order.getShippingAddress();
				// canceled and pending order has no address
				if (address != null) {
					ps2 = con.prepareStatement(sql2);
					ps2.setString(1, order.getAmazonOrderId());
					ps2.setString(2, address.getName());
					ps2.setString(3, address.getAddressLine1());
					ps2.setString(4, address.getAddressLine2());
					ps2.setString(5, address.getAddressLine3());
					ps2.setString(6, address.getCity());
					ps2.setString(7, address.getCounty());
					ps2.setString(8, address.getDistrict());
					ps2.setString(9, address.getStateOrRegion());
					ps2.setString(10, address.getPostalCode());
					ps2.setString(11, address.getCountryCode());
					ps2.setString(12, address.getPhone());
					rows2 += ps2.executeUpdate();
					ps2.close();
				}

				// insert into table orders
				ps = con.prepareStatement(sql);
				ps.setString(1, order.getAmazonOrderId());
				ps.setString(2, order.getSellerOrderId());
				ps.setTimestamp(3, Time.getTime(order.getPurchaseDate()));
				ps.setTimestamp(4, Time.getTime(order.getLastUpdateDate()));
				ps.setString(5, order.getSalesChannel());
				ps.setString(6, order.getFulfillmentChannel());
				ps.setString(7, order.getIsBusinessOrder() == null ? null : order.getIsBusinessOrder().toString());
				ps.setString(8, order.getIsPremiumOrder() == null ? null : order.getIsPremiumOrder().toString());
				ps.setString(9, order.getIsPrime() == null ? null : order.getIsPrime().toString());
				ps.setString(10, order.getBuyerName());
				ps.setString(11, order.getBuyerEmail());
				ps.setString(12, order.getOrderStatus());
				ps.setString(13, order.getOrderTotal() == null ? null : order.getOrderTotal().getCurrencyCode());
				ps.setString(14, order.getOrderTotal() == null ? null : order.getOrderTotal().getAmount());
				ps.setString(15, order.getShipmentServiceLevelCategory());
				ps.setString(16, order.getShipServiceLevel());
				ps.setInt(17, order.getNumberOfItemsShipped());
				ps.setInt(18, order.getNumberOfItemsUnshipped());
				rows += ps.executeUpdate();
				ps.close();
			}
			con.commit();// commit
		} catch (Exception e) {// rollback should be called for any exception
			try {
				con.rollback();// rollback
				rows = 0;
				rows2 = 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new SQLException(e);
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
		System.out.println("table orders insert " + rows + " rows, order_shipping_address insert " + rows2 + " rows");

		return rows;
	}

	public int insert(Order order) throws SQLException {
		List<Order> orders = new ArrayList<>();
		orders.add(order);
		return insert(orders);
	}

	public static int deleteAndInsert(List<Order> orders) throws SQLException {
		int delRows = 0;// delete table orders numbers
		int delRows2 = 0;// delete table order_shipping_address numbers

		int rows = 0;// insert into table orders numbers
		int rows2 = 0;// insert into table order_shipping_address numbers
		if (orders == null || orders.size() == 0)
			return 0;

		String delSql = "DELETE FROM orders WHERE amazon_order_id=?";
		String delSql2 = "DELETE FROM order_shipping_address WHERE amazon_order_id=?";

		String sql = "insert IGNORE into orders(amazon_order_id,seller_order_id,purchase_date,"
				+ "last_update_date,sales_channel,fulfillment_channel," + "is_business_order,is_premium_order,is_prime,"
				+ "buyer_name,buyer_email,order_status,"
				+ "order_total_currency,order_total_amount,ship_service_category,"
				+ "ship_service_level,number_items_shipped,number_items_unshipped) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql2 = "insert IGNORE into order_shipping_address(amazon_order_id,name,address_line1,"
				+ "address_line2,address_line3,city," + "country,district,state_or_regin,"
				+ "postal_code,country_code,phone) values(?,?,?,?,?,?,?,?,?,?,?,?)";

		System.out.println(sql2);// TODO
		System.out.println(sql);// TODO
		System.out.println("orders.size()=" + orders.size());// TODO
		Connection con = null;
		try {
			con = DB.getConnection();
			con.setAutoCommit(false);// cancel auto commit
			PreparedStatement delPs;
			PreparedStatement delPs2;
			PreparedStatement ps;
			PreparedStatement ps2;
			for (Order order : orders) {
				System.out.print(order.getAmazonOrderId());// TODO
				System.out.print("\t");
				System.out.print(order.getOrderStatus());
				System.out.print("\t");
				System.out.println(order.getPurchaseDate());
				// delete from table order_shipping_address
				delPs2 = con.prepareStatement(delSql2);
				delPs2.setString(1, order.getAmazonOrderId());
				delRows2 += delPs2.executeUpdate();
				delPs2.close();

				// delete from table orders
				delPs = con.prepareStatement(delSql);
				delPs.setString(1, order.getAmazonOrderId());
				delRows += delPs.executeUpdate();
				delPs.close();

				// insert into table order_shipping_address
				Address address = order.getShippingAddress();
				// canceled and pending order has no address
				if (address != null) {
					ps2 = con.prepareStatement(sql2);
					ps2.setString(1, order.getAmazonOrderId());
					ps2.setString(2, address.getName());
					ps2.setString(3, address.getAddressLine1());
					ps2.setString(4, address.getAddressLine2());
					ps2.setString(5, address.getAddressLine3());
					ps2.setString(6, address.getCity());
					ps2.setString(7, address.getCounty());
					ps2.setString(8, address.getDistrict());
					ps2.setString(9, address.getStateOrRegion());
					ps2.setString(10, address.getPostalCode());
					ps2.setString(11, address.getCountryCode());
					ps2.setString(12, address.getPhone());
					rows2 += ps2.executeUpdate();
					ps2.close();
				}

				// insert into table orders
				ps = con.prepareStatement(sql);
				ps.setString(1, order.getAmazonOrderId());
				ps.setString(2, order.getSellerOrderId());
				ps.setTimestamp(3, Time.getTime(order.getPurchaseDate()));
				ps.setTimestamp(4, Time.getTime(order.getLastUpdateDate()));
				ps.setString(5, order.getSalesChannel());
				ps.setString(6, order.getFulfillmentChannel());
				ps.setString(7, order.getIsBusinessOrder() == null ? null : order.getIsBusinessOrder().toString());
				ps.setString(8, order.getIsPremiumOrder() == null ? null : order.getIsPremiumOrder().toString());
				ps.setString(9, order.getIsPrime() == null ? null : order.getIsPrime().toString());
				ps.setString(10, order.getBuyerName());
				ps.setString(11, order.getBuyerEmail());
				ps.setString(12, order.getOrderStatus());
				ps.setString(13, order.getOrderTotal() == null ? null : order.getOrderTotal().getCurrencyCode());
				ps.setString(14, order.getOrderTotal() == null ? null : order.getOrderTotal().getAmount());
				ps.setString(15, order.getShipmentServiceLevelCategory());
				ps.setString(16, order.getShipServiceLevel());
				ps.setInt(17, order.getNumberOfItemsShipped());
				ps.setInt(18, order.getNumberOfItemsUnshipped());
				rows += ps.executeUpdate();
				ps.close();
			}
			con.commit();// commit
		} catch (Exception e) {// rollback should be called for any exception
			try {
				con.rollback();// rollback
				rows = 0;
				rows2 = 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new SQLException(e);
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
				"table orders delete " + delRows + " rows, order_shipping_address delete " + delRows2 + " rows");
		System.out.println("table orders insert " + rows + " rows, order_shipping_address insert " + rows2 + " rows");

		return rows;
	}

	public int selectCountByPruchaseDate(Timestamp createdAfter, Timestamp createdBefore) {
		if (createdAfter == null || createdBefore == null)
			return -1;
		int rows = -1;

		String sql = "select count(amazon_order_id) from orders where purchase_date >=? and purchase_date<?";
		Connection con = null;
		try {
			con = DB.getConnection();
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
		} catch (SQLException | NamingException e) {
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

	public int selectCountById(String amazonOrderId) {
		if (amazonOrderId == null || "".equals(amazonOrderId.trim()))
			return -1;

		int rows = -1;

		String sql = "select count(amazon_order_id) from orders where amazon_order_id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, amazonOrderId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rows = rs.getInt(1);
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException | NamingException e) {
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
