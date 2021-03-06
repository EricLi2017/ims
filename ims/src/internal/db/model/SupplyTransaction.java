package internal.db.model;

import java.math.BigDecimal;
import java.util.Date;

public class SupplyTransaction {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.supply_id
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private Integer supplyId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.quantity
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private Integer quantity;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.unit_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private BigDecimal unitPrice;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.price_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private String priceDescription;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.status
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private String status;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.batch_no
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private Integer batchNo;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.product_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private BigDecimal productPrice;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.shipped_fee
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private BigDecimal shippedFee;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.time
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private Date time;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.operator
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private String operator;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column supply_transaction.transaction_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	private String transactionDescription;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.supply_id
	 * @return  the value of supply_transaction.supply_id
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public Integer getSupplyId() {
		return supplyId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.supply_id
	 * @param supplyId  the value for supply_transaction.supply_id
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.quantity
	 * @return  the value of supply_transaction.quantity
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.quantity
	 * @param quantity  the value for supply_transaction.quantity
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.unit_price
	 * @return  the value of supply_transaction.unit_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.unit_price
	 * @param unitPrice  the value for supply_transaction.unit_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.price_description
	 * @return  the value of supply_transaction.price_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public String getPriceDescription() {
		return priceDescription;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.price_description
	 * @param priceDescription  the value for supply_transaction.price_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.status
	 * @return  the value of supply_transaction.status
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.status
	 * @param status  the value for supply_transaction.status
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.batch_no
	 * @return  the value of supply_transaction.batch_no
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public Integer getBatchNo() {
		return batchNo;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.batch_no
	 * @param batchNo  the value for supply_transaction.batch_no
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.product_price
	 * @return  the value of supply_transaction.product_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public BigDecimal getProductPrice() {
		return productPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.product_price
	 * @param productPrice  the value for supply_transaction.product_price
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.shipped_fee
	 * @return  the value of supply_transaction.shipped_fee
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public BigDecimal getShippedFee() {
		return shippedFee;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.shipped_fee
	 * @param shippedFee  the value for supply_transaction.shipped_fee
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setShippedFee(BigDecimal shippedFee) {
		this.shippedFee = shippedFee;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.time
	 * @return  the value of supply_transaction.time
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.time
	 * @param time  the value for supply_transaction.time
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.operator
	 * @return  the value of supply_transaction.operator
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.operator
	 * @param operator  the value for supply_transaction.operator
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column supply_transaction.transaction_description
	 * @return  the value of supply_transaction.transaction_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public String getTransactionDescription() {
		return transactionDescription;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column supply_transaction.transaction_description
	 * @param transactionDescription  the value for supply_transaction.transaction_description
	 * @mbg.generated  Wed Jul 12 10:16:34 CST 2017
	 */
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}
}