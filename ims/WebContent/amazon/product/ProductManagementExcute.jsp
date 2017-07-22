<%@ page import="amazon.db.query.QueryProductAndOrder"%>
<%@ page import="common.util.Time"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.util.List"%>
<%
	//query params ()
	boolean isQuery = (request.getQueryString() != null);
	String sku = common.util.Filter.nullFilter(request.getParameter("sku"));
	String fnsku = common.util.Filter.nullFilter(request.getParameter("fnsku"));
	String asin = common.util.Filter.nullFilter(request.getParameter("asin"));
	String name = common.util.Filter.nullFilter(request.getParameter("name"));
	String dateAfter = common.util.Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = common.util.Filter.nullFilter(request.getParameter("dateBefore"));
	//order by params
	String orderBy = common.util.Filter.nullFilter(request.getParameter("orderBy"));
	String ascOrDesc = common.util.Filter.nullFilter(request.getParameter("ascOrDesc"));

	System.out.println("sku=" + sku);
	System.out.println("fnsku=" + fnsku);
	System.out.println("asin=" + asin);
	System.out.println("name=" + name);
	System.out.println("dateAfter=" + dateAfter);
	System.out.println("dateBefore=" + dateBefore);

	//set time zone to PST
	//
	TimeZone PST = Time.PST;
	TimeZone.setDefault(PST);
	TimeZone currentTZone = TimeZone.getDefault();
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	Timestamp now = new Timestamp(System.currentTimeMillis());

	//params showed in page
	List<QueryProductAndOrder.ProductAndOrder> productAndOrders = null;
	if (isQuery) {
		XMLGregorianCalendar createdAfter = null;
		XMLGregorianCalendar createdBefore = null;

		boolean isDateAfterValid = true;
		boolean isDateBeforeValid = true;
		if (dateAfter != null && !"".equals(dateAfter)) {
			isDateAfterValid = false;
			try {
				createdAfter = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateAfter).getTime()));
				isDateAfterValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (dateBefore != null && !"".equals(dateBefore)) {
			isDateBeforeValid = false;
			try {
				createdBefore = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateBefore).getTime()));
				isDateBeforeValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!isDateAfterValid || !isDateBeforeValid) {
			out.print("incorrect date, correct date format is " + pattern);
			return;
		}

		productAndOrders = new QueryProductAndOrder().querySkuSalesSum(Time.getTime(createdAfter),
				Time.getTime(createdBefore), sku, asin, fnsku, name, orderBy, ascOrDesc);
	}
%>