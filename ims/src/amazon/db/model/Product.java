package amazon.db.model;

/**
 * for Inventory Report Uploading
 * 
 * @author Eric Li
 *
 */
public class Product {
	private String sku;
	private String asin;
	private double price;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [sku=" + sku + ", asin=" + asin + ", price=" + price + "]";
	}

}
