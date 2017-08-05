package amazon.mws.fulfillment;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;
import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.ListInventorySupplyResponse;

public class ListInventorySupplyMWSTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		List<String> skus = new ArrayList<String>();
		skus.add("0B-K1QZ-4RUE");
		ListInventorySupplyResponse response = ListInventorySupplyMWS.listInventory(skus);

		List<InventorySupply> member = response.getListInventorySupplyResult().getInventorySupplyList().getMember();
		for (InventorySupply supply : member) {
			System.out.println(supply.getASIN());
			System.out.println(supply.getCondition());
			System.out.println(supply.getEarliestAvailability() == null ? null
					: supply.getEarliestAvailability().getTimepointType());
			System.out.println(supply.getFNSKU());
			System.out.println(supply.getInStockSupplyQuantity());
			System.out.println(supply.getSellerSKU());
			System.out.println(supply.getTotalSupplyQuantity());
		}
	}

}