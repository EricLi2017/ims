package amazon.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import amazon.db.model.Product;
import common.db.DB;

public class InventoryReportDatabase {

	/**
	 * insert: sku,asin,your_price
	 *
	 * @param products
	 * @return
	 */
	public int insert(List<Product> products) {
		int rows = 0;
		if (products == null || products.size() == 0)
			return 0;

		String sql = "insert into amazon_product (sku,asin,your_price) values (?,?,?)";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			for (Product product : products) {
				ps = con.prepareStatement(sql);
				ps.setString(1, product.getSku());
				ps.setString(2, product.getAsin());
				ps.setDouble(3, product.getPrice());

				rows += ps.executeUpdate();
				ps.close();
			}
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

	/**
	 * select all: sku,asin,your_price
	 *
	 * @return null: if exception happen while query database List<Product>: matched
	 *         result found in database
	 */
	public List<Product> selectAll() {
		List<Product> products = new ArrayList<>();

		String sql = "select sku,asin,your_price from amazon_product order by sku asc";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setSku(rs.getString(1));
				product.setAsin(rs.getString(2));
				product.setPrice(rs.getDouble(3));

				products.add(product);
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
			return null;
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

		return products;

	}

}
