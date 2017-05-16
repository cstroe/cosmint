package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.testutil.web.SpyHttpServletRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class AccountServletTest {

    private static DateTimeFormatter dateFormat;

    @BeforeClass
    public static void oneTime() {
        dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    @Test
    public void testNavigationDates() {
        HttpServletRequest request = new SpyHttpServletRequest();

        LocalDate june5 = dateFormat.parse("06-05-1982", LocalDate::from);

//        AccountServlet.setNavigationDates(request, june5);

        assertEquals("05-01-1982", request.getAttribute("previousMonth"));
        assertEquals("07-01-1982", request.getAttribute("nextMonth"));
    }

    @Test
    public void testNavigationDatesBeginningOfYear() {
        HttpServletRequest request = new SpyHttpServletRequest();

        LocalDate sometimeInJanuary = dateFormat.parse("01-24-2001", LocalDate::from);

//        AccountServlet.setNavigationDates(request, sometimeInJanuary);

        assertEquals("12-01-2000", request.getAttribute("previousMonth"));
        assertEquals("02-01-2001", request.getAttribute("nextMonth"));
    }

    @Test
    public void testNavidationDatesEndOfYear() {
        HttpServletRequest request = new SpyHttpServletRequest();

        LocalDate sometimeInDecember = dateFormat.parse("12-15-2010", LocalDate::from);

//        AccountServlet.setNavigationDates(request, sometimeInDecember);

        assertEquals("11-01-2010", request.getAttribute("previousMonth"));
        assertEquals("01-01-2011", request.getAttribute("nextMonth"));
    }

    @Test(expected = NullPointerException.class)
    public void testNavidationDatesWithNullInput() {
        HttpServletRequest request = new SpyHttpServletRequest();
//        AccountServlet.setNavigationDates(request, null);
    }
}
