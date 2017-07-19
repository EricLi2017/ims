package amazon.db.query;

import common.db.DB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Use to query database for tables orders,order_items,products
 * Created by Eric Li on 2/26/2017.
 */
public class QueryProductAndOrder {
    public final static String ORDER_BY_SKU = "d.sku";
    public final static String ORDER_BY_FNSKU = "d.fnsku";
    public final static String ORDER_BY_ASIN = "d.asin";
    public final static String ORDER_BY_UNITS = "c.ordered";
    public final static String ORDER_BY_UNITS_B2B = "c.ordered_b2b";
    public final static String ORDER_BY_SALES = "c.sales";
    public final static String ORDER_BY_SALES_B2B = "c.sales_b2b";
    public final static String ASC = "asc";
    public final static String DESC = "desc";

    /**
     * @return null: if exception happens while query database
     * a empty List: if there is no matched result in database
     */
    public List<ProductAndOrder> querySkuSalesSum(Timestamp createdAfter, Timestamp createdBefore, String sku, String asin, String fnsku, String title, String orderBy, String ascOrDesc) {


        String part1 = "select  d.*,c.* from amazon_product as d left join" +
                "(select b.sku as sku," +
                "sum(if(a.is_business_order='false',b.price_amount-b.discount_amount,null)  ) as sales," +
                "sum(if(a.is_business_order='true',b.price_amount-b.discount_amount,null)  ) as sales_b2b," +
                "sum(if(a.is_business_order='false',b.quantity_ordered,null)) as ordered," +
                "sum(if(a.is_business_order='true',b.quantity_ordered,null)) as ordered_b2b" +
                " from orders as a, order_items as b" +
                " where a.amazon_order_id=b.amazon_order_id and a.order_status!='canceled'";
        String part2 = " group by b.sku) as c" +
                " on c.sku=d.sku";
        //set where condition of table order_items
        if (createdAfter != null) {
            part1 += " and a.purchase_date>='" + createdAfter + "'";
        }
        if (createdBefore != null) {
            part1 += " and a.purchase_date<'" + createdBefore + "'";
        }
        //set where condition of table amazon_product
        String where2 = null;
        if (sku != null && !"".equals(sku.trim())) {
            if (where2 == null) {
                where2 = " where d.sku='" + sku + "'";
            } else {
                where2 = " and d.sku='" + sku + "'";
            }
            part2 += where2;
        }
        if (fnsku != null && !"".equals(fnsku.trim())) {
            if (where2 == null) {
                where2 = " where d.fnsku='" + fnsku + "'";
            } else {
                where2 = " and d.fnsku='" + fnsku + "'";
            }
            part2 += where2;
        }
        if (asin != null && !"".equals(asin.trim())) {
            if (where2 == null) {
                where2 = " where d.asin='" + asin + "'";
            } else {
                where2 = " and d.asin='" + asin + "'";
            }
            part2 += where2;
        }
        if (title != null && !"".equals(title.trim())) {
            if (where2 == null) {
                where2 = " where d.title='" + title + "'";
            } else {
                where2 = " and d.title='" + title + "'";
            }
            part2 += where2;
        }

        //set order by
        if (orderBy != null && !"".equals(orderBy.trim())) {
            part2 += " order by " + orderBy;
            if (ascOrDesc != null && !"".equals(ascOrDesc)) {
                part2 += " " + ascOrDesc;
            }
        } else {
            part2 += " order by c.ordered desc";//default order by
        }

        //complete the sql string
        String sql = part1 + part2;
        System.out.println(sql);//TODO

        List<ProductAndOrder> productAndOrders = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductAndOrder productAndOrder = new ProductAndOrder();
                productAndOrder.setSku(rs.getString("d.sku"));
                productAndOrder.setFnsku(rs.getString("fnsku"));
                productAndOrder.setAsin(rs.getString("asin"));
                productAndOrder.setTitle(rs.getString("title"));
                productAndOrder.setYourPrice(rs.getBigDecimal("your_price"));
                productAndOrder.setBinding(rs.getString("binding"));
                productAndOrder.setBrand(rs.getString("brand"));
                productAndOrder.setProductGroup(rs.getString("product_group"));
                productAndOrder.setProductTypeName(rs.getString("product_type_name"));
                productAndOrder.setImage(rs.getString("small_image_url"));
                productAndOrder.setSalesRank(rs.getString("sales_rank") == null ? null : rs.getInt("sales_rank"));
                productAndOrder.setFbaTotal(rs.getString("total_supply_quantity") == null ? null : rs.getInt("total_supply_quantity"));
                productAndOrder.setFbaInStock(rs.getString("in_stock_supply_quantity") == null ? null : rs.getInt("in_stock_supply_quantity"));
                productAndOrder.setUnitsOrdered(rs.getString("ordered") == null ? null : rs.getInt("ordered"));
                productAndOrder.setUnitsOrderedB2B(rs.getString("ordered_b2b") == null ? null : rs.getInt("ordered_b2b"));
                productAndOrder.setOrderedProductSales(rs.getBigDecimal("sales"));
                productAndOrder.setOrderedProductSalesB2B(rs.getBigDecimal("sales_b2b"));

                productAndOrders.add(productAndOrder);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
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

        return productAndOrders;
    }

