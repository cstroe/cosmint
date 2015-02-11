package com.github.cstroe.spendhawk.bean;

import javax.enterprise.inject.Default;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Does stuff with dates, duh!
 */
@Default
public class DateBean {

    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public String format(Date date) {
        return dateFormat.format(date);
    }

    public Date parse(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch(ParseException ex) {
            // fail early fail often, fail at runtime?
            throw new RuntimeException(ex);
        }
    }
}
