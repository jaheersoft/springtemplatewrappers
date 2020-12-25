package com.wrapper.templates.utilities;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public final class Dates {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dates.class);
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DD_MMM_YYYY = "dd-MMM-yyyy";
    

    //privatized for non-instantiability
    private Dates(){}

    public static Date date(int year, int month, int day){
        return localDate(year, month, day).toDate();
    }

    public static Date date(LocalDate localDate){
        return localDate==null?null:localDate.toDate();
    }

    public static Date date(String date, String format){
    	if (date == null || format == null || date.trim().length() == 0 || format.trim().length() == 0) {
			return null;
		}
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static LocalDate localDate(int year, int month, int day){
        return new LocalDate(year, month, day);
    }

    public static LocalDate localDate(Date date){
        return date==null?null:LocalDate.fromDateFields(date);
    }

    public static LocalDate localDate(XMLGregorianCalendar gregorianCalendar){
        return gregorianCalendar == null
                ? null
                : localDate(gregorianCalendar.toGregorianCalendar().getTime());
    }

    public static LocalDate localDate(String date, String format){
        if (date == null || format == null || date.trim().length() == 0 || format.trim().length() == 0) {
            return null;
        }
        return LocalDate.parse(date, DateTimeFormat.forPattern(format));
    }

    //Due to a bug, PMD throws "Avoid using java.lang.ThreadGroup; it is not thread safe". Below annotation suppresses it.
    @SuppressWarnings("PMD.AvoidThreadGroup")
    public static java.sql.Date sqlDate(Date date){
        return date==null?null:new java.sql.Date(date.getTime());
    }

    public static java.sql.Date sqlDate(LocalDate date){
        return sqlDate(date(date));
    }

    public static java.sql.Date sqlDate(String date){
    	java.sql.Date retDate = null;
    	if (date == null || date.trim().length() == 0) {
			return null;
		}
        try {
        	 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
             java.util.Date datetmp = sdf.parse(date);
            
             retDate = new  java.sql.Date(datetmp.getTime());
        } catch (ParseException e) {
            return null;
        }
        return retDate;
    }
    
    public static XMLGregorianCalendar gregorianCalendar(Date date){
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        XMLGregorianCalendar xmlDate = null;
        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            LOGGER.warn("Error converting java.util.Date to XMLGregorianCalendar: ", e);
        }
        return xmlDate;
    }

    public static XMLGregorianCalendar gregorianCalendar(LocalDate localDate){
        return gregorianCalendar(localDate.toDate());
    }

    public static XMLGregorianCalendar gregorianCalendar(int year, int month, int day){
        return gregorianCalendar(new LocalDate(year, month, day));
    }

    public static String toString(Date date, String format){
        return LocalDate.fromDateFields(date).toString(format);
    }

    public static String toString(LocalDate date, String format){
        return date.toString(format);
    }

    public static String yyyyMMdd(Date date){
        return LocalDate.fromDateFields(date).toString(YYYYMMDD);
    }

    public static String yyyyMMdd(LocalDate date){
        return date.toString(YYYYMMDD);
    }

    public static String toFormatMMddyyyy(Date date){
        return LocalDate.fromDateFields(date).toString(MM_DD_YYYY);
    }

    public static String toFormatMMddyyyy(LocalDate date){
        return date.toString(MM_DD_YYYY);
    }

}
