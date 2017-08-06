package common.util;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Time {
	private static final Log log = LogFactory.getLog(Time.class);
	/**
	 * Hours from UTC: UTC-8 (Standard time), UTC-7 (Daylight saving time)
	 * <p>
	 * Through 2006, the local time (PST, UTC−8) changed to daylight time (PDT,
	 * UTC−7) at 02:00 LST (local standard time) to 03:00 LDT (local daylight time)
	 * on the first Sunday in April, and returned at 02:00 LDT to 01:00 LST on the
	 * last Sunday in October.
	 * <p>
	 * Effective in the U.S. in 2007 as a result of the Energy Policy Act of 2005,
	 * the local time changes from PST to PDT at 02:00 LST to 03:00 LDT on the
	 * second Sunday in March and the time returns at 02:00 LDT to 01:00 LST on the
	 * first Sunday in November. The Canadian provinces and territories that use
	 * daylight time each adopted these dates between October 2005 and February
	 * 2007. In Mexico, beginning in 2010, the portion of the country in this time
	 * zone uses the extended dates, as do some other parts. The vast majority of
	 * Mexico, however, still uses the old dates.
	 */
	public static final TimeZone PST = TimeZone.getTimeZone("PST");

	/**
	 * Get a time in Timestamp type from the time in XMLGregorianCalendar type
	 *
	 * @param time
	 *            XMLGregorianCalendar
	 * @return time Timestamp or null if time is null or not isValid
	 */
	public static Timestamp getTime(XMLGregorianCalendar time) {
		log.debug("[input] XMLGregorianCalendar time:" + time);
		log.debug("Timezone of XMLGregorianCalendar time:" + time.getTimezone() / 60);
		if (time == null || !time.isValid())
			return null;
		Timestamp timestamp = new Timestamp(time.toGregorianCalendar().getTimeInMillis());
		log.debug("[output] Timestamp:" + timestamp);
		log.debug("Timezone default:" + TimeZone.getDefault());
		return timestamp;
	}

	/**
	 * Get a time in XMLGregorianCalendar type with specified time zone from the
	 * time in Timestamp type
	 * 
	 * @param timeZone
	 * @param timestamp
	 * @return
	 */
	public static XMLGregorianCalendar getTime(TimeZone timeZone, Timestamp timestamp) {
		if (timeZone == null || timestamp == null)
			return null;

		GregorianCalendar gc = new GregorianCalendar(timeZone);
		gc.setTimeInMillis(timestamp.getTime());
		XMLGregorianCalendar cal = null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return cal;
	}

	/**
	 * Get a time in XMLGregorianCalendar type with default time zone from the time
	 * in Timestamp type
	 * 
	 * @param timestamp
	 * @return
	 */
	public static XMLGregorianCalendar getTime(Timestamp timestamp) {
		if (timestamp == null)
			return null;

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(timestamp.getTime());
		XMLGregorianCalendar cal = null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return cal;
	}

}
