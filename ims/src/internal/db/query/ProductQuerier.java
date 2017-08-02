package internal.db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import common.db.DB;
import common.util.Filter;
import internal.db.model.InternalProduct;

/**
 * database select operation for table: internal_product
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 13, 2017 Time: 5:04:25 PM
 */
public class ProductQuerier {
	/**
	 * Query data from table internal_product
	 * 
	 * Where condition: the parameter with value null or "" will be ignored
	 * 
	 * @param productId
	 * @param name
	 * @param dateAfter
	 * @param dateBefore
	 * @param status
	 * @param asin
	 * @param orderBy
	 * @param ascOrDesc
	 * @return null: if exception happened while query database; empty size
	 *         List: if there is no matched result;
	 */
	public List<InternalProduct> queryProduct(String productId, String name, Timestamp dateAfter, Timestamp dateBefore,
			String status, String asin, String orderBy, String ascOrDesc) {

		// SQL part1: select & from
		String part1 = "select product_id,name,description,create_time,status,asin from internal_product";

		// SQL part2: where
		String part2 = "";
		if (Filter.nullFilter(productId) != "" || Filter.nullFilter(name) != "" || dateAfter != null
				|| dateBefore != null || Filter.nullFilter(status) != "" || Filter.nullFilter(asin) != "") {
			// where condition exists
			// construct where condition
			List<String> whereCondition = new ArrayList<>();
			whereCondition.add(" where ");// size is 1 now
			if (Filter.nullFilter(productId) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("product_id='" + Integer.parseInt(productId.trim()) + "'");
			}
			if (Filter.nullFilter(name) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("name like '%" + name.trim() + "%'");// %name%
			}
			if (dateAfter != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("create_time > '" + dateAfter + "'");
			}
			if (dateBefore != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("create_time < '" + dateBefore + "'");
			}
			if (Filter.nullFilter(status) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("status='" + status.trim() + "'");
			}
			if (Filter.nullFilter(asin) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("asin='" + asin.trim() + "'");
			}

			// convert List to String
			for (String wc : whereCondition) {
				part2 += wc;
			}
		}

		// SQL part3: order by
		String part3 = "";
		if (orderBy != null && !"".equals(orderBy.trim())) {
			// order by condition exists
			// construct order by condition
			part3 += " order by " + orderBy;
			if (ascOrDesc != null && !"".equals(ascOrDesc)) {
				// use the specified sort order
				part3 += " " + ascOrDesc;
			}
		}

		// get the SQL string
		String sql = part1 + part2 + part3;
		// System.out.println(sql);

		List<InternalProduct> products = new ArrayList<>();
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				InternalProduct product = new InternalProduct();
				product.setProductId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setDescription(rs.getString(3));
				product.setCreateTime(rs.getTimestamp(4));
				product.setStatus(rs.getString(5));
				product.setAsin(rs.getString(6));

				products.add(product);
			}
			rs.close();
			ps.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
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

	public InternalProduct selectById(int productId) {
		InternalProduct product = null;

		String sql = "select product_id,name,description,create_time,status,asin from internal_product where product_id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
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

	// has no use
	// public List<InternalProduct> selectAll() {
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
