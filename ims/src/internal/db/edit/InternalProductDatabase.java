package internal.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import common.db.DB;
import internal.db.model.InternalProduct;

/**
 * database base operation for table: internal_product Created by Eric Li on
 * 3/20/2017.
 */
public class InternalProductDatabase {
	public int insert(List<InternalProduct> products) {
		if (products == null || products.size() == 0)
			return 0;

		int rows = 0;
		String sql = "insert into internal_product (name,description,status,asin) values (?,?,?,?)";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			for (InternalProduct product : products) {
				ps = con.prepareStatement(sql);
				ps.setString(1, product.getName());
				ps.setString(2, product.getDescription());
				ps.setString(3, product.getStatus());
				ps.setString(4, product.getAsin());

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

	public int deleteById(int productId) {
		// todo
		return -1;
	}

	public int updateById(InternalProduct product) {
		int rows = -1;

		String sql = "update internal_product set name=?,description=?,status=?,asin=? where product_id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getStatus());
			ps.setString(4, product.getAsin());
			ps.setInt(5, product.getProductId());

			rows = ps.executeUpdate();

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
