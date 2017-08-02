/**
 * 
 */
package amazon.db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.NamingException;

import amazon.db.model.ListOrdersTrackStatus;
import common.db.DB;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 27, 2017 Time: 10:36:30 PM
 */
public class ListOrdersTrackQuerier {

	/**
	 * Query to get the latest completed createdBefore
	 * 
	 * Only the not null value should be used,it means clearly.
	 * 
	 * The null value means there is no matched result or exception happened while
	 * querying.
	 * 
	 * @return the latest completed createdBefore
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws NamingException
	 */
	public static Timestamp queryLatestCompletedCreatedBefore() throws SQLException, NamingException {
		Timestamp latestCompletedCreatedBefore = null;

		String sql = "SELECT created_before FROM list_orders_track WHERE status=? ORDER BY created_before DESC LIMIT 1";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setByte(1, ListOrdersTrackStatus.Completed.value());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				latestCompletedCreatedBefore = rs.getTimestamp(1);
			}
			rs.close();
			ps.close();
			con.close();
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

		return latestCompletedCreatedBefore;
	}

	/**
	 * If the specified table is empty
	 * 
	 * Only the true value should be used, because it means the table is empty.
	 * 
	 * The false value means the table is not empty or exception happened while
	 * querying.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws ClassNotFoundException
	 */
	public static boolean isTableEmpty() throws SQLException, NamingException {
		boolean isEmpty = false;

		String sql = "SELECT id FROM list_orders_track LIMIT 1";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (!rs.next())
				isEmpty = true;

			rs.close();
			ps.close();
			con.close();
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

		return isEmpty;
	}

	/**
	 * If pending record exists
	 * 
	 * Only the true value should be used, because it means a pending record exists.
	 * 
	 * The false value means no pending record exists or exception happened while
	 * querying.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws NamingException
	 */
	public static boolean hasPendingTask() throws SQLException, NamingException {
		boolean hasPendingTask = false;

		String sql = "SELECT id FROM list_orders_track WHERE status=? ORDER BY id ASC LIMIT 1";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setByte(1, ListOrdersTrackStatus.Pending.value());
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				hasPendingTask = true;

			rs.close();
			ps.close();
			con.close();
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

		return hasPendingTask;
	}

	public static int getOldestPendingTaskId() throws SQLException, NamingException {
		int id = 0;

		String sql = "SELECT id FROM list_orders_track WHERE status=? ORDER BY id ASC LIMIT 1";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setByte(1, ListOrdersTrackStatus.Pending.value());
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				id = rs.getInt(1);

			rs.close();
			ps.close();
			con.close();
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

		return id;
	}
}
