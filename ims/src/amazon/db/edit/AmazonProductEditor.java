package amazon.db.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonservices.mws.FulfillmentInventory._2010_10_01.model.InventorySupply;
import com.amazonservices.mws.products.model.GetMatchingProductResult;
import com.amazonservices.mws.products.model.SalesRankList;
import com.amazonservices.mws.products.model.SalesRankType;

import amazon.mws.product.ItemAttributes;
import common.db.DB;

public class AmazonProductEditor {
	private static Log log = LogFactory.getLog(AmazonProductEditor.class);

	public static final int COUNT_OF_FEATURE = 5;
	public static final int COUNT_OF_RANK = 3;

	/**
	 * update :fnsku=?,total_supply_quantity=?,in_stock_supply_quantity=? by sku
	 */
	public static final int update(List<InventorySupply> supplys) {
		int rows = 0;
		if (supplys == null || supplys.size() == 0)
			return 0;

		String sql = "update amazon_product set fnsku=?,total_supply_quantity=?,in_stock_supply_quantity=? where sku=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			for (InventorySupply supply : supplys) {
				// Not in FBA inventory: the supply condition and fnsku will return null (and
				// the supply inventory will return 0)
				if (supply.getCondition() == null || "NewItem".equalsIgnoreCase(supply.getCondition())) {
					ps = con.prepareStatement(sql);
					ps.setString(1, supply.getFNSKU());
					ps.setString(2,
							(supply.getTotalSupplyQuantity() == null
									|| (supply.getCondition() == null && supply.getTotalSupplyQuantity() == 0)) ? null
											: supply.getTotalSupplyQuantity().toString());
					ps.setString(3,
							(supply.getInStockSupplyQuantity() == null
									|| (supply.getCondition() == null && supply.getInStockSupplyQuantity() == 0)) ? null
											: supply.getInStockSupplyQuantity().toString());
					ps.setString(4, supply.getSellerSKU());

					rows += ps.executeUpdate();
					ps.close();
				}
			}
			con.close();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			boolean flag = true;
			try {
				if (con == null || con.isClosed()) {
					flag = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (flag) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return rows;
	}

	public static final int updateAttributeAndSalesRankByAsin(List<GetMatchingProductResult> results) {
		if (results == null || results.size() == 0)
			return 0;

		int rows = 0;
		for (GetMatchingProductResult result : results) {
			try {
				rows += updateAttributeAndSalesRankByAsin(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rows;
	}

	public static final int updateAttributeAndSalesRankByAsin(GetMatchingProductResult result) {
		if (result == null || result.getASIN() == null || result.getProduct() == null)
			return 0;
		if (!"Success".equalsIgnoreCase(result.getStatus())) {
			log.error("The status of GetMatchingProductResult is not Success!");
			return 0;
		}
		int rows = 0;

		String sql = "UPDATE amazon_product SET title=?,binding=?,brand=?,product_group=?,product_type_name=?,small_image_url=?,feature1=?,feature2=?,feature3=?,feature4=?,feature5=?,rank1=?,rank2=?,rank3=?,product_category_id1=?,product_category_id2=?,product_category_id3=? WHERE asin=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			// parse result for product
			ProductWithAttributeAndSalesRank product = parse(result);

			// update product in database
			if (product != null && product.getAsin() != null) {
				ps = con.prepareStatement(sql);
				ps.setString(1, product.getTitle());
				ps.setString(2, product.getBinding());
				ps.setString(3, product.getBrand());
				ps.setString(4, product.getProductGroup());
				ps.setString(5, product.getProductTypeName());
				ps.setString(6, product.getSmallImageUrl());
				ps.setString(7, product.getFeatures1());
				ps.setString(8, product.getFeatures2());
				ps.setString(9, product.getFeatures3());
				ps.setString(10, product.getFeatures4());
				ps.setString(11, product.getFeatures5());
				ps.setInt(12, product.getRank1());
				ps.setInt(13, product.getRank2());
				ps.setInt(14, product.getRank3());
				ps.setString(15, product.getProductCategoryId1());
				ps.setString(16, product.getProductCategoryId2());
				ps.setString(17, product.getProductCategoryId3());
				ps.setString(18, product.getAsin());

				rows += ps.executeUpdate();
				ps.close();
			}

			con.close();
		} catch (SQLException |

				NamingException e) {
			e.printStackTrace();
		} finally {
			boolean flag = true;
			try {
				if (con == null || con.isClosed()) {
					flag = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (flag) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return rows;
	}

	public static final int updateLevelByAsin(int level, String sku) {
		if (sku == null)
			return 0;
		int rows = 0;

		String sql = "UPDATE amazon_product SET level=? WHERE sku=?";
		Connection con = null;
		try {
			con = DB.getConnection();
			PreparedStatement ps;
			ps = con.prepareStatement(sql);
			ps.setInt(1, level);
			ps.setString(2, sku);
			rows = ps.executeUpdate();

			ps.close();
			con.close();
		} catch (SQLException |

				NamingException e) {
			e.printStackTrace();
		} finally {
			boolean flag = true;
			try {
				if (con == null || con.isClosed()) {
					flag = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (flag) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return rows;
	}

	/**
	 * Parse GetMatchingProductResult to get ProductWithAttributeAndSalesRank
	 * 
	 * @param result
	 * @return
	 */
	static final ProductWithAttributeAndSalesRank parse(GetMatchingProductResult result) {
		// set ASIN of product
		ProductWithAttributeAndSalesRank product = new ProductWithAttributeAndSalesRank();
		product.setAsin(result.getASIN());

		// set ItemAttributes of product
		ItemAttributes itemAttributes = null;
		try {
			if (result.getProduct().getAttributeSets() != null)
				itemAttributes = ItemAttributes.parse(result.getProduct().getAttributeSets());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (itemAttributes != null) {
			product.setBinding(itemAttributes.getBinding());
			product.setBrand(itemAttributes.getBrand());
			product.setProductGroup(itemAttributes.getProductGroup());
			product.setProductTypeName(itemAttributes.getProductTypeName());
			product.setPublisher(itemAttributes.getPublisher());
			if (itemAttributes.getSmallImage() != null)
				product.setSmallImageUrl(itemAttributes.getSmallImage().getUrl());
			product.setTitle(itemAttributes.getTitle());
			if (itemAttributes.getFeatures() != null) {
				int i = 0;
				for (String feature : itemAttributes.getFeatures()) {
					i++;
					switch (i) {
					case 1:
						product.setFeatures1(feature);
						break;
					case 2:
						product.setFeatures1(feature);
						break;
					case 3:
						product.setFeatures1(feature);
						break;
					case 4:
						product.setFeatures1(feature);
						break;
					case 5:
						product.setFeatures1(feature);
						break;
					}
				}
			}
		}

		// set SalesRankList of product
		SalesRankList salesRankList = result.getProduct().getSalesRankings();
		if (salesRankList != null) {
			if (salesRankList.getSalesRank() != null) {
				int i = 0;
				for (SalesRankType salesRankType : salesRankList.getSalesRank()) {
					i++;
					switch (i) {
					case 1:
						product.setRank1(salesRankType.getRank());
						product.setProductCategoryId1(salesRankType.getProductCategoryId());
						break;
					case 2:
						product.setRank2(salesRankType.getRank());
						product.setProductCategoryId2(salesRankType.getProductCategoryId());
						break;
					case 3:
						product.setRank3(salesRankType.getRank());
						product.setProductCategoryId3(salesRankType.getProductCategoryId());
						break;
					}
				}

			}
		}

		return product;
	}

	static class ProductWithAttributeAndSalesRank {
		/**
		 * ASIN
		 */
		private String asin;
		/**
		 * ItemAttributes
		 */
		// private ItemAttributes itemAttributes;
		private String binding;
		private String brand;
		private String title;
		private String publisher;
		private String productGroup;
		private String productTypeName;
		private String smallImageUrl;
		private String features1;
		private String features2;
		private String features3;
		private String features4;
		private String features5;
		/**
		 * SalesRank
		 */
		// private SalesRankList salesRankList;
		private int rank1;
		private int rank2;
		private int rank3;
		private String productCategoryId1;
		private String productCategoryId2;
		private String productCategoryId3;

		public String getAsin() {
			return asin;
		}

		public void setAsin(String asin) {
			this.asin = asin;
		}

		public String getBinding() {
			return binding;
		}

		public void setBinding(String binding) {
			this.binding = binding;
		}

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getProductGroup() {
			return productGroup;
		}

		public void setProductGroup(String productGroup) {
			this.productGroup = productGroup;
		}

		public String getProductTypeName() {
			return productTypeName;
		}

		public void setProductTypeName(String productTypeName) {
			this.productTypeName = productTypeName;
		}

		public String getSmallImageUrl() {
			return smallImageUrl;
		}

		public void setSmallImageUrl(String smallImageUrl) {
			this.smallImageUrl = smallImageUrl;
		}

		public String getFeatures1() {
			return features1;
		}

		public void setFeatures1(String features1) {
			this.features1 = features1;
		}

		public String getFeatures2() {
			return features2;
		}

		public void setFeatures2(String features2) {
			this.features2 = features2;
		}

		public String getFeatures3() {
			return features3;
		}

		public void setFeatures3(String features3) {
			this.features3 = features3;
		}

		public String getFeatures4() {
			return features4;
		}

		public void setFeatures4(String features4) {
			this.features4 = features4;
		}

		public String getFeatures5() {
			return features5;
		}

		public void setFeatures5(String features5) {
			this.features5 = features5;
		}

		public int getRank1() {
			return rank1;
		}

		public void setRank1(int rank1) {
			this.rank1 = rank1;
		}

		public int getRank2() {
			return rank2;
		}

		public void setRank2(int rank2) {
			this.rank2 = rank2;
		}

		public int getRank3() {
			return rank3;
		}

		public void setRank3(int rank3) {
			this.rank3 = rank3;
		}

		public String getProductCategoryId1() {
			return productCategoryId1;
		}

		public void setProductCategoryId1(String productCategoryId1) {
			this.productCategoryId1 = productCategoryId1;
		}

		public String getProductCategoryId2() {
			return productCategoryId2;
		}

		public void setProductCategoryId2(String productCategoryId2) {
			this.productCategoryId2 = productCategoryId2;
		}

		public String getProductCategoryId3() {
			return productCategoryId3;
		}

		public void setProductCategoryId3(String productCategoryId3) {
			this.productCategoryId3 = productCategoryId3;
		}

	}
}