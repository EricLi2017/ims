/**
 * 
 */
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
import internal.db.model.SupplyTransaction;

/**
 * database select operation for table: supply_transaction
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 13, 2017 Time: 5:04:25 PM
 */
public class TransactionQuerier {

	/**
	 * Query data from table supply_transaction and product_supply
	 * 
	 * Where condition: the parameter with value null or "" will be ignored
	 * 
	 * @param supplyId
	 * @param batchNo
	 * @param dateAfter
	 * @param dateBefore
	 * @param status
	 * @param productId
	 * @param orderBy
	 * @param ascDesc
	 * @return null: if exception happened while query database; empty size List: if
	 *         there is no matched result;
	 */
	public List<TransactionAndSupply> queryTransactionAndSupply(String supplyId, String batchNo, Timestamp dateAfter,
			Timestamp dateBefore, String status, String productId, String orderBy, String ascDesc) {

		// SQL part1: select & from
		String part1 = "select a.supply_id,a.quantity,a.unit_price,a.price_description,a.status,a.batch_no,a.product_price,a.shipped_fee,a.time,a.operator,a.transaction_description,b.product_id from supply_transaction a left join product_supply b on a.supply_id=b.supply_id";

		// SQL part2: where
		String part2 = "";
		if (Filter.nullFilter(supplyId) != "" || Filter.nullFilter(batchNo) != "" || dateAfter != null
				|| dateBefore != null || Filter.nullFilter(status) != "" || Filter.nullFilter(productId) != "") {
			// where condition exists
			// construct where condition
			List<String> whereCondition = new ArrayList<>();
			whereCondition.add(" where ");// size is 1 now
			if (Filter.nullFilter(supplyId) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("a.supply_id='" + Integer.parseInt(supplyId.trim()) + "'");
			}
			if (Filter.nullFilter(batchNo) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("a.batch_no='" + Integer.parseInt(batchNo.trim()) + "'");
			}
			if (dateAfter != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("a.time > '" + dateAfter + "'");
			}
			if (dateBefore != null) {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("a.time < '" + dateBefore + "'");
			}
			if (Filter.nullFilter(status) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("a.status='" + status.trim() + "'");
			}
			if (Filter.nullFilter(productId) != "") {
				whereCondition.add(whereCondition.size() == 1 ? "" : " and ");
				whereCondition.add("b.product_id='" + Integer.parseInt(productId.trim()) + "'");
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

		List<TransactionAndSupply> transactionAndSupplies = new ArrayList<>();
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				TransactionAndSupply transactionAndSupply = new TransactionAndSupply();
				SupplyTransaction transaction = new SupplyTransaction();
				ProductSupply supply = new ProductSupply();
				// set transaction
				transaction.setSupplyId(rs.getInt(1));
				transaction.setQuantity(rs.getInt(2));
				transaction.setUnitPrice(rs.getBigDecimal(3));
				transaction.setPriceDescription(rs.getString(4));
				transaction.setStatus(rs.getString(5));
				transaction.setBatchNo(rs.getInt(6));
				transaction.setProductPrice(rs.getBigDecimal(7));
				transaction.setShippedFee(rs.getBigDecimal(8));
				transaction.setTime(rs.getTimestamp(9));
				transaction.setOperator(rs.getString(10));
				transaction.setTransactionDescription(rs.getString(11));
				// set supply
				supply.setProductId(rs.getString(12) == null ? null : rs.getInt(12));// null
				// please continue set other attributes when needed

				transactionAndSupply.setTransaction(transaction);
				transactionAndSupply.setSupply(supply);

				transactionAndSupplies.add(transactionAndSupply);
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

		return transactionAndSupplies;
	}

	/**
	 * count transaction by product id
	 * 
	 * @return
	 */
	public int countByProductId(int productId) {
		int count = -1;

		String sql = "select count(*) from supply_transaction as a,product_supply as b where a.supply_id=b.supply_id and b.product_id=?";
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

		return count;
	}

	public class TransactionAndSupply {
		private SupplyTransaction transaction;
		private ProductSupply supply;

		public SupplyTransaction getTransaction() {
			return transaction;
		}

		public void setTransaction(SupplyTransaction transaction) {
			this.transaction = transaction;
		}

		public ProductSupply getSupply() {
			return supply;
		}

		public void setSupply(ProductSupply supply) {
			this.supply = supply;
		}

		@Override
		public String toString() {
			return "TransactionAndSupply [transaction=" + transaction + ", supply=" + supply + "]";
		}

	}

	// has no use
	// public List<SupplyTransaction> queryAll() {
	// List<SupplyTransaction> transactions = new ArrayList<>();
	//
	// String sql = "select
	// supply_id,quantity,unit_price,price_description,status,batch_no,product_price,shipped_fee,time,operator,transaction_description
	// from supply_transaction order by batch_no desc";
	// DB db = new DB();
	// Connection con = null;
	// try {
	// con = db.getConnection();
	// PreparedStatement ps = con.prepareStatement(sql);
	//
	// ResultSet rs = ps.executeQuery();
	// while (rs.next()) {
	// SupplyTransaction transaction = new SupplyTransaction();
	// transaction.setSupplyId(rs.getInt(1));
	// transaction.setQuantity(rs.getInt(2));
	// transaction.setUnitPrice(rs.getBigDecimal(3));
	// transaction.setPriceDescription(rs.getString(4));
	// transaction.setStatus(rs.getString(5));
	// transaction.setBatchNo(rs.getInt(6));
	// transaction.setProductPrice(rs.getBigDecimal(7));
	// transaction.setShippedFee(rs.getBigDecimal(8));
	// transaction.setTime(rs.getTimestamp(9));
	// transaction.setOperator(rs.getString(10));
	// transaction.setTransactionDescription(rs.getString(11));
	//
	// transactions.add(transaction);
	// }
	// rs.close();
	// ps.close();
	// con.close();
	// } catch ( SQLException e) {
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
	// return transactions;
	// }
}
