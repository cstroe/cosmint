package com.github.cstroe.spendhawk.bean;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JanitorBeanTest {

    private JanitorBean janitor;

    @Before
    public void setUp() {
        janitor = new JanitorBean();
    }

    @Test
    public void testIsBlank() {
        assertFalse(janitor.isBlank("   not a blank string   "));
        assertTrue(janitor.isBlank("     "));
        assertTrue(janitor.isBlank("\t\t\t\t"));
        assertTrue(janitor.isBlank("\t \t \t \t"));
    }

    @Test
    public void testSanitize() {
        {
            String string1 = "Normal string here.";
            assertThat(string1, is(equalTo(janitor.sanitize(string1))));
        }{
            String string2 = "</a><a>Html String here</a>";
            assertThat(janitor.sanitize(string2), is(not(equalTo(string2))));
            assertFalse(janitor.sanitize(string2).contains("<"));
            assertFalse(janitor.sanitize(string2).contains(">"));
        }
        {
            String string3 = "' and 1=1";
            assertThat(janitor.sanitize(string3), is(not(equalTo(string3))));
            assertFalse(janitor.sanitize(string3).contains("'"));
        }
    }
}
