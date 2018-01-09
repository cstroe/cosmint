package com.github.cstroe.spendhawk.util;

import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TemplateForwarderTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    private HttpServletRequest mockRequest;
    @Before
    public void setUp() {
        mockRequest = context.mock(HttpServletRequest.class);
        context.checking(new Expectations() {{
            oneOf(mockRequest).getContextPath(); will(returnValue("/contextPath"));
        }});
    }

    @Test
    public void testTemplateForwarder() {
        TemplateForwarder fw = new TemplateForwarder(mockRequest);

        assertThat(fw.url("/somePath"), is(equalTo("/contextPath/somePath")));
        assertThat(fw.url("somePath"), is(equalTo("/contextPath/somePath")));
        assertThat(fw.url("somePath/otherPath"), is(equalTo("/contextPath/somePath/otherPath")));

        // with arguments
        assertThat(fw.url("somePath/otherPath", "arg1", "val1", "arg2", "val2"),
                is(equalTo("/contextPath/somePath/otherPath?arg1=val1&arg2=val2")));

        assertThat(fw.url("somePath/otherPath", "arg1", 1234l, "arg2", 0.5d),
                is(equalTo("/contextPath/somePath/otherPath?arg1=1234&arg2=0.5")));
    }

    @Test
    public void testServletForwarding() throws ClassNotFoundException {
        TemplateForwarder fw = new TemplateForwarder(mockRequest);

        final String canonicalServletName = UserSummaryServlet.class.getCanonicalName();
        final String servletPath = ServletUtil.servletPath(UserSummaryServlet.class);

        assertThat(fw.servlet(canonicalServletName, "arg1", "val1"),
                is(equalTo("/contextPath" + servletPath + "?arg1=val1")));
    }
}
