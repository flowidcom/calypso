package com.flowid.refd.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Class used in the JAXB unmarshalling to transform empty strings in nulls and trim the white
 * spaces at the end. It also converts dates to Joda Date-Time.
 */
public class DataTypeAdapter {
    private static final String XML_DATE_FORMAT = "YYYY-MM-dd";
    private static final String XML_DATE_BEFORE_1900_FORMAT = "YYYY-MM-dd'T'HH:mm:ss";

    public static final DateTimeZone DEFAULT_TIME_ZONE = DateTimeZone.forID("America/New York");

    private static final DateTimeFormatter XML_DATE_FORMATER = DateTimeFormat.forPattern(XML_DATE_FORMAT);
    private static final DateTimeFormatter XML_DATE_BEFORE_1900_FORMATER =
            DateTimeFormat.forPattern(XML_DATE_BEFORE_1900_FORMAT);
    private static final DateTimeFormatter XML_PRINT_DATE_TIME_FORMATER = ISODateTimeFormat.dateTimeNoMillis();
    private static final DateTimeFormatter XML_PARSE_DATE_TIME_FORMATER = ISODateTimeFormat.dateTimeParser();

    public static String trimToNull(String str) {
        if (str == null)
        {
            return null;
        }
        str = str.trim();
        if (str.length() == 0)
        {
            return null;
        }
        return str;
    }

    public static String parseString(String str) {
        return trimToNull(str);
    }

    public static String printString(String string) {
        return string;
    }

    public static DateTime parseDate(String str) {
        str = trimToNull(str);

        // ignore bad data coming from systems
        if (str != null && str.length() > 0)
        {
            // Drop the timezone from the date. Assumes that the dates do no have timezones.
            if (str.length() > XML_DATE_FORMAT.length())
            {
                str = str.substring(0, XML_DATE_FORMAT.length());
            }
            return XML_DATE_FORMATER.parseDateTime(str);
        }

        return null;
    }

    public static String printDate(DateTime dateTime) {
        if (dateTime == null)
        {
            return null;
        }
        return XML_DATE_FORMATER.print(dateTime);
    }

    public static DateTime parseDateTime(String str) {
        str = trimToNull(str);

        if (str != null)
        {
            return XML_PARSE_DATE_TIME_FORMATER.parseDateTime(str).withZone(DEFAULT_TIME_ZONE);
        }

        return null;
    }

    public static String printDateTime(DateTime dateTime) {
        if (dateTime == null)
        {
            return null;
        }
        /*
         * This is to avoid JodaTime producing an invalid timezone for dates before 1900.
         * http://sourceforge.net/p/joda-time/discussion/337835/thread/c67a8632
         */
        if (dateTime.getYear() < 1900)
        {
            return XML_DATE_BEFORE_1900_FORMATER.print(dateTime);
        }
        return XML_PRINT_DATE_TIME_FORMATER.print(dateTime);
    }
}
