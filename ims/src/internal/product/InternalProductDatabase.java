package internal.product;

import database.model.InternalProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import common.db.DB;

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
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
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

		return rows;
	}

	public int deleteById(int productId) {
		// todo
		return -1;
	}

	public int updateById(InternalProduct product) {
		int rows = -1;

		String sql = "update internal_product set name=?,description=?,status=?,asin=? where product_id=?";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getStatus());
			ps.setString(4, product.getAsin());
			ps.setInt(5, product.getProductId());

			rows = ps.executeUpdate();

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
		return rows;
	}

	public InternalProduct selectById(int productId) {
		InternalProduct product = null;

		String sql = "select product_id,name,description,create_time,status,asin from internal_product where product_id=?";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				product = new InternalProduct();
				product.setProductId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setDescription(rs.getString(3));
				product.setCreateTime(rs.getTimestamp(4));
				product.setStatus(rs.getString(5));
				product.setAsin(rs.getString(6));
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

		return product;
	}

	// public List<InternalProduct> selectAll() {
	// // todo
	// List<InternalProduct> products = new ArrayList<>();
	//
	// String sql = "select product_id,name,description,create_time,status,asin
	// from internal_product order by product_id desc";
	// DB db = new DB();
	// Connection con = null;
	// try {
	// con = db.getConnection();
	// PreparedStatement ps = con.prepareStatement(sql);
	//
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// InternalProduct product = new InternalProduct();
	// product.setProductId(rs.getInt(1));
	// product.setName(rs.getString(2));
	// product.setDescription(rs.getString(3));
	// product.setCreateTime(rs.getTimestamp(4));
	// product.setStatus(rs.getString(5));
	// product.setAsin(rs.getString(6));
	//
	// products.add(product);
	// }
	// rs.close();
	// ps.close();
	// con.close();
	// } catch (ClassNotFoundException | SQLException e) {
	// e.printStackTrace();
	// return null;
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
	// return products;
	// }
}
