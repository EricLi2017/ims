/**
 * 
 */
package amazon.mws.product;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Eclipse on Aug 3, 2017 at 10:01:18 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
// @XmlType(name = "ItemAttributes", propOrder = { "brand" })
@XmlRootElement(name = "ItemAttributes", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
public class ItemAttributes {
	@XmlElement(name = "Binding", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String binding;
	@XmlElement(name = "Brand", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String brand;
	@XmlElement(name = "Feature", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private List<String> features;
	@XmlElement(name = "Title", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String title;
	@XmlElement(name = "ProductGroup", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String productGroup;
	@XmlElement(name = "ProductTypeName", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String productTypeName;
	@XmlElement(name = "Publisher", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private String publisher;
	@XmlElement(name = "SmallImage", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	private SmallImage smallImage;

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

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public SmallImage getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(SmallImage smallImage) {
		this.smallImage = smallImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	// @XmlType(name = "ItemAttributes", propOrder = { "brand" })
	@XmlRootElement(name = "SmallImage", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
	public static class SmallImage {
		@XmlElement(name = "URL", namespace = "http://mws.amazonservices.com/schema/Products/2011-10-01/default.xsd")
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}
}