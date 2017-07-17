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
	 * return a HTML down/up arrow or empty :&#8595; / &#8593; or ""
	 * 
	 * @param orderBy
	 * @param orderByKey
	 * @param orderByMap
	 * @param ascDesc
	 * @param ascDescKey
	 * @return
	 */
	public static String getHtmlArrow(String orderByKey, String orderBy, Map<String, String> orderByMap,
			String ascDesc) {
		String empty = "";
		String down = "&#8595;";
		String up = "&#8593;";
		if (orderByKey == null || "" == orderByKey.trim() || orderBy == null || "" == orderBy.trim()
				|| orderByMap == null || orderByMap.size() == 0 || ascDesc == null || "" == ascDesc.trim()
				|| (!ASCENDING.equals(ascDesc) && !DESCENDING.equals(ascDesc))) {
			return empty;
		}

		if (orderByKey.equalsIgnoreCase(orderBy) && orderByMap.containsKey(orderByKey)) {
			return ASCENDING.equals(ascDesc) ? up : down;
		}
		return empty;
	}

	/**
	 * get opposite sort order between ASCENDING and DESCENDING
	 * 
	 * if the original sort order is not ASCENDING or DESCENDING, than return
	 * itself
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
