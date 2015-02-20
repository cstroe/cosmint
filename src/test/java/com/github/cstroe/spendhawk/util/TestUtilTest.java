package com.github.cstroe.spendhawk.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.github.cstroe.spendhawk.util.TestUtil.getLink;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
}
