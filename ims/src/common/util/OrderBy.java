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

	/**
	 * Get a HTML arrow in the opposite sort order if the sorted column selected
	 * is valid and it is the same with the sorted column where the HTML arrow
	 * is located in,
	 * 
	 * otherwise get a empty string in other cases
	 *
	 * 
	 * @param sortedColumnLocated
	 * @param sortedColumnSelected
	 * @param orderByMap
	 * @param ascDesc
	 * @param ascDescKey
	 * @return a HTML down/up arrow or empty string: &uarr;(&#8595;) /
	 *         &darr;(&#8593;) or ""
	 */
	public static String getHtmlArrow(String sortedColumnLocated, String sortedColumnSelected,
			Map<String, String> orderByMap, String ascDesc) {
		String empty = "";
		String down = "&uarr;";
		String up = "&darr;";
		if (sortedColumnLocated == null || sortedColumnLocated.trim().isEmpty() || sortedColumnSelected == null
				|| sortedColumnSelected.trim().isEmpty() || orderByMap == null || orderByMap.size() == 0
				|| ascDesc == null || ascDesc.trim().isEmpty()
				|| (!ASCENDING.equals(ascDesc) && !DESCENDING.equals(ascDesc))) {
			return empty;
		}

		if (sortedColumnLocated.equalsIgnoreCase(sortedColumnSelected) && orderByMap.containsKey(sortedColumnLocated)) {

			// reverse sort order
			return ASCENDING.equals(ascDesc) ? up : down;
		}
		return empty;
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

}
