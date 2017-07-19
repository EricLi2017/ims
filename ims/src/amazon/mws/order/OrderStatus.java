package amazon.mws.order;

/**
 * Amazon Order Status
 * Created by Eric Li on 2/28/2017.
 */
public enum OrderStatus {

    /**
     * This status is available for pre-orders only. The order has been placed, payment has not been authorized, and the release date of the item is in the future. The order is not ready for shipment. Note that Preorder is a possible OrderType value in Japan (JP) only.
     */
    PendingAvailability,
    /**
     * The order has been placed but payment has not been authorized. The order is not ready for shipment. Note that for orders with OrderType = Standard, the initial order status is Pending. For orders with OrderType = Preorder (available in JP only), the initial order status is PendingAvailability, and the order passes into the Pending status when the payment authorization process begins.
     */
    Pending,
    /**
     * Payment has been authorized and order is ready for shipment, but no items in the order have been shipped.
     */
    Unshipped,
    /**
     * One or more (but not all) items in the order have been shipped.
     */
    PartiallyShipped,
    /**
     * All items in the order have been shipped.
     */
    Shipped,
    /**
     * All items in the order have been shipped. The seller has not yet given confirmation to Amazon that the invoice has been shipped to the buyer. Note: This value is available only in China (CN).
     */
    InvoiceUnconfirmed,
    /**
     * The order was canceled.
     */
    Canceled,
    /**
     * The order cannot be fulfilled. This state applies only to Amazon-fulfilled orders that were not placed on Amazon's retail web site.
     */
    Unfulfillable;

    public String value() {
        return name();
    }

    public static OrderStatus fromValue(String v) {
        return valueOf(v);
    }

}


