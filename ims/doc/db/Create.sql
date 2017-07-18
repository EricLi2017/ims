-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.1.73 - Source distribution
-- Server OS:                    redhat-linux-gnu
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for ims
CREATE DATABASE IF NOT EXISTS `ims` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ims`;

-- Dumping structure for table ims.amazon_product
CREATE TABLE IF NOT EXISTS `amazon_product` (
  `sku` char(50) NOT NULL,
  `fnsku` char(10) DEFAULT NULL,
  `asin` char(10) NOT NULL COMMENT 'SKU : ASIN = (1..*) : 1',
  `your_price` decimal(10,2) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `binding` varchar(50) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `feature1` varchar(500) DEFAULT NULL,
  `feature2` varchar(500) DEFAULT NULL,
  `feature3` varchar(500) DEFAULT NULL,
  `feature4` varchar(500) DEFAULT NULL,
  `feature5` varchar(500) DEFAULT NULL,
  `product_group` varchar(50) DEFAULT NULL,
  `product_type_name` varchar(50) DEFAULT NULL,
  `small_image_url` varchar(255) DEFAULT NULL,
  `sales_rank` int(11) DEFAULT NULL,
  `total_supply_quantity` int(11) DEFAULT NULL,
  `in_stock_supply_quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`sku`),
  KEY `asin` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims.internal_product
CREATE TABLE IF NOT EXISTS `internal_product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` char(10) NOT NULL,
  `asin` char(10) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `asin` (`asin`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `amazon_order_id` char(20) NOT NULL,
  `seller_order_id` char(20) NOT NULL,
  `purchase_date` timestamp NULL DEFAULT NULL,
  `last_update_date` timestamp NULL DEFAULT NULL,
  `sales_channel` varchar(50) DEFAULT NULL,
  `fulfillment_channel` varchar(50) DEFAULT NULL,
  `is_business_order` char(5) DEFAULT NULL,
  `is_premium_order` char(5) DEFAULT NULL,
  `is_prime` char(5) DEFAULT NULL,
  `buyer_name` varchar(50) DEFAULT NULL,
  `buyer_email` varchar(50) DEFAULT NULL,
  `order_status` char(10) DEFAULT NULL,
  `order_total_currency` char(3) DEFAULT NULL,
  `order_total_amount` decimal(10,2) DEFAULT NULL,
  `ship_service_category` varchar(50) DEFAULT NULL,
  `ship_service_level` varchar(50) DEFAULT NULL,
  `number_items_shipped` int(11) DEFAULT NULL,
  `number_items_unshipped` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`),
  KEY `purchase_date` (`purchase_date`),
  KEY `last_update_date` (`last_update_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='amazon order';

-- Data exporting was unselected.
-- Dumping structure for table ims.order_items
CREATE TABLE IF NOT EXISTS `order_items` (
  `amazon_order_id` char(20) NOT NULL,
  `sku` char(50) NOT NULL,
  `asin` char(10) NOT NULL,
  `price_currency` char(3) DEFAULT NULL,
  `price_amount` decimal(10,2) DEFAULT NULL,
  `discount_currency` char(3) DEFAULT NULL,
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `tax_currency` char(3) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `order_item_id` varchar(50) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `quantity_ordered` int(11) DEFAULT NULL,
  `quantity_shipped` int(11) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`,`sku`),
  UNIQUE KEY `unique_id_asin` (`amazon_order_id`,`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='items of amazon order';

-- Data exporting was unselected.
-- Dumping structure for table ims.order_shipping_address
CREATE TABLE IF NOT EXISTS `order_shipping_address` (
  `amazon_order_id` char(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address_line1` varchar(50) DEFAULT NULL,
  `address_line2` varchar(50) DEFAULT NULL,
  `address_line3` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `state_or_regin` varchar(50) DEFAULT NULL,
  `postal_code` varchar(50) DEFAULT NULL,
  `country_code` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`amazon_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='shipping addres of amazon order';

-- Data exporting was unselected.
-- Dumping structure for table ims.product_supply
CREATE TABLE IF NOT EXISTS `product_supply` (
  `supply_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `supplier_name` varchar(200) DEFAULT NULL,
  `supplier_description` varchar(500) DEFAULT NULL,
  `supply_type` char(50) DEFAULT NULL,
  `supply_url` varchar(200) DEFAULT NULL,
  `shipped_from` char(50) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT 'the unit price for reference',
  `price_description` varchar(500) DEFAULT NULL,
  `price_time` timestamp NULL DEFAULT NULL,
  `status` char(10) NOT NULL,
  UNIQUE KEY `supply_id` (`supply_id`),
  KEY `product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims.sales_record
CREATE TABLE IF NOT EXISTS `sales_record` (
  `sku` char(50) NOT NULL,
  `asin` char(10) NOT NULL,
  `date` date NOT NULL,
  `units_ordered` int(11) DEFAULT NULL,
  `units_ordered_b2b` int(11) DEFAULT NULL,
  `sales` decimal(10,2) DEFAULT NULL,
  `sales_b2b` decimal(10,2) DEFAULT NULL,
  UNIQUE KEY `sku` (`sku`,`date`),
  UNIQUE KEY `asin` (`asin`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sales record according to per sku/asin per day';

-- Data exporting was unselected.
-- Dumping structure for table ims.supply_transaction
CREATE TABLE IF NOT EXISTS `supply_transaction` (
  `supply_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL COMMENT 'for same batch: sum(quantity*unit_price)=product_price',
  `price_description` varchar(500) DEFAULT NULL,
  `status` char(10) NOT NULL,
  `batch_no` int(11) NOT NULL,
  `product_price` decimal(10,2) NOT NULL,
  `shipped_fee` decimal(10,2) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `operator` char(50) NOT NULL,
  `transaction_description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`batch_no`,`supply_id`),
  KEY `supply_id` (`supply_id`),
  KEY `batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims.test_sql
CREATE TABLE IF NOT EXISTS `test_sql` (
  `ts` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(50) NOT NULL,
  `password` char(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`name`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for table ims._business_report_
CREATE TABLE IF NOT EXISTS `_business_report_` (
  `date_from` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_to` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `parent_asin` char(50) NOT NULL,
  `child_asin` char(50) NOT NULL,
  `title` char(100) DEFAULT NULL,
  `sessions` int(11) DEFAULT NULL,
  `session_percentage` decimal(10,2) DEFAULT NULL,
  `page_views` int(11) DEFAULT NULL,
  `page_views_percentage` decimal(10,2) DEFAULT NULL,
  `buy_box_percentage` decimal(10,2) DEFAULT NULL,
  `units_ordered` int(11) DEFAULT NULL,
  `units_ordered_b2b` int(11) DEFAULT NULL,
  `unit_session_percentage` decimal(10,2) DEFAULT NULL,
  `unit_session_percentage_b2b` decimal(10,2) DEFAULT NULL,
  `ordered_product_sales` decimal(10,2) DEFAULT NULL,
  `ordered_product_sales_b2b` decimal(10,2) DEFAULT NULL,
  `total_order_items` int(11) DEFAULT NULL,
  `total_order_items_b2b` int(11) DEFAULT NULL,
  UNIQUE KEY `date` (`date_from`,`date_to`),
  KEY `parent_asin` (`parent_asin`),
  KEY `child_asin` (`child_asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
