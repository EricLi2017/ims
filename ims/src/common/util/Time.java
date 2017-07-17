package common.util;


import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Time {
    /**
     * Hours from UTC: UTC-8 (Standard time), UTC-7 (Daylight saving time)
     * <p>
     * Through 2006, the local time (PST, UTC−8) changed to daylight time (PDT,
     * UTC−7) at 02:00 LST (local standard time) to 03:00 LDT (local daylight
     * time) on the first Sunday in April, and returned at 02:00 LDT to 01:00
     * LST on the last Sunday in October.
     * <p>
     * Effective in the U.S. in 2007 as a result of the Energy Policy Act of
     * 2005, the local time changes from PST to PDT at 02:00 LST to 03:00 LDT on
     * the second Sunday in March and the time returns at 02:00 LDT to 01:00 LST
     * on the first Sunday in November. The Canadian provinces and territories
     * that use daylight time each adopted these dates between October 2005 and
     * February 2007. In Mexico, beginning in 2010, the portion of the country
     * in this time zone uses the extended dates, as do some other parts. The
     * vast majority of Mexico, however, still uses the old dates.
     */
    public static final TimeZone PST = TimeZone.getTimeZone("PST");

    /**
     * get time in Timestamp from XMLGregorianCalendar
     *
     * @param time XMLGregorianCalendar
     * @return time Timestamp or null if time is null or not isValid
     */
    public static Timestamp getTimeInPST(XMLGregorianCalendar time) {
        return getTime(PST, time);
    }

    private synchronized static Timestamp getTime(TimeZone timeZone, XMLGregorianCalendar time) {
        if (timeZone == null || time == null || !time.isValid())
            return null;

        System.out.println("XMLGregorianCalendar source time = " + time + "\t timezone offset hours: "
                + time.getTimezone() / 60 + "\t timezone id: " + time.getTimeZone(time.getTimezone()).getID());
        TimeZone.setDefault(timeZone);
        Timestamp timestamp = new Timestamp(time.toGregorianCalendar().getTimeInMillis());
        System.out.println("Timestamp target time = " + timestamp + "\t timezone offset hours: "
                + TimeZone.getDefault().getRawOffset() / 3600000 + "\t timezone id: " + TimeZone.getDefault().getID());

        return timestamp;
    }


    /**
     * get time in XMLGregorianCalendar from Timestamp and time zone
     */
    public static XMLGregorianCalendar getTime(TimeZone timeZone, Timestamp timestamp) {
        if (timeZone == null || timestamp == null)
            return null;

        return (Time.getTime(timeZone, timestamp.getTime()));
    }

    /**
     * get time in XMLGregorianCalendar from Timestamp and time zone
     *
     * @param timeZone the time zone will be stored in XMLGregorianCalendar
     * @param millis   the time in UTC milliseconds from the epoch
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar getTime(TimeZone timeZone, long millis) {
        GregorianCalendar gc = new GregorianCalendar(timeZone);
        gc.setTimeInMillis(millis);
        XMLGregorianCalendar cal = null;
        try {
            cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return cal;
    }


//    public static final XMLGregorianCalendar getTimeInPST(TimeZone timeZone, SimpleDateFormat sdf, String strTime) {
//        if (timeZone == null || sdf == null || strTime == null || "".equals(strTime.trim()))
//            return null;
//
//        XMLGregorianCalendar cal = null;
//        sdf.setTimeZone(timeZone);
//        try {
//            Date date = sdf.parse(strTime);
//            System.out.println(date);
//            cal = Time.getTimeInPST(timeZone, date.getTimeInPST());
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }
//        return cal;
//    }


    /**
     * get time in the specified time zone
     *
     * @param timeZone  the time zone will be stored in XMLGregorianCalendar
     * @param year      the value used to set the YEAR calendar
     * @param month     the value used to set the MONTH calendar field (0-11)
     * @param date      the value used to set the DAY_OF_MONTH calendar field
     * @param hourOfDay the value used to set the HOUR_OF_DAY calendar field
     * @param minute    the value used to set the MINUTE calendar field
     * @param second    the value used to set the SECOND calendar field
     * @return XMLGregorianCalendar
     */
    private static XMLGregorianCalendar getTime(TimeZone timeZone, int year, int month, int date, int hourOfDay,
                                                int minute, int second) {
        GregorianCalendar gc = new GregorianCalendar(timeZone);
        gc.set(year, month, date, hourOfDay, minute, second);
        XMLGregorianCalendar cal = null;
        try {
            cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            cal.setMillisecond(0);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * get the time of now in PST time zone: UTC-08:00
     */
    public static XMLGregorianCalendar getNowTimeInPST() {
        return getNowTime(PST);
    }

    /**
     * get the time of now in the specified time zone
     *
     * @param timeZone the time zone will be stored in XMLGregorianCalendar
     */
    public static XMLGregorianCalendar getNowTime(TimeZone timeZone) {
        return getTime(timeZone, System.currentTimeMillis());
    }

    public static void main(String... strings) {
        // for (String id : java.util.TimeZone.getAvailableIDs()) {
        // TimeZone timeZone = TimeZone.getTimeZone(id);
        // System.out.print(timeZone.getID());
        // System.out.print("\t|");
        // System.out.print(timeZone.getDisplayName());
        // System.out.print("\t|");
        // System.out.println(Time.getTimeInPST(timeZone, 2016, 6, 22, 0, 0, 0));
        // }

        // System.out.println(TimeZone.getDefault().getID());
        // for (int month = 0; month < 12; month++) {
        // XMLGregorianCalendar xgc=Time.getTimeInPST(Time.PST, 2017, month, 22, 0,
        // 0, 0);
        // System.out.print(xgc);
        // System.out.print("\t|");
        // System.out.print(xgc.getTimezone()/60);
        // System.out.print("\t|");
        // System.out.println(xgc.toGregorianCalendar().getTimeZone().getID());
        // }
        // System.out.println(TimeZone.getDefault().getID());

        // XMLGregorianCalendar xgc = Time.getNowTime(PST);
        // System.out.print(xgc);
        // System.out.print("\t|");
        // System.out.print(xgc.toGregorianCalendar().getTimeZone().getID());
        // System.out.print("\t|");
        // System.out.println(xgc.getMillisecond());
        //// System.out.println(xgc.getTimezone() / 60);
        // System.out.println("----------------------------------------");
        // GregorianCalendar gc = xgc.toGregorianCalendar();
        // System.out.print(gc.getTimeInPST());
        // System.out.print("\t|");
        // System.out.println(gc.getTimeZone().getID());
        // TimeZone.setDefault(PST);
        // for (int i = 0; i < 36; i++) {
        // gc.add(Calendar.DAY_OF_MONTH, 1);
        // gc.setTimeZone(Time.PST);
        // System.out.print(gc.getTimeInPST());
        // System.out.print("\t|");
        // System.out.print(gc.getTimeZone().getID());
        // System.out.print("\t|");
        // System.out.println(gc.getTimeInMillis());
        // }
        // System.out.print(xgc);
        // System.out.print("\t|");
        // System.out.print(xgc.toGregorianCalendar().getTimeZone().getID());
        // System.out.print("\t|");
        // System.out.println(xgc.getMillisecond());
        //// System.out.println(xgc.getTimezone() / 60);
        // System.out.println("----------------------------------------");

//        XMLGregorianCalendar xgc = Time.getNowTime(PST);
//        System.out.println(xgc);
//
//        Timestamp timestamp = getTimeInPST(xgc);
//        System.out.println(timestamp.getTimeInPST());
//        System.out.println(timestamp);
//        TimeZone.setDefault(PST);
//        System.out.println(timestamp.getTimeInPST());
//        System.out.println(timestamp);


        XMLGregorianCalendar createdAfter = null;
        try {
            createdAfter = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        if (createdAfter == null) return;
//        createdAfter.setTimezone(-8 * 60);// PST time zone UTC-8
        createdAfter.setTimezone(0);// zero time zone UTC
        createdAfter.setYear(2017);
        createdAfter.setMonth(2);
        createdAfter.setDay(3);
        createdAfter.setTime(0, 0, 0);
        createdAfter.setMillisecond(0);
        System.out.println(createdAfter);
        System.out.println(createdAfter.getTimezone() / 60);

    }
}
