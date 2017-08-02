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

import amazon.mws.fulfillment.ListInventorySupplyTimerTask;
import amazon.mws.order.GetOrderTimerTask;
import amazon.mws.order.ListOrderItemsTimerTask;
import amazon.mws.order.ListOrdersTimerTask;

/**
 * Servlet implementation class ScheduleServlet
 */
@WebServlet(urlPatterns = { "/ScheduleServlet" }, loadOnStartup = 1)
public class ScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * initialDealy
	 */
	public static final long INITIAL_DELAY_LIST_ORDERS = 60;
	public static final long INITIAL_DELAY_LIST_ORDER_ITEMS = 80;
	public static final long INITIAL_DELAY_GET_ORDER = 100;
	public static final long INITIAL_DELAY_LIST_INVENTORY_SUPPLY = 120;
	/**
	 * delay
	 */
	public static final long DELAY_LIST_ORDERS = 60 * 60;// at least 6 minutes
	public static final long DELAY_LIST_ORDER_ITEMS = 60 * 60;// at least 1 minute
	public static final long DELAY_GET_ORDER = 60 * 60;// at least 6 minutes
	public static final long DELAY_LIST_INVENTORY_SUPPLY = 60 * 60;// at least 15 seconds
	/**
	 * unit
	 */
	public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * <pre>
	 * ********************************************************************    
	 * Use scheduleWithFixedDelay to comply with the MWS API Throttling 
	 * ********************************************************************
	 * </pre>
	 */
	private static final ScheduledExecutorService scheduledExecutorService;
	static {
		scheduledExecutorService = Executors.newScheduledThreadPool(5);

		/**
		 * Insert all status orders into IMS from MWS Orders API
		 */
		scheduledExecutorService.scheduleWithFixedDelay(ListOrdersTimerTask.getInstance(), INITIAL_DELAY_LIST_ORDERS,
				DELAY_LIST_ORDERS, TIME_UNIT);

		/**
		 * Insert order items that order is not pending status and order has no related
		 * order items into IMS from MWS Orders API
		 */
		scheduledExecutorService.scheduleWithFixedDelay(
				ListOrderItemsTimerTask
						.getInstance(ListOrderItemsTimerTask.WorkType.INSERT_BY_INTERNAL_SET_AMAZON_ORDER_ID),
				INITIAL_DELAY_LIST_ORDER_ITEMS, DELAY_LIST_ORDER_ITEMS, TIME_UNIT);

		/**
		 * Update orders that status changed from pending to non-pending, and schedule
		 * ListOrderItemsTimerTask to asynchronously insert order items for these orders
		 */
		scheduledExecutorService.scheduleWithFixedDelay(GetOrderTimerTask.getInstance(), INITIAL_DELAY_GET_ORDER,
				DELAY_GET_ORDER, TIME_UNIT);

		/**
		 * Update product inventory by SKU
		 */
		scheduledExecutorService.scheduleWithFixedDelay(ListInventorySupplyTimerTask.getInstance(),
				INITIAL_DELAY_LIST_INVENTORY_SUPPLY, DELAY_LIST_INVENTORY_SUPPLY, TIME_UNIT);
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
