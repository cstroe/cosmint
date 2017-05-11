package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.web.AccountManagerServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AccountManagerServletIT extends BaseClientIT {

    private static final Long USER_ID = 7L;

    @Test
    public void t0100_should_have_correct_link_back_to_user_summary_page() throws Exception {

//        response = connect(AccountManagerServlet.class, "user.id", USER_ID);
        assertResponseStatus(200, response);

        // assert on link back to user summary page
        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByTag("a");

        //noinspection ThrowableResultOfMethodCallIgnored
        String backToUserSummaryHref = findLinkByText(links, "Back to User Summary")
            .orElseThrow(() -> saveAndFail("Could not find the 'Back to User Summary' link.", response));

        assertThat(backToUserSummaryHref,
            is(equalTo(servletPath(UserSummaryServlet.class, "user.id", USER_ID))));
    }
}
