/**
 * 
 */
package common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 14, 2017 Time: 3:24:45 PM
 */
public class OrderBy {
	public static final String ASCENDING = "0";
	public static final String DESCENDING = "1";

	// ascending,descending
	public static final Map<String, String> ASC_DESC_MAP;
	static {
		ASC_DESC_MAP = new HashMap<>();
		ASC_DESC_MAP.put(DESCENDING, "desc");
		ASC_DESC_MAP.put(ASCENDING, "asc");
	}

	public enum Direction {
		UP, DOWND, LEFT, RIGHT
	}

	private String sortedColumnCurrent;
	private Map<String, String> orderByMap;
	private String sortOrderCurrent;

	public OrderBy(String sortedColumnCurrent, Map<String, String> orderByMap, String sortOrderCurrent) {
		super();
		this.sortedColumnCurrent = sortedColumnCurrent;
		this.orderByMap = orderByMap;
		this.sortOrderCurrent = sortOrderCurrent;
	}

	/**
	 * Get the HTML arrow in the sorted column when the column be showed
	 * 
	 * Get a HTML up/down arrow in the current sort order: If the current sort
	 * condition is valid and the current sorted column is same with the sorted
	 * column where the HTML arrow located;
	 * 
	 * Or get a empty string "": In any other cases
	 * 
	 * @param sortedColumnLocated
	 * @return a HTML down/up arrow or empty string: &uarr;(&#8595;) /
	 *         &darr;(&#8593;) or ""
	 */
	public String getHtmlArrow(String sortedColumnLocated) {
		// the HTML format of arrow depends on the method getHtml()
		//

		// check if the current sort condition is valid
		if (sortedColumnCurrent == null || sortedColumnCurrent.trim().isEmpty() || orderByMap == null
				|| orderByMap.size() == 0 || !orderByMap.containsKey(sortedColumnCurrent)
				|| (!ASCENDING.equals(sortOrderCurrent) && !DESCENDING.equals(sortOrderCurrent))) {
			return getHtml(null);
		}

		// check if the current sorted column is same with the sorted column
		// where the HTML arrow located
		//
		// same column
		if (sortedColumnCurrent.equalsIgnoreCase(sortedColumnLocated)) {
			// in the current sort order
			return getHtml(ASCENDING.equals(sortOrderCurrent) ? Direction.UP : Direction.DOWND);
		} else {
			// different column
			return getHtml(null);
		}
	}

	/**
	 * Get a HTML arrow if the direction is valid
	 * 
	 * or get a empty string ""
	 * 
	 * @param direction
	 * @return
	 */
	protected String getHtml(Direction direction) {
		if (direction != null) {
			switch (direction) {
			case UP:
				return "&uarr;";
			case DOWND:
				return "&darr;";
			case LEFT:
				return "&larr;";
			case RIGHT:
				return "&rarr;";
			}
		}

		return "";
	}

	/**
	 * Get the HTML arrow in the sorted column when the column be clicked
	 * 
	 * Get a HTML up/down arrow:
	 * 
	 * In the current sort order if the clicked sort condition is valid and the
	 * sorted column clicked is different with the current sorted column;
	 * 
	 * In the opposite sort order if the clicked sort condition is valid and the
	 * sorted column clicked is same with the current sorted column;
	 * 
	 * Or get a empty string "": In any other cases
	 * 
	 * @param sortedColumnClicked
	 * @return
	 */
	public String getHtmlArrowClicked(String sortedColumnClicked) {
		// the HTML format of arrow depends on the method getHtml()
		//

		// check if the clicked sort condition is valid
		if (sortedColumnClicked == null || sortedColumnClicked.trim().isEmpty() || orderByMap == null
				|| orderByMap.size() == 0 || !orderByMap.containsKey(sortedColumnClicked)
				|| (!ASCENDING.equals(sortOrderCurrent) && !DESCENDING.equals(sortOrderCurrent))) {
			return getHtml(null);
		}

		// check if the clicked sorted column is same with the current sorted
		// column
		//
		// different column
		if (!sortedColumnClicked.equalsIgnoreCase(sortedColumnCurrent)) {
			// in the current sort order
			return getHtml(ASCENDING.equals(sortOrderCurrent) ? Direction.UP : Direction.DOWND);
		} else {// same column
			// in the opposite sort order
			return getHtml(ASCENDING.equals(sortOrderCurrent) ? Direction.DOWND : Direction.UP);
		}
	}

	/**
	 * Get an opposite sort order between ASCENDING and DESCENDING
	 * 
	 * or
	 * 
	 * get itself if the original sort order is neither ASCENDING nor DESCENDING
	 * 
	 * @param sortOrder
	 * @return
	 */
	public static String getOppositeSortOrder(String sortOrder) {
		if (ASCENDING.equals(sortOrder))
			return DESCENDING;
		if (DESCENDING.equals(sortOrder))
			return ASCENDING;

		return sortOrder;
	}

//	public static Direction getDirection(String sortOrder) {
//		if (sortOrder != null) {
//			switch (sortOrder) {
//			case ASCENDING:
//
//				return Direction.UP;
//			case DESCENDING:
//				return Direction.DOWND;
//			}
//		}
//		return null;
//	}
}
