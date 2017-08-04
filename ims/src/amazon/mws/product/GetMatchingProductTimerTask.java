/**
 * 
 */
package amazon.mws.product;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.amazonservices.mws.products.model.GetMatchingProductResult;

import amazon.db.edit.AmazonProductEditor;
import amazon.db.query.AmazonProductQuerier;
import amazon.mws.MWSTimerTask;
import common.util.Page;

/**
 * Update product ItemAttribute and SalesRank by ASIN
 * 
 * Created by Eclipse on Aug 4, 2017 at 12:01:55 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class GetMatchingProductTimerTask extends MWSTimerTask {
	private static GetMatchingProductTimerTask getMatchingProductTimerTask = new GetMatchingProductTimerTask();

	private GetMatchingProductTimerTask() {

	}

	public static GetMatchingProductTimerTask getInstance() {
		return getMatchingProductTimerTask;
	}

	private static Log log = LogFactory.getLog(GetMatchingProductTimerTask.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see amazon.mws.MWSTimerTask#work()
	 */
	@Override
	protected void work() throws Exception {
		// init
		int mwsCalledTimes = 0;
		List<String> asins = AmazonProductQuerier.selectAllDistinctAsin();
		log.info(getLogPrefix() + ": There are a total of " + asins.size() + " ASINs.");

		// Call ListInventorySupply and update database
		int total = asins.size();
		int subSize = GetMatchingProductMWS.MAX_SIZE_ASIN_LIST;
		int subMaxIndex = total % subSize == 0 ? total / subSize : total / subSize + 1;
		int subIndex = 0;

		while (++subIndex <= subMaxIndex) {
			// catch exception for all the loop can be executed
			try {
				// wait a safe restore period for next call when request quota reached
				if (!(++mwsCalledTimes <= GetMatchingProductMWS.REQUEST_QUOTA)) {
					log.info(getLogPrefix() + ": mwsCalledTimes reached request quota, start to sleep "
							+ GetMatchingProductMWS.getSafeRestorePeriod() / 1000 + " seconds");
					Thread.sleep(GetMatchingProductMWS.getSafeRestorePeriod());
					mwsCalledTimes = 1;
					log.info(getLogPrefix() + ": sleeping ended and reset mwsCalledTimes to "+mwsCalledTimes);
				}

				// get sub ASINs
				List<String> subAsins = Page.getSub(asins, subIndex, subSize);
				log.info(getLogPrefix() + ": (" + subIndex + "/" + subMaxIndex + ") process the sub " + subAsins.size()
						+ " ASINs");

				// call
				GetMatchingProductResponse response = GetMatchingProductMWS.getMatchingProduct(subAsins);
				List<GetMatchingProductResult> results = response.getGetMatchingProductResult();
				log.info(getLogPrefix() + ": (" + subIndex + "/" + subMaxIndex + ") get " + results.size()
						+ " ASINs results");

				// update database
				if (results.size() > 0) {
					int update = AmazonProductEditor.updateAttributeAndSalesRankByAsin(results);
					log.info(getLogPrefix() + ": (" + subIndex + "/" + subMaxIndex
							+ ") updateAttributeAndSalesRankByAsin " + update
							+ " rows(by SKU) amazon product updated by" + results.size() + " ASINs");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// set ready for the next scheduled task running
		log.info(getLogPrefix() + ": ready()");
		ready();
	}
}