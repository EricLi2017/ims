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
 * Servlet implementation class ScheduleServlet
 */
@WebServlet(urlPatterns = { "/ScheduleServlet" }, loadOnStartup = 1)
public class ScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final long INITIAL_DELAY_LIST_ORDERS = 1;
	public static final long INITIAL_DELAY_LIST_ORDER_ITEMS = 2;
	public static final long DELAY_LIST_ORDERS = 60;
	public static final long DELAY_LIST_ORDER_ITEMS = 60;
	public static final TimeUnit TIME_UNIT_LIST_ORDERS = TimeUnit.MINUTES;
	public static final TimeUnit TIME_UNIT_LIST_ORDER_ITEMS = TimeUnit.MINUTES;
	

	private static final ScheduledExecutorService scheduledExecutorService;
	static {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);

		/**
		 * Use scheduleWithFixedDelay to comply with the MWS API Throttling
		 */
		// Insert orders into IMS from MWS Orders API
		scheduledExecutorService.scheduleWithFixedDelay(new ListOrdersTimerTask(), INITIAL_DELAY_LIST_ORDERS,
				DELAY_LIST_ORDERS, TIME_UNIT_LIST_ORDERS);

		// Insert order items into IMS from MWS Orders API
		scheduledExecutorService.scheduleWithFixedDelay(new ListOrderItemsTimerTask(), INITIAL_DELAY_LIST_ORDER_ITEMS,
				DELAY_LIST_ORDER_ITEMS, TIME_UNIT_LIST_ORDER_ITEMS);

	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScheduleServlet() {
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
