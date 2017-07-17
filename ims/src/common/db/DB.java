package common.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
	private static Properties config = null;

	public Connection getConnection() throws ClassNotFoundException, SQLException {

		if (config == null) {
			config = new Properties();
			try {
				config.load(getClass().getResourceAsStream("DB.properties"));
			} catch (IOException e) {
				e.printStackTrace();
				// avoid to produce new type exception
				throw new ClassNotFoundException("Could no find database connection config properties file!");
			}
		}

		Class.forName(config.getProperty("DRIVER"));
		Connection conn = DriverManager.getConnection(config.getProperty("URL"), config.getProperty("USER"),
				config.getProperty("PASSWORD"));
		return conn;
	}

}