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
	 * @throws ClassNotFoundException
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

}