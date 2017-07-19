package amazon.mws.order;

import java.math.BigDecimal;

public class SkuSum {
	private String sku;
	private BigDecimal priceAmount;
	private BigDecimal discountAmount;
	private Integer quantityOrdered;
	private Integer quantityShipped;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getPriceAmount() {
		return priceAmount;
	}

	public void setPriceAmount(BigDecimal priceAmount) {
		this.priceAmount = priceAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Integer getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(Integer quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	@Override
	public String toString() {
		return "SkuSum [sku=" + sku + ", priceAmount=" + priceAmount + ", discountAmount=" + discountAmount
				+ ", quantityOrdered=" + quantityOrdered + ", quantityShipped=" + quantityShipped + "]";
	}

}
