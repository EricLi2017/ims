package internal.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import common.db.DB;
import internal.db.model.ProductSupply;

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

}
