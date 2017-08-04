package amazon.mws.product;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.amazonservices.mws.products.model.AttributeSetList;
import com.amazonservices.mws.products.model.GetMatchingProductResponse;
import com.amazonservices.mws.products.model.GetMatchingProductResult;
import com.amazonservices.mws.products.model.IdentifierType;
import com.amazonservices.mws.products.model.Product;
import com.amazonservices.mws.products.model.SalesRankList;
import com.amazonservices.mws.products.model.SalesRankType;

public class GetMatchingProductMWSTest2 {

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
	public void testGetMatchingProduct() {
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

	private void printProduct(Product product) {
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

	private void printAttributeSetList(AttributeSetList attributeSetList) {
		if (attributeSetList == null) {
			System.out.println("attributeSetList=" + null);
			return;
		}
		if (attributeSetList.getAny() == null) {
			System.out.println("attributeSetList.getAny()=" + null);
			return;
		} else {
			System.out.println("attributeSetList.getAny().size()=" + attributeSetList.getAny().size());
		}
		System.out.println("attributeSetList.toXML()=" + attributeSetList.toXML());
		System.out.println("attributeSetList.toXMLFragment()=" + attributeSetList.toXMLFragment());

		@SuppressWarnings("unchecked")
		List<Element> elements = (List<Element>) (Object) attributeSetList.getAny();
		System.out.println("elements.size()=" + elements.size());

		String namespaceURI = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd";
		for (Element element : elements) {
			if (element.hasChildNodes()) {
				System.out.println("element.getChildNodes.getLength=" + element.getChildNodes().getLength());
				System.out.println("element.getChildNodes=" + element.getChildNodes());
			}

			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node childNode = element.getChildNodes().item(i);
				if (childNode != null) {
					System.out.println("print node " + i);
					printNode(childNode);

					String localName = childNode.getLocalName();
					String textContent = childNode.getTextContent();
					if (namespaceURI.equalsIgnoreCase(childNode.getNamespaceURI())) {
						switch (localName) {
						case "Binding":
							System.out.println("binding=" + textContent);
							break;
						case "Brand":
							System.out.println("brand=" + textContent);
							break;
						case "Title":
							System.out.println("title=" + textContent);
							break;
						case "SmallImage":
							System.out.println("smallImage=" + textContent);
							break;
						case "ProductGroup":
							System.out.println("productGroup=" + textContent);
							break;

						default:
							break;
						}
					}

				} else {
					System.out.println("node " + i + " is null");
				}
			}
		}
	}

	private void printNode(Node node) {
		System.out.println("node.getNodeType=" + node.getNodeType());
		System.out.println("node.getNodeName=" + node.getNodeName());
		System.out.println("node.getNamespaceURI=" + node.getNamespaceURI());
		System.out.println("node.getNodeValue=" + node.getNodeValue());
		System.out.println("node.getTextContent=" + node.getTextContent());
		System.out.println("node.getBaseURI=" + node.getBaseURI());
		System.out.println("node.getLocalName=" + node.getLocalName());
		System.out.println("node.getPrefix=" + node.getPrefix());
		System.out.println("node.getAttributes=" + node.getAttributes());
		if (node.getAttributes() != null)
			System.out.println("node.getAttributes.getLength=" + node.getAttributes().getLength());
		System.out.println("node.hasChildNodes=" + node.hasChildNodes());
		System.out.println("node.getChildNodes=" + node.getChildNodes());
		if (node.getChildNodes() != null)
			System.out.println("node.getChildNodes.getLength=" + node.getChildNodes().getLength());
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