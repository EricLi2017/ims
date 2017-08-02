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

		System.out.println("amazonOrderId:" + amazonOrderId + ", orderItems.size():" + orderItems.size());

		String sql = "insert into order_items(amazon_order_id,sku,asin,"
				+ "price_currency,price_amount,discount_currency," + "discount_amount,tax_currency,tax_amount,"
				+ "order_item_id,title,quantity_ordered," + "quantity_shipped) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		try {
			con = DB.getConnection();
			con.setAutoCommit(false);// cancel auto commit
			PreparedStatement ps;
			for (OrderItem orderItem : orderItems) {
				if (orderItem == null)
					continue;
				// catch to continue for loop to complete
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, amazonOrderId);
					ps.setString(2, orderItem.getSellerSKU());
					ps.setString(3, orderItem.getASIN());
					ps.setString(4,
							orderItem.getItemPrice() == null ? null : orderItem.getItemPrice().getCurrencyCode());
					ps.setString(5, orderItem.getItemPrice() == null ? null : orderItem.getItemPrice().getAmount());
					ps.setString(6, orderItem.getPromotionDiscount() == null ? null
							: orderItem.getPromotionDiscount().getCurrencyCode());
					ps.setString(7, orderItem.getPromotionDiscount() == null ? null
							: orderItem.getPromotionDiscount().getAmount());
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
			con.commit();// commit
		} catch (Exception e) {// rollback should be called for any exception
			try {
				con.rollback();// rollback
				rows = 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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

		System.out.println("amazonOrderId:" + amazonOrderId + ", table order_items insert(/should insert):" + rows + "/"
				+ orderItems.size() + " rows");

		return rows;
	}

	// public int deleteAndInsert(List<OrderItem> orderItems, String amazonOrderId)
	// {
	// int rows = 0;
	// if (orderItems == null || orderItems.size() == 0)
	// return 0;
	//
	// System.out.println("amazonOrderId:" + amazonOrderId + ", orderItems.size():"
	// + orderItems.size());
	//
	// String delSql = "DELETE FROM order_items WHERE amazon_order_id=?";
	//
	// String sql = "insert into order_items(amazon_order_id,sku,asin,"
	// + "price_currency,price_amount,discount_currency," +
	// "discount_amount,tax_currency,tax_amount,"
	// + "order_item_id,title,quantity_ordered," + "quantity_shipped) values
	// (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// DB db = new DB();
	// Connection con = null;
	// try {
	// con = db.getConnection();
	// con.setAutoCommit(false);// cancel auto commit
	//
	// // delete orderItems by amazonOderId
	// PreparedStatement delPs = con.prepareStatement(delSql);
	// delPs.setString(1, amazonOrderId);
	// delPs.executeUpdate();
	// delPs.close();
	//
	// // insert orderItems
	// PreparedStatement ps;
	// for (OrderItem orderItem : orderItems) {
	// if (orderItem == null)
	// continue;
	// // catch to continue for loop to complete
	// try {
	// ps = con.prepareStatement(sql);
	// ps.setString(1, amazonOrderId);
	// ps.setString(2, orderItem.getSellerSKU());
	// ps.setString(3, orderItem.getASIN());
	// ps.setString(4,
	// orderItem.getItemPrice() == null ? null :
	// orderItem.getItemPrice().getCurrencyCode());
	// ps.setString(5, orderItem.getItemPrice() == null ? null :
	// orderItem.getItemPrice().getAmount());
	// ps.setString(6, orderItem.getPromotionDiscount() == null ? null
	// : orderItem.getPromotionDiscount().getCurrencyCode());
	// ps.setString(7, orderItem.getPromotionDiscount() == null ? null
	// : orderItem.getPromotionDiscount().getAmount());
	// ps.setString(8, orderItem.getItemTax() == null ? null :
	// orderItem.getItemTax().getCurrencyCode());
	// ps.setString(9, orderItem.getItemTax() == null ? null :
	// orderItem.getItemTax().getAmount());
	// ps.setString(10, orderItem.getOrderItemId());
	// ps.setString(11, orderItem.getTitle());
	// ps.setInt(12, orderItem.getQuantityOrdered());
	// ps.setInt(13, orderItem.getQuantityShipped());
	//
	// rows += ps.executeUpdate();
	// ps.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// con.commit();// commit
	// } catch (Exception e) {// rollback should be called for any exception
	// try {
	// con.rollback();// rollback
	// rows = 0;
	// } catch (SQLException e1) {
	// e1.printStackTrace();
	// }
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
	// System.out.println("amazonOrderId:" + amazonOrderId + ", table order_items
	// update(/should update):" + rows + "/"
	// + orderItems.size() + " rows");
	//
	// return rows;
	// }

}