    public class ProductAndOrder {
        private String sku;
        private String fnsku;
        private String asin;
        private String title;
        private BigDecimal yourPrice;
        private String binding;
        private String brand;
        private String productGroup;
        private String productTypeName;
        private String image;
        private Integer salesRank;
        private Integer fbaTotal;
        private Integer fbaInStock;
        private Integer unitsOrdered;
        private Integer unitsOrderedB2B;
        private BigDecimal orderedProductSales;
        private BigDecimal orderedProductSalesB2B;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getFnsku() {
            return fnsku;
        }

        public void setFnsku(String fnsku) {
            this.fnsku = fnsku;
        }

        public String getAsin() {
            return asin;
        }

        public void setAsin(String asin) {
            this.asin = asin;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public BigDecimal getYourPrice() {
            return yourPrice;
        }

        public void setYourPrice(BigDecimal yourPrice) {
            this.yourPrice = yourPrice;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getSalesRank() {
            return salesRank;
        }

        public void setSalesRank(Integer salesRank) {
            this.salesRank = salesRank;
        }

        public Integer getFbaTotal() {
            return fbaTotal;
        }

        public void setFbaTotal(Integer fbaTotal) {
            this.fbaTotal = fbaTotal;
        }

        public Integer getFbaInStock() {
            return fbaInStock;
        }

        public void setFbaInStock(Integer fbaInStock) {
            this.fbaInStock = fbaInStock;
        }

        public Integer getUnitsOrdered() {
            return unitsOrdered;
        }

        public void setUnitsOrdered(Integer unitsOrdered) {
            this.unitsOrdered = unitsOrdered;
        }

        public Integer getUnitsOrderedB2B() {
            return unitsOrderedB2B;
        }

        public void setUnitsOrderedB2B(Integer unitsOrderedB2B) {
            this.unitsOrderedB2B = unitsOrderedB2B;
        }

        public BigDecimal getOrderedProductSales() {
            return orderedProductSales;
        }

        public void setOrderedProductSales(BigDecimal orderedProductSales) {
            this.orderedProductSales = orderedProductSales;
        }

        public BigDecimal getOrderedProductSalesB2B() {
            return orderedProductSalesB2B;
        }

        public void setOrderedProductSalesB2B(BigDecimal orderedProductSalesB2B) {
            this.orderedProductSalesB2B = orderedProductSalesB2B;
        }

        @Override
        public String toString() {
            return "OrderAndProduct{" +
                    "sku='" + sku + '\'' +
                    ", fnsku='" + fnsku + '\'' +
                    ", asin='" + asin + '\'' +
                    ", title='" + title + '\'' +
                    ", yourPrice='" + yourPrice + '\'' +
                    ", binding='" + binding + '\'' +
                    ", brand='" + brand + '\'' +
                    ", productGroup='" + productGroup + '\'' +
                    ", productTypeName='" + productTypeName + '\'' +
                    ", image='" + image + '\'' +
                    ", salesRank='" + salesRank + '\'' +
                    ", fbaTotal=" + fbaTotal +
                    ", fbaInStock=" + fbaInStock +
                    ", unitsOrdered=" + unitsOrdered +
                    ", unitsOrderedB2B=" + unitsOrderedB2B +
                    ", orderedProductSales=" + orderedProductSales +
                    ", orderedProductSalesB2B=" + orderedProductSalesB2B +
                    '}';
        }
    }
}
