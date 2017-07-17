/**
 * 
 */
package internal.supply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.db.DB;

/**
 * @author Eric Li
 *
 */
public class BatchNoGenerator {
	/**
	 * the batch no will start from 1
	 * 
	 * @return
	 * @throws SQLException
	 *             if exception happened while querying database
	 */
	public static synchronized int generateBatchNo() throws SQLException {
		// step 1: get the current max batch no.
		int batchNo = getMaxBatchNo();
		// step 2: increase 1 or throw SQLException
		// exception happened while querying database
		if (batchNo < 0)
			throw new SQLException("Exception happened while querying the current max batch no from database!");
		// if batch no ==0 : there is no existed batch no
		// increase 1 :the current max batch no found
		// increase 1
		batchNo++;

		return batchNo;
	}

	/**
	 * 
	 * @return the current max batch no : while batch no existed; -1 : while
	 *         system error while query database; 0 : while there is no existed
	 *         batch no
	 */
	private static synchronized int getMaxBatchNo() {
		int batchNo = -1;

		String sql = "select batch_no from supply_transaction order by batch_no desc limit 1";
		DB db = new DB();
		Connection con = null;
		try {
			con = db.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				batchNo = rs.getInt(1);// get the current max batch no
			} else {
				batchNo = 0;// get 0 : for the first time, there is no existed
							// batch no
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

		return batchNo;
	}
	
	

}
