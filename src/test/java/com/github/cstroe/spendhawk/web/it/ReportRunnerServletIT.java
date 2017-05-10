package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.report.ReportRunnerServlet;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import com.mashape.unirest.http.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ReportRunnerServletIT extends BaseClientIT {

    @Test
    public void t0100_link_back_to_user_summary_page_should_be_correct() throws Exception {
        // connect to main report runner page
        response = connect(ReportRunnerServlet.class, "user.id", "3");
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());

        Element reportOption = doc.getElementsByTag("option").stream()
            .filter(option -> option.text().equals("Category Hierarchy Report"))
            .findFirst().orElseThrow(() -> new AssertionError("Could not find the 'Category Hierachy Report' option."));
        assertThat(reportOption.attr("value"), is(equalTo("Category Hierarchy Report")));

        Element submitButton = doc.getElementsByAttributeValue("type", "submit").first();
        assertThat(submitButton.attr("name"), is(equalTo("action")));

        // post to select the report to run
        response = Unirest.post(fullURL(ReportRunnerServlet.class))
            .field("report.name", reportOption.attr("value"))
            .field("user.id", "3")
            .field("action", submitButton.attr("value"))
            .asString();
        assertResponseStatus(200, response);

        doc = Jsoup.parse(response.getBody());
        submitButton = doc.getElementsByAttributeValue("type", "submit").first();
        assertThat(submitButton.attr("value"), is(equalTo("Run Report")));

        // post to run the report
        response = Unirest.post(fullURL(ReportRunnerServlet.class))
            .field("user.id", "3")
            .field("report.name", reportOption.attr("value"))
            .field(submitButton.attr("name"), submitButton.attr("value"))
            .asString();
        assertResponseStatus(200, response);

        // assert on link back to user summary page
        doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByTag("a");

        //noinspection ThrowableResultOfMethodCallIgnored
        String backToUserSummaryHref = findLinkByText(links, "Back to User Summary")
            .orElseThrow(() -> saveAndFail("Could not find the 'Back to User Summary' link.", response));

        assertThat(backToUserSummaryHref,
            is(equalTo(servletPath(UserSummaryServlet.class, "user.id", "3"))));
    }
}
