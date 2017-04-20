package com.github.cstroe.spendhawk.util;

import com.github.cstroe.spendhawk.web.WelcomeServlet;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ServletUtilTest {

    @Test
    public void welcomeServlet() {
        String path = ServletUtil.servletPath(WelcomeServlet.class);
        assertThat(path, is(equalTo("/welcome")));
    }

    @Test
    public void welcomeServletWithParams() {
        String path = ServletUtil.servletPath(
                WelcomeServlet.class, "arg1", "val1", "arg2", "val2");

        assertThat(path, is(equalTo("/welcome?arg1=val1&arg2=val2")));
    }
}
