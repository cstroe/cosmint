package com.github.cstroe.spendhawk.web.it;

//import com.github.cstroe.spendhawk.web.AccountServlet;
import com.github.cstroe.spendhawk.web.AddTransactionServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.transaction.TransactionView;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountServletIT extends BaseClientIT {

    @Rule
    public TestWatcher saveResponse = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Test name: " + description.getMethodName());
            saveResponseToFile(response);
        }
    };

//    @Test
//    public void t0100_can_connect() {
//        connect200(AccountServlet.class, "id", 10L, "relDate", "currentMonth");
//    }

    @Test
    public void t0110_can_see_all_transactions() {
//        response = connect(AccountServlet.class,
//            "id", 17L, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> transactionLinks = doc.getElementsByTag("a").stream()
            .filter(e -> e.attr("href").startsWith(servletPath(TransactionView.class) + "?"))
            .collect(Collectors.toList());

        assertThat("AccountServlet should respect date range and display 3 transactions.",
            transactionLinks.size(), is(3));
    }

    @Test
    public void t0200_display_correct_add_transaction_link() {
//        response = connect(AccountServlet.class,
//            "id", 17L, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> links = doc.getElementsByTag("a").stream()
            .filter(e -> e.attr("href").startsWith(servletPath(AddTransactionServlet.class) + "?"))
            .collect(Collectors.toList());

        assertThat("There should only be one link to add transactions", links.size(), is(1));

        Element addTransactionLink = links.get(0);

        String correctLink = servletPath(AddTransactionServlet.class, "user.id", 3L, "account.id", 17L);

        assertThat(addTransactionLink.attr("href"), is(equalTo(correctLink)));
    }

    @Test
    public void t0300_display_correct_accounts_link() {
//        response = connect(AccountServlet.class,
//                "id", 17L, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> links = doc.getElementsByTag("a").stream()
                .filter(e -> e.attr("href").startsWith(servletPath(UserSummaryServlet.class) + "?"))
                .collect(Collectors.toList());

        assertThat("There should be one link to the user summary page.", links.size(), is(1));

        Element addTransactionLink = links.get(0);

        String correctLink = servletPath(UserSummaryServlet.class, "user.id", 3L);

        assertThat(addTransactionLink.attr("href"), is(equalTo(correctLink)));
    }
}
