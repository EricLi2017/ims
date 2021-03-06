/**
 * 
 */
package amazon.mws.fulfillment;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;
import amazon.db.edit.AmazonProductEditor;
import amazon.db.query.AmazonProductQuerier;
import amazon.mws.MWSTimerTask;
import common.util.Page;

/**
 * Update product inventory by SKU
 * 
 * Updated information: fnsku,total_supply_quantity,in_stock_supply_quantity
 * 
 * Created by Eclipse on Aug 1, 2017 at 3:38:12 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
public class ListInventorySupplyTimerTask extends MWSTimerTask {
	private static final Log log = LogFactory.getLog(ListInventorySupplyTimerTask.class);
	private static final ListInventorySupplyTimerTask listInventorySupplyTimerTask = new ListInventorySupplyTimerTask();

	private ListInventorySupplyTimerTask() {

	}

	public static ListInventorySupplyTimerTask getInstance() {
		return listInventorySupplyTimerTask;
	}

	@Override
	protected void work() throws SQLException, NamingException {
		// init
		int mwsCalledTimes = 0;
		List<String> skus = AmazonProductQuerier.selectAllSku();
		log.info("There are a total of " + skus.size() + " SKUs.");

		// Call ListInventorySupply and update database
		int total = skus.size();
		int subSize = ListInventorySupplyMWS.SKU_MAX_NUM;
		int subMaxIndex = total % subSize == 0 ? total / subSize : total / subSize + 1;
		int subIndex = 0;
		while (++mwsCalledTimes <= ListInventorySupplyMWS.REQUEST_QUOTA && ++subIndex <= subMaxIndex) {
			// get sub SKUs
			List<String> subSkus = Page.getSub(skus, subIndex, subSize);
			log.info("(" + subIndex + "/" + subMaxIndex + ") process the sub " + subSkus.size() + " SKUs");

			// catch exception for all the loop can be executed
			try {
				// call
				ListInventorySupplyResponse response = ListInventorySupplyMWS.listInventory(subSkus);
				List<InventorySupply> supplys = response.getListInventorySupplyResult().getInventorySupplyList()
						.getMember();
				log.info("(" + subIndex + "/" + subMaxIndex + ") get " + supplys.size() + " SKUs inventory supply");

				// update database
				if (supplys.size() > 0) {
					int update = AmazonProductEditor.update(supplys);
					log.info("(" + subIndex + "/" + subMaxIndex + ") update " + update + "/" + supplys.size()
							+ " amazon product");
				}
			} catch (Exception e) {
				log.error("(" + subIndex + "/" + subMaxIndex + ") processiong failed!");
				e.printStackTrace();
			}
		}

		// set ready for the next scheduled task running
		log.info("ready()");
		ready();
	}
}
