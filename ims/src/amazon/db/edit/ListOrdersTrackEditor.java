/**
 * 
 */
package amazon.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import amazon.db.model.ListOrdersTrackStatus;
import common.db.DB;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 27, 2017 Time: 10:11:43 PM
 */
public class ListOrdersTrackEditor {
	/**
	 * Insert the default first row
	 * 
	 * @param createdBefore
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static int insertDefaultFirst(Timestamp createdBefore, Timestamp startTime, Timestamp endTime)
			throws SQLException, ClassNotFoundException {
		if (createdBefore == null || startTime == null || endTime == null)
			return 0;
		int rows = -1;

		String sql = "INSERT INTO list_orders_track(created_before,status,start_time,end_time) VALUES (?,?,?,?)";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, createdBefore);
			ps.setByte(2, ListOrdersTrackStatus.Completed.value());
			ps.setTimestamp(3, startTime);
			ps.setTimestamp(4, endTime);

			rows = ps.executeUpdate();
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

		return rows;
	}

	/**
	 * Insert a non default first row and get the id of this row
	 * 
	 * @param startTime
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public static int insertNewAndGetId(Timestamp startTime) throws ClassNotFoundException, SQLException {
		if (startTime == null)
			return 0;
		int rows = -1;
		int id = 0;

		String insertSql = "INSERT INTO list_orders_track(status,start_time) VALUES (?,?)";
		String selectSql = "SELECT LAST_INSERT_ID()";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			ps = con.prepareStatement(insertSql);
			ps.setByte(1, ListOrdersTrackStatus.Pending.value());
			ps.setTimestamp(2, startTime);

			rows = ps.executeUpdate();
			ps.close();

			PreparedStatement ps2;
			ps2 = con.prepareStatement(selectSql);
			ResultSet rs = ps2.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);

				System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getName() + ": "
						+ ListOrdersTrackEditor.class.getName() + ": insertNewAndGetId() success, insert rows=" + rows
						+ ", id=" + id);// TODO
			}
			rs.close();
			ps2.close();

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

	/**
	 * Update by PK:id
	 * 
	 * Note: the start_time could not be updated since it is set
	 * 
	 * @param listOrdersTrack
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static int updateToCompleted(Timestamp createdBefore, Timestamp endTime, int id)
			throws ClassNotFoundException, SQLException {
		int rows = -1;

		String sql = "UPDATE list_orders_track SET created_before=?,status=?,end_time=? WHERE id=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setTimestamp(1, createdBefore);
			ps.setByte(2, ListOrdersTrackStatus.Completed.value());
			ps.setTimestamp(3, endTime);
			ps.setInt(4, id);

			rows = ps.executeUpdate();

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
		return rows;
	}

}
