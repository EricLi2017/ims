package amazon.db.query;

import common.db.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

/**
 * Check sku in database
 * <p>
 * Created by Eric Li on 2/27/2017.
 */
public class SKUMatch {
	/**
	 * get distinct sku count from amazon_products
	 *
	 * @return
	 */
	public int getSkuCountFromProducts() {
		return getDistinctCountFromProducts("sku");
	}

	/**
	 * get distinct asin count from amazon_products
	 *
	 * @return
	 */
	public int getAsinCountFromProducts() {
		return getDistinctCountFromProducts("asin");
	}

	/**
	 * get distinct colName count from amazon_products
	 *
	 * @param colName
	 * @return
	 */
	private int getDistinctCountFromProducts(String colName) {
		if (colName == null || "".equals(colName.trim())) {
			return -1;
		}

		int rows = -1;
		String sql = "select count(DISTINCT " + colName + " ) from amazon_product";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

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

	/**
	 * get distinct sku count from order_items
	 *
	 * @return
	 */
	public int getSkuCountFromOrders() {
		return getDistinctCountFromOrders("sku");
	}

	/**
	 * get distinct asin count from order_items
	 *
	 * @return
	 */
	public int getAsinCountFromOrders() {
		return getDistinctCountFromOrders("asin");
	}

	/**
	 * get distinct colName count from order_items
	 *
	 * @param colName
	 * @return
	 */
	private int getDistinctCountFromOrders(String colName) {
		if (colName == null || "".equals(colName.trim())) {
			return -1;
		}

		int rows = -1;
		String sql = "select count(DISTINCT " + colName + " ) from order_items";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

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

	/**
	 * get sku count where sku exists in order_items but not exists in
	 * amazon_product
	 *
	 * @return
	 */
	public int getSkuCountNotInProducts() {
		int rows = -1;
		String sql = "select count(DISTINCT a.sku ) from order_items as a where a.sku not in (select b.sku from amazon_product as b )";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

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

	/**
	 * get sku list where sku exists in order_items but not exists in amazon_product
	 *
	 * @return
	 */
	public List<String> getSkuNotInProducts() {
		List<String> skus = new ArrayList<>();

		String sql = "select DISTINCT a.sku from order_items as a where a.sku not in (select b.sku from amazon_product as b )";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				skus.add(rs.getString(1));
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
		return skus;
	}

	public static void main(String... args) {
		// System.out.println("There are " + new SKUMatch().getSkuCountFromProducts() +
		// " sku in product records.");
		// System.out.println("There are " + new SKUMatch().getAsinCountFromProducts() +
		// " asin in product records.");
		// System.out.println("There are " + new SKUMatch().getSkuCountFromOrders() + "
		// sku in order records.");
		// System.out.println("There are " + new SKUMatch().getAsinCountFromOrders() + "
		// asin in order records.");
		// System.out.println("There are " + new SKUMatch().getSkuCountNotInProducts() +
		// " sku that exists in order records but not exists in product records.");
		// System.out.println(new SKUMatch().getSkuNotInProducts() + "are the sku that
		// exists in order records but not exists in product records.");
	}

}
