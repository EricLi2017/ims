/**
 * 
 */
package common.util;

import java.util.List;

/**
 * Created by Eclipse on Aug 1, 2017 at 4:05:45 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class Page {
	/**
	 * index starts from 1
	 * 
	 * @param list
	 * @param index
	 * @param subSize
	 * @return
	 */
	/**
	 * index should be from 1 to maxIndex
	 * 
	 * @param list
	 * @param index
	 * @param subSize
	 * @return
	 */
	public static final <T> List<T> getSub(List<T> list, int index, int subSize) {
		if (list == null)
			return null;
		int maxIndex = list.size() % subSize == 0 ? list.size() / subSize : list.size() / subSize + 1;
		int endIndex = (index == maxIndex ? list.size() : index * subSize);

		return list.subList((index - 1) * subSize, endIndex);
	}

}
