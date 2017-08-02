package amazon.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;

import common.db.DB;

public class AmazonProductEditor {
	/**
	 * update :fnsku=?,total_supply_quantity=?,in_stock_supply_quantity=? by sku
	 */
	public static final int update(List<InventorySupply> supplys) {
		int rows = 0;
		if (supplys == null || supplys.size() == 0)
			return 0;

		String sql = "update amazon_product set fnsku=?,total_supply_quantity=?,in_stock_supply_quantity=? where sku=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			// System.out.println("sku condition fusku TotalSupplyQuantity
			// InStockSupplyQuantity");//TODO for debug
			for (InventorySupply supply : supplys) {
				// TODO for debug
				// System.out.print(supply.getSellerSKU());
				// System.out.print(" ");
				// System.out.print(supply.getCondition());
				// System.out.print(" ");
				// System.out.print(supply.getFNSKU());
				// System.out.print(" ");
				// System.out.print(supply.getTotalSupplyQuantity());
				// System.out.print(" ");
				// System.out.print(supply.getInStockSupplyQuantity());
				// System.out.println("");
				// Not in FBA inventory: the supply condition and fnsku will return null (and
				// the supply inventory will return 0)
				if (supply.getCondition() == null || "NewItem".equalsIgnoreCase(supply.getCondition())) {
					ps = con.prepareStatement(sql);
					ps.setString(1, supply.getFNSKU());
					ps.setString(2,
							(supply.getTotalSupplyQuantity() == null
									|| (supply.getCondition() == null && supply.getTotalSupplyQuantity() == 0)) ? null
											: supply.getTotalSupplyQuantity().toString());
					ps.setString(3,
							(supply.getInStockSupplyQuantity() == null
									|| (supply.getCondition() == null && supply.getInStockSupplyQuantity() == 0)) ? null
											: supply.getInStockSupplyQuantity().toString());
					ps.setString(4, supply.getSellerSKU());

					rows += ps.executeUpdate();
					ps.close();
				}
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
}
