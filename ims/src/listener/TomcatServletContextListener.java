package listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import servlet.ScheduleServlet;

/**
 * Application Lifecycle Listener implementation class
 * TomcatServletContextListener
 *
 */
@WebListener
public final class TomcatServletContextListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(TomcatServletContextListener.class);

	/**
	 * Default constructor.
	 */
	public TomcatServletContextListener() {
		log.info("Tomcat Listener be constructed");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Web application initialization process is starting");
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Servlet context is about to be shut down");

		// Shut down executer
		log.info("Shut down executer");
		ScheduleServlet.shutDownExecuter();

		// Deregister JDBC Driver
		deregisterDriver();

		// AbandonedConnectionCleanupThread@since 5.1.41
		log.info("AbandonedConnectionCleanupThread.checkedShutdown()");
		AbandonedConnectionCleanupThread.checkedShutdown();
	}

	private void deregisterDriver() {
		// ... First close any background tasks which may be using the DB ...
		// ... Then close any DB connection pools ...

		// Now deregister JDBC drivers in this context's ClassLoader:
		// Get the webapp's ClassLoader
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		// Loop through all drivers
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() == cl) {
				// This driver was registered by the webapp's ClassLoader, so deregister it:
				try {
					log.info("Deregistering JDBC driver " + driver);
					DriverManager.deregisterDriver(driver);
				} catch (SQLException ex) {
					log.error("Error deregistering JDBC driver " + driver, ex);
				}
			} else {
				// driver was not registered by the webapp's ClassLoader and may be in use
				// elsewhere
				log.info("Not deregistering JDBC driver " + driver
						+ " as it does not belong to this webapp's ClassLoader");
			}

		}
	}
}