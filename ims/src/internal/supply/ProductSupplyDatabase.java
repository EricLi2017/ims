package internal.supply;

import database.model.ProductSupply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import common.db.DB;

/**
 * database base operation for table: product_supply Created by Eric Li on
 * 3/20/2017.
 */
public class ProductSupplyDatabase {
	public int insert(List<ProductSupply> supplies) {
		if (supplies == null || supplies.size() == 0)
			return 0;

		int rows = 0;
		String sql = "insert into product_supply (product_id,supplier_id,supplier_name,supplier_description,supply_type,supply_url,shipped_from,unit_price,price_description,price_time,status) values (?,?,?,?,?,?,?,?,?,?,?)";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps;
			for (ProductSupply supply : supplies) {
				ps = con.prepareStatement(sql);
				ps.setInt(1, supply.getProductId());
				ps.setString(2, supply.getSupplierId() == null ? null : supply.getSupplierId().toString());
				ps.setString(3, supply.getSupplierName());
				ps.setString(4, supply.getSupplierDescription());
				ps.setString(5, supply.getSupplyType());
				ps.setString(6, supply.getSupplyUrl());
				ps.setString(7, supply.getShippedFrom());
				ps.setBigDecimal(8, supply.getUnitPrice());
				ps.setString(9, supply.getPriceDescription());
				ps.setTimestamp(10,
						supply.getPriceTime() == null ? null : new Timestamp(supply.getPriceTime().getTime()));
				ps.setString(11, supply.getStatus());

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

	public int updateById(ProductSupply supply) {
		int rows = -1;

		String sql = "update product_supply set product_id=?,supplier_id=?,supplier_name=?,supplier_description=?,supply_type=?,supply_url=?,shipped_from=?,unit_price=?,price_description=?,price_time=?,status=? where supply_id=?";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps = con.prepareStatement(sql);
			ps.setInt(1, supply.getProductId());
			ps.setString(2, supply.getSupplierId() == null ? null : supply.getSupplierId().toString());
			ps.setString(3, supply.getSupplierName());
			ps.setString(4, supply.getSupplierDescription());
			ps.setString(5, supply.getSupplyType());
			ps.setString(6, supply.getSupplyUrl());
			ps.setString(7, supply.getShippedFrom());
			ps.setBigDecimal(8, supply.getUnitPrice());
			ps.setString(9, supply.getPriceDescription());
			ps.setTimestamp(10, supply.getPriceTime() == null ? null : new Timestamp(supply.getPriceTime().getTime()));
			ps.setString(11, supply.getStatus());

			ps.setInt(12, supply.getSupplyId());
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

	/**
	 * select by PK
	 * 
	 * @param supplyId
	 * @return
	 */
	public ProductSupply selectById(int supplyId) {
		ProductSupply supply = null;

		String sql = "select supply_id,product_id,supplier_id,supplier_name,supplier_description,supply_type,supply_url,shipped_from,unit_price,price_description,price_time,status from product_supply where supply_id=?";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
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

		return supply;
	}

}
