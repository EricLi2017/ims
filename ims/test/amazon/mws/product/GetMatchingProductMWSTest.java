package amazon.mws.product;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonservices.mws.products.model.AttributeSetList;
import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.amazonservices.mws.products.model.GetMatchingProductResult;
import com.amazonservices.mws.products.model.IdentifierType;
import com.amazonservices.mws.products.model.Product;
import com.amazonservices.mws.products.model.SalesRankList;
import com.amazonservices.mws.products.model.SalesRankType;

public class GetMatchingProductMWSTest {

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
	public void testGetMatchingProduct() throws JAXBException {
		List<String> asinList = new ArrayList<>();
		asinList.add("");
		asinList.add("");

		GetMatchingProductResponse response = GetMatchingProductMWS.getMatchingProduct(asinList);
		List<GetMatchingProductResult> results = response.getGetMatchingProductResult();
		int i = 0;
		for (GetMatchingProductResult result : results) {
			System.out.println("result " + ++i + "------------------------------------------");
			System.out.println("asin=" + result.getASIN());
			System.out.println("status=" + result.getStatus());
			System.out.println("error=" + result.getError());
			printProduct(result.getProduct());
		}

	}

	private void printProduct(Product product) throws JAXBException {
		printIdentifierType(product.getIdentifiers());
		printAttributeSetList(product.getAttributeSets());
		printSalesRankList(product.getSalesRankings());

		System.out.println("Offers=" + product.getOffers());
		System.out.println("Relationships=" + product.getRelationships());
		System.out.println("CompetitivePricing=" + product.getCompetitivePricing());
		System.out.println("LowestOfferListings()=" + product.getLowestOfferListings());

	}

	private void printIdentifierType(IdentifierType identifierType) {
		if (identifierType == null) {
			System.out.println("identifierType=" + null);
			return;
		}

		if (identifierType.getMarketplaceASIN() == null) {
			System.out.println("identifierType.getMarketplaceASIN()=" + null);
		} else {
			System.out.println("getMarketplaceASIN().getASIN()=" + identifierType.getMarketplaceASIN().getASIN());
			System.out.println("getMarketplaceASIN().getMarketplaceId()="
					+ identifierType.getMarketplaceASIN().getMarketplaceId());
		}

		if (identifierType.getSKUIdentifier() == null) {
			System.out.println("identifierType.getSKUIdentifier()=" + null);
		} else {
			System.out.println(
					"getSKUIdentifier().getMarketplaceId()=" + identifierType.getSKUIdentifier().getMarketplaceId());
			System.out.println("getSKUIdentifier().getSellerId()=" + identifierType.getSKUIdentifier().getSellerId());
			System.out.println("getSKUIdentifier().getSellerSKU()=" + identifierType.getSKUIdentifier().getSellerSKU());
		}
	}

	private void printAttributeSetList(AttributeSetList attributeSetList) throws JAXBException {
		if (attributeSetList == null) {
			System.out.println("attributeSetList=" + null);
			return;
		}
		if (attributeSetList.getAny() == null) {
			System.out.println("attributeSetList.getAny()=" + null);
			return;
		}

		for (Object object : attributeSetList.getAny()) {
			System.out.println("getAny=" + object);
		}
		System.out.println("attributeSetList.toXML()=" + attributeSetList.toXML());
		System.out.println("attributeSetList.toXMLFragment()=" + attributeSetList.toXMLFragment());

		ItemAttributes itemAttributes = ItemAttributes.parse(attributeSetList);
		if (itemAttributes != null) {
			System.out.println("itemAttributes.getBinding=" + itemAttributes.getBinding());
			System.out.println("itemAttributes.getBrand=" + itemAttributes.getBrand());
			System.out.println("itemAttributes.getTitle=" + itemAttributes.getTitle());
			System.out.println("itemAttributes.getPublisher=" + itemAttributes.getPublisher());
			System.out.println("itemAttributes.getProductGroup=" + itemAttributes.getProductGroup());
			System.out.println("itemAttributes.getProductTypeName=" + itemAttributes.getProductTypeName());
			System.out.println("itemAttributes.getFeatures=" + itemAttributes.getFeatures());
			System.out.println("itemAttributes.getFeatures.size=" + itemAttributes.getFeatures().size());
			System.out.println("itemAttributes.getSmallImage.getUrl=" + itemAttributes.getSmallImage().getUrl());
		} else {
			System.out.println("itemAttributes=" + itemAttributes);
		}

	}

	private void printSalesRankList(SalesRankList salesRankList) {
		if (salesRankList == null) {
			System.out.println("salesRankList=" + null);
			return;
		}
		if (salesRankList.getSalesRank() == null) {
			System.out.println("salesRankList.getSalesRank()=" + null);
			return;
		}

		for (SalesRankType salesRankType : salesRankList.getSalesRank()) {
			System.out.print("sales rank is " + salesRankType.getRank() + " in ");
			System.out.println(salesRankType.getProductCategoryId());
		}
	}

}