package amazon.mws.fulfillment;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;

import amazon.db.edit.InventoryReportDatabase;
import amazon.db.model.Product;

/**
 * extends Observable to notify other related functions
 *
 * @see java.util.Observable
 */
public class ListInventoryManager extends Observable {

	/**
	 * Call MWS to get FBA inventory of the SKUs and update the database
	 */
	public static int updateInventory(List<String> skus) {
		return update(skus);
	}

	/**
	 * Call MWS to get FBA inventory of the SKU and update the database
	 */
	public static int updateInventory(String sku) {
		List<String> skus = new ArrayList<>();
		skus.add(sku);
		return update(skus);
	}

	/**
	 * Call MWS to get FBA inventory and update inventory to database
	 *
	 * @param skus
	 *            skus.size()<=SKU_MAX_NUM
	 * @return updated rows
	 */
	private static int update(List<String> skus) {
		ListInventorySupplyResponse response = ListInventorySupplyMWS.listInventory(skus);
		List<InventorySupply> supplys = response.getListInventorySupplyResult().getInventorySupplyList().getMember();
		return new ListInventoryDatabase().update(supplys);
	}

	public int updateAll() {
		int totalUpdateRows = 0;// updated total row numbers
		String msg;// message for updating information
		// notify observers the messages start
		startMessage();
		List<Product> products = new InventoryReportDatabase().selectAll();
		// set message
		msg = products.size() + " SKU start to update FBA inventory:";
		System.out.println(msg);
		// notify observers to add a message
		addMessage(msg);

		int skuNum = 0;// index of product list
		// initiate sku list
		List<String> skus = new ArrayList<>();
		int batchUpdateRows;// update num for one batch
		for (Product product : products) {
			// add to sku list,
			// and if sku size reach SKU_MAX_NUM should call MWS and reset the
			// sku list
			skus.add(product.getSku());
			skuNum++;
			if (skuNum % ListInventorySupplyMWS.SKU_MAX_NUM == 0 || skuNum == products.size()) {
				// call MWS and update inventory in database
				batchUpdateRows = updateInventory(skus);
				totalUpdateRows += batchUpdateRows;
				// set message
				msg = batchUpdateRows + "/" + skus.size() + " " + totalUpdateRows + "/" + products.size()
						+ " SKU updated";
				System.out.println(msg);
				// notify observers to add a message
				addMessage(msg);

				// reset sku list
				skus = new ArrayList<>();
			}
		}

		// set message
		msg = "A total of " + totalUpdateRows + "/" + products.size() + " SKU are updated.";
		System.out.println(msg);
		// notify observers to add a message
		addMessage(msg);
		// notify observers the messages start
		endMessage();

		return totalUpdateRows;
	}

	/**
	 * notify observers the messages start
	 */
	private void startMessage() {
		setChanged();
		// Boolean is a flag for messages start or end: false=start; true=end
		notifyObservers(false);
	}

	/**
	 * notify observers to add a message
	 *
	 * @param message
	 *            the message to be added
	 */
	private void addMessage(String message) {
		setChanged();
		// String is a flag for adding a message
		notifyObservers(message);
	}

	/**
	 * notify observers the messages end
	 */
	private void endMessage() {
		setChanged();
		// Boolean is a flag for messages start or end: false=start; true=end
		notifyObservers(true);
	}

	public static void main(String[] args) {

		// update special skus inventory
		//
		List<String> skus = new ArrayList<>();
		skus.add("1L-8PAW-7QJ2");

		int rows = ListInventoryManager.updateInventory(skus);
		System.out.println(rows);

		// update all skus inventory exists in database
		//
		// int rows = ListInventoryManager.updateAll();
		// System.out.println(rows);
	}
}