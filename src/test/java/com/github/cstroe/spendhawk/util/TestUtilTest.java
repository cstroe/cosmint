package com.github.cstroe.spendhawk.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static com.github.cstroe.spendhawk.util.TestUtil.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class TestUtilTest {

    private Document doc;

    @Before
    public void setUp() throws IOException {
        doc = Jsoup.parse(
            ClassLoader.getSystemResourceAsStream("files/sample.html"),
            "UTF-8", "http://localhost/"
        );
    }

    @Test
    public void getLinkNoArgs() throws IOException {
        assertThat(getLink(doc, "/myContext/simplePath"), is(equalTo("/myContext/simplePath")));
    }

    @Test
    public void getLinkWithOneArg() {
        assertThat(getLink(doc, "/myContext/funstuff/action1", "argument1", "val1"),
            is(equalTo("/myContext/funstuff/action1?argument1=val1")));
    }

    @Test
    public void getLinkWithMultipleArgs() {
        assertThat("Should match an existing link.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "valB",
                "argumentC", "valD",
                "argumentF", ""),
            is("/myContext/funstuff/action2?argumentA=valB&argumentC=valD&argumentE&argumentF"));

        TestUtil.saveFileOnError = false;

        assertNull("Should not match a link that is similar, but missing one empty value argument.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "valB",
                "argumentC", "valD"));

        assertNull("Should not match a link that is similar, but missing one non-empty value argument.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "valB",
                "argumentF", ""));

        assertNull("Should not match a link that is similar, but contains an extra empty value argument.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "valB",
                "argumentC", "valD",
                "argumentF", "",
                "argumentG", ""));

        assertNull("Should not match a link that is similar, but contains an extra non-empty value argument.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "valB",
                "argumentC", "valD",
                "argumentF", "",
                "argumentG", "foobar"));

        assertNull("Should not match a link that just contains the argument names.",
            getLink(doc, "/myContext/funstuff/action2",
                "argumentE", "",
                "argumentA", "",
                "argumentC", "",
                "argumentF", ""));

        assertNull("Should not match a link with correct arguments but incorrect path.",
            getLink(doc, "/myContext/funstuff/wrongpath",
                "argumentE", "",
                "argumentA", "valB",
                "argumentC", "valD",
                "argumentF", ""));
    }

    @Test
    public void testCreateArgumentMap() {
        Map<String, String> argumentMap;

        argumentMap = createArgumentMap("");
        assertThat(argumentMap.size(), is(0));

        argumentMap = createArgumentMap("a=3");
        assertThat(argumentMap.size(), is(1));
        assertThat(argumentMap.get("a"), is(equalTo("3")));

        argumentMap = createArgumentMap("b=4&c=five");
        assertThat(argumentMap.size(), is(2));
        assertThat(argumentMap.get("b"), is(equalTo("4")));
        assertThat(argumentMap.get("c"), is(equalTo("five")));

        argumentMap = createArgumentMap("a&b=something");
        assertThat(argumentMap.size(), is(2));
        assertThat(argumentMap.get("a"), is(equalTo("")));
        assertThat(argumentMap.get("b"), is(equalTo("something")));

        argumentMap = createArgumentMap("a&b&c");
        assertThat(argumentMap.size(), is(3));
        assertThat(argumentMap.get("a"), is(equalTo("")));
        assertThat(argumentMap.get("b"), is(equalTo("")));
        assertThat(argumentMap.get("c"), is(equalTo("")));

        argumentMap = createArgumentMap("a&b=778&c=hjhjhjhj");
        assertThat(argumentMap.size(), is(3));
        assertThat(argumentMap.get("a"), is(equalTo("")));
        assertThat(argumentMap.get("b"), is(equalTo("778")));
        assertThat(argumentMap.get("c"), is(equalTo("hjhjhjhj")));
    }

    @Test
    public void testGetArguments() {
        assertFalse(getArguments("/someContext/url1").isPresent());
        assertFalse(getArguments("/someContext/url1?").isPresent());
        assertThat(getArguments("/someContext/url1?a").get(), is(equalTo("a")));
        assertThat(getArguments("/someContext/url1?a=1&b=2").get(), is(equalTo("a=1&b=2")));
    }

    @Test
    public void testGetPath() {
        assertThat(getPath("/someContext/url1"), is(equalTo("/someContext/url1")));
        assertThat(getPath("/someContext/url1?"), is(equalTo("/someContext/url1")));
        assertThat(getPath("/someContext/url1?a"), is(equalTo("/someContext/url1")));
        assertThat(getPath("/someContext/url1?a=1&b=2"), is(equalTo("/someContext/url1")));
    }
}
