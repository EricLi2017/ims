package internal.db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import common.db.DB;
import common.util.Filter;
import internal.db.model.ProductSupply;

/**
 * database select operation for table: product_supply
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 13, 2017 Time: 5:04:25 PM
 */
public class SupplyQuerier {
	/**
	 * Query data from table product_supply
	 * 
	 * Where condition: the parameter with value null or "" will be ignored
	 * 
	 * @param supplyId
	 * @param productId
	 * @param supplyUrl
	 * @param dateAfter
	 * @param dateBefore
	 * @param status
	 * @param orderBy
	 * @param ascDesc
	 * @return null: if exception happened while query database; empty size List: if
	 *         there is no matched result;
	 */
	public List<ProductSupply> querySupply(String supplyId, String productId, String supplyUrl, Timestamp dateAfter,
			Timestamp dateBefore, String status, String orderBy, String ascDesc) {

		// SQL part1: select & from
		String part1 = "select supply_id,product_id,supplier_id,supplier_name,supplier_description,supply_type,supply_url,shipped_from,unit_price,price_description,price_time,status from product_supply";
		// SQL part2: where
		String part2 = "";
		if (Filter.nullFilter(supplyId) != "" || Filter.nullFilter(productId) != ""
				|| Filter.nullFilter(supplyUrl) != "" || dateAfter != null || dateBefore != null
				|| Filter.nullFilter(status) != "") {
			// where condition exists
			// construct where condition
			List<String> whereCondition = new ArrayList<>();
			whereCondition.add(" where ");// size is 1 now
			if (Filter.nullFilter(supplyId) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("supply_id='" + Integer.parseInt(supplyId.trim()) + "'");
			}
			if (Filter.nullFilter(productId) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("product_id='" + Integer.parseInt(productId.trim()) + "'");
			}
			if (Filter.nullFilter(supplyUrl) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("supply_url like '%" + supplyUrl.trim() + "%'");// %supply_url%
			}
			if (dateAfter != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("price_time > '" + dateAfter + "'");
			}
			if (dateBefore != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("price_time < '" + dateBefore + "'");
			}
			if (Filter.nullFilter(status) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("status='" + status.trim() + "'");
			}

			// convert List to String
			for (String wc : whereCondition) {
				part2 += wc;
			}
		}

		// SQL part3: order by
		String part3 = "";
		if (Filter.nullFilter(orderBy) != "") {
			// order by condition exists
			// construct order by condition
			part3 += " order by " + orderBy.trim();
			if (Filter.nullFilter(ascDesc) != "") {
				// use the specified sort order
				part3 += " " + ascDesc;
			}
		}

		// get the SQL string
		String sql = part1 + part2 + part3;
		// System.out.println(sql);

		List<ProductSupply> supplies = new ArrayList<>();
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ProductSupply supply = new ProductSupply();
				supply.setSupplyId(rs.getInt(1));
				supply.setProductId(rs.getInt(2));
				supply.setSupplierId(rs.getString(3) == null ? null : rs.getInt(3));
				supply.setSupplierName(rs.getString(4));
				supply.setSupplierDescription(rs.getString(5));
				supply.setSupplyType(rs.getString(6));
				supply.setSupplyUrl(rs.getString(7));
				supply.setShippedFrom(rs.getString(8));
				supply.setUnitPrice(rs.getBigDecimal(9));
				supply.setPriceDescription(rs.getString(10));
				supply.setPriceTime(rs.getTimestamp(11));
				supply.setStatus(rs.getString(12));

				supplies.add(supply);
			}
			rs.close();
			ps.close();
			con.close();
		} catch ( SQLException | NamingException e) {
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

		return supplies;
	}

	/**
	 * count supply by product id
	 * 
	 * @return
	 */
	public int countByProductId(int productId) {
		int count = -1;

		String sql = "select count(*) from product_supply where product_id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			ps.close();
			con.close();
		} catch ( SQLException | NamingException e) {
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

		return count;
	}

	/**
	 * select by PK
	 * 
	 * @param supplyId
	 * @return
	 */
	public ProductSupply selectById(int supplyId) {
		ProductSupply supply = null;

		String sql = "select supply_id,product_id,supplier_id,supplier_name,supplier_description,supply_type,supply_url,shipped_from,unit_price,price_description,price_time,status from product_supply where supply_id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, supplyId);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				supply = new ProductSupply();
				supply.setSupplyId(rs.getInt(1));
				supply.setProductId(rs.getInt(2));
				supply.setSupplierId(rs.getString(3) == null ? null : rs.getInt(3));
				supply.setSupplierName(rs.getString(4));
				supply.setSupplierDescription(rs.getString(5));
				supply.setSupplyType(rs.getString(6));
				supply.setSupplyUrl(rs.getString(7));
				supply.setShippedFrom(rs.getString(8));
				supply.setUnitPrice(rs.getBigDecimal(9));
				supply.setPriceDescription(rs.getString(10));
				supply.setPriceTime(rs.getTimestamp(11));
				supply.setStatus(rs.getString(12));

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

		return supply;
	}

	// has no use
	// public List<ProductSupply> queryAll() {
	// List<ProductSupply> supplies = new ArrayList<>();
	//
	// String sql = "select
	// supply_id,product_id,supplier_id,supplier_name,supplier_description,supply_type,supply_url,shipped_from,unit_price,price_description,price_time,status
	// from product_supply order by supply_id desc";
	// DB db = new DB();
	// Connection con = null;
	// try {
	// con = db.getConnection();
	// PreparedStatement ps = con.prepareStatement(sql);
	//
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// ProductSupply supply = new ProductSupply();
	// supply.setSupplyId(rs.getInt(1));
	// supply.setProductId(rs.getInt(2));
	// supply.setSupplierId(rs.getString(3) == null ? null : rs.getInt(3));
	// supply.setSupplierName(rs.getString(4));
	// supply.setSupplierDescription(rs.getString(5));
	// supply.setSupplyType(rs.getString(6));
	// supply.setSupplyUrl(rs.getString(7));
	// supply.setShippedFrom(rs.getString(8));
	// supply.setUnitPrice(rs.getBigDecimal(9));
	// supply.setPriceDescription(rs.getString(10));
	// supply.setPriceTime(rs.getTimestamp(11));
	// supply.setStatus(rs.getString(12));
	//
	// supplies.add(supply);
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
	// return supplies;
	// }

}
