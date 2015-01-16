package com.github.cstroe.spendhawk.util;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TemplateForwarderTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();


    @Test
    public void testTemplateForwarder() {
        HttpServletRequest request = context.mock(HttpServletRequest.class);
        context.checking(new Expectations() {{
            oneOf(request).getContextPath(); will(returnValue("/contextPath"));
        }});

        TemplateForwarder fw = new TemplateForwarder(request);

        assertThat(fw.url("/somePath"), is(equalTo("/contextPath/somePath")));
        assertThat(fw.url("somePath"), is(equalTo("/contextPath/somePath")));
        assertThat(fw.url("somePath/otherPath"), is(equalTo("/contextPath/somePath/otherPath")));

        // with arguments
        assertThat(fw.url("somePath/otherPath", "arg1", "val1", "arg2", "val2"),
                is(equalTo("/contextPath/somePath/otherPath?arg1=val1&arg2=val2")));

        assertThat(fw.url("somePath/otherPath", "arg1", 1234l, "arg2", 0.5d),
                is(equalTo("/contextPath/somePath/otherPath?arg1=1234&arg2=0.5")));

    }
}
