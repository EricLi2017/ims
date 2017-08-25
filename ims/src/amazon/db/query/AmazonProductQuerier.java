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

import common.db.DB;

/**
 * Created by Eclipse on Aug 1, 2017 at 4:26:55 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class AmazonProductQuerier {

	/**
	 * Acquire all the SKU from amazon_product table
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static final List<String> selectAllSku() throws SQLException, NamingException {
		List<String> skus = new ArrayList<>();

		String sql = "SELECT sku FROM amazon_product";
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

		System.out.println(OrderQuerier.class.getName() + ": selectAllSku().size()=" + skus.size());
		return skus;
	}

	public static final List<String> selectAllDistinctAsin() throws SQLException, NamingException {
		List<String> asins = new ArrayList<>();

		String sql = "SELECT DISTINCT asin FROM amazon_product";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				asins.add(rs.getString(1));
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

		System.out.println(OrderQuerier.class.getName() + ": selectAllDistinctAsin().size()=" + asins.size());
		return asins;
	}

	public static final Integer selectLevelBySku(String sku) throws SQLException, NamingException {
		Integer level = null;

		String sql = "SELECT level FROM amazon_product WHERE sku=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, sku);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				level = (rs.getString(1) == null ? null : rs.getInt(1));
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

		return level;
	}

}
