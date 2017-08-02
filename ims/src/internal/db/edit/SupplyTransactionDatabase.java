package internal.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import common.db.DB;
import internal.db.model.SupplyTransaction;

public class SupplyTransactionDatabase {

	public SupplyTransaction selectByPK(int supplyId, int batchNo) {
		SupplyTransaction transaction = null;

		String sql = "select supply_id,quantity,unit_price,price_description,status,batch_no,product_price,shipped_fee,time,operator,transaction_description from supply_transaction where supply_id=? and batch_no=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, supplyId);
			ps.setInt(2, batchNo);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				transaction = new SupplyTransaction();
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

		return transaction;
	}

	public int updateByPK(SupplyTransaction transaction) {
		int rows = -1;

		String sql = "update supply_transaction set supply_id=?,quantity=?,unit_price=?,price_description=?,status=?,batch_no=?,product_price=?,shipped_fee=?,time=?,operator=?,transaction_description=? where supply_id=? and batch_no=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, transaction.getSupplyId());// PK
			ps.setInt(2, transaction.getQuantity());
			ps.setBigDecimal(3, transaction.getUnitPrice());
			ps.setString(4, transaction.getPriceDescription());
			ps.setString(5, transaction.getStatus());
			ps.setInt(6, transaction.getBatchNo());// PK
			ps.setBigDecimal(7, transaction.getProductPrice());
			ps.setBigDecimal(8, transaction.getShippedFee());
			ps.setTimestamp(9, new Timestamp(transaction.getTime().getTime()));
			ps.setString(10, transaction.getOperator());
			ps.setString(11, transaction.getTransactionDescription());
			ps.setInt(12, transaction.getSupplyId());// PK set where condition
			ps.setInt(13, transaction.getBatchNo());// PK set where condition

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
	 * batch insert using Database Transaction mode
	 * 
	 * Note about DT: if auto commit is canceled, must call commit or rollback to
	 * avoid table be locked
	 * 
	 * Note: Mysql only InnoDB supports Database Transaction
	 * 
	 * @param transactions
	 * @return
	 */
	public int insertDT(List<SupplyTransaction> transactions) {
		if (transactions == null || transactions.size() == 0)
			return 0;

		int rows = 0;
		String sql = "insert into supply_transaction (supply_id,quantity,unit_price,price_description,status,batch_no,product_price,shipped_fee,time,operator,transaction_description) values (?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		try {
			con = DB.getConnection();
			con.setAutoCommit(false);// cancel auto commit
			PreparedStatement ps;
			for (SupplyTransaction transaction : transactions) {
				ps = con.prepareStatement(sql);
				ps.setInt(1, transaction.getSupplyId());
				ps.setInt(2, transaction.getQuantity());
				ps.setBigDecimal(3, transaction.getUnitPrice());
				ps.setString(4, transaction.getPriceDescription());
				ps.setString(5, transaction.getStatus());
				ps.setInt(6, transaction.getBatchNo());
				ps.setBigDecimal(7, transaction.getProductPrice());
				ps.setBigDecimal(8, transaction.getShippedFee());
				ps.setTimestamp(9, new Timestamp(transaction.getTime().getTime()));
				ps.setString(10, transaction.getOperator());
				ps.setString(11, transaction.getTransactionDescription());

				rows += ps.executeUpdate();
				ps.close();
			}
			con.commit();// commit
			// con.close();
		} catch (Exception e) {// rollback should be called for any exception
			try {
				con.rollback();// rollback
				rows = 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
