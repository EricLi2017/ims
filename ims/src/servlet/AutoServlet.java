package servlet;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import amazon.mws.order.ListOrderItemsTimerTask;
import amazon.mws.order.ListOrdersTimerTask;

/**
 * Servlet implementation class AutoServlet
 */
@WebServlet(urlPatterns = { "/AutoServlet" }, loadOnStartup = 1)
public class AutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final ScheduledExecutorService scheduledExecutorService;
	static {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);

		/**
		 * Use scheduleWithFixedDelay to comply with the MWS API Throttling
		 */
		// Insert orders into IMS from MWS Orders API
		// scheduledExecutorService.scheduleWithFixedDelay(new ListOrdersTimerTask(), 1,
		// 60, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new ListOrdersTimerTask(), 5, 2, TimeUnit.SECONDS);// TODO TEST
		// Insert order items into IMS from MWS Orders API
		// scheduledExecutorService.scheduleWithFixedDelay(new
		// ListOrderItemsTimerTask(), 2, 60, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new ListOrderItemsTimerTask(), 10, 5, TimeUnit.SECONDS);// TODO
																												// TEST
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutoServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
