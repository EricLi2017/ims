/**
 * 
 */
package common.util;

import java.sql.Timestamp;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 22, 2017 Time: 6:57:11 PM
 */
public class TimeTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link common.util.Time#getTime(javax.xml.datatype.XMLGregorianCalendar)}.
	 */
	@Test
	public void testGetTime1() {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		if (xmlGregorianCalendar == null)
			return;

		// createdAfter.setTimezone(-8 * 60);// PST time zone UTC-8
		xmlGregorianCalendar.setTimezone(0);// zero time zone UTC
		xmlGregorianCalendar.setYear(2017);
		xmlGregorianCalendar.setMonth(2);
		xmlGregorianCalendar.setDay(3);
		xmlGregorianCalendar.setTime(0, 0, 0);
		xmlGregorianCalendar.setMillisecond(0);

		Timestamp timestamp;
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());

		System.out.println("xmlGregorianCalendar=" + xmlGregorianCalendar);
		System.out.println("xmlGregorianCalendar.getTimezone()/60=" + xmlGregorianCalendar.getTimezone() / 60);
		System.out.println("Convert time from XmlGregorianCalendar to Timestamp");
		timestamp = Time.getTime(xmlGregorianCalendar);
		System.out.println("timestamp.getTime()=" + timestamp.getTime());
		System.out.println("timestamp=" + timestamp);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());

		int offSet = TimeZone.getTimeZone("PST").getRawOffset() / 60000;
		System.out.println("Now change the time zone of xmlGregorianCalendar by offSet=" + offSet);
		xmlGregorianCalendar.setTimezone(offSet);// change the time zone

		System.out.println("xmlGregorianCalendar=" + xmlGregorianCalendar);
		System.out.println("xmlGregorianCalendar.getTimezone()/60=" + xmlGregorianCalendar.getTimezone() / 60);
		System.out.println("Convert time from XmlGregorianCalendar to Timestamp");
		timestamp = Time.getTime(xmlGregorianCalendar);
		System.out.println("timestamp.getTime()=" + timestamp.getTime());
		System.out.println("timestamp=" + timestamp);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());

	}

	/**
	 * Test method for {@link common.util.Time#getTime(java.sql.Timestamp)} and
	 * {@link common.util.Time#getTime(java.util.TimeZone , java.sql.Timestamp)}.
	 */
	@Test
	public void testGetTime2() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("timestamp.getTime()=" + timestamp.getTime());
		System.out.println("timestamp=" + timestamp);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		System.out.println("");

		XMLGregorianCalendar xmlGregorianCalendar = Time.getTime(timestamp);
		System.out.println("Now get time int default time zone from XmlGregorianCalendar to Timestamp");
		System.out.println("");

		System.out.println("xmlGregorianCalendar=" + xmlGregorianCalendar);
		System.out.println("xmlGregorianCalendar.getTimezone()/60=" + xmlGregorianCalendar.getTimezone() / 60);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		timestamp = Time.getTime(xmlGregorianCalendar);
		System.out.println("timestamp.getTime()=" + timestamp.getTime());
		System.out.println("timestamp=" + timestamp);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		System.out.println("");

		XMLGregorianCalendar xmlGregorianCalendar2 = Time.getTime(TimeZone.getTimeZone("PST"), timestamp);
		System.out.println("Now get time int PST time zone from XmlGregorianCalendar to Timestamp");
		System.out.println("");

		System.out.println("xmlGregorianCalendar2=" + xmlGregorianCalendar2);
		System.out.println("xmlGregorianCalendar2.getTimezone()/60=" + xmlGregorianCalendar2.getTimezone() / 60);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
		timestamp = Time.getTime(xmlGregorianCalendar);
		System.out.println("timestamp.getTime()=" + timestamp.getTime());
		System.out.println("timestamp=" + timestamp);
		System.out.println("TimeZone.getDefault()=" + TimeZone.getDefault());
	}

}
