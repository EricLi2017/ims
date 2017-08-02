package common.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DB {
	private static Properties config;
	private static DataSource dataSource;

	private DB() {
	}

	public static Connection getConnection() throws NamingException, SQLException {
		if (dataSource == null) {
			if (config == null) {
				config = new Properties();
				try {
					config.load(DB.class.getResourceAsStream("DB.properties"));
				} catch (IOException e) {
					e.printStackTrace();
					// avoid to produce new type exception
					throw new NamingException("Could no find database connection config properties file!");
				}
			}
			dataSource = (DataSource) new InitialContext().lookup(config.getProperty("JNDI_NAME_IMS"));
		}

		Connection conn = dataSource.getConnection();

		return conn;
	}

}