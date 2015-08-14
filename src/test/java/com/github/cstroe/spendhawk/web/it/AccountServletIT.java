package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.testutil.web.DBUnitServlet;
import com.github.cstroe.spendhawk.web.AccountServlet;
import com.github.cstroe.spendhawk.web.AddTransactionServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.transaction.TransactionView;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class AccountServletIT extends BaseClientIT {

    @Rule
    public TestWatcher saveResponse = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Test name: " + description.getMethodName());
            saveResponseToFile(response);
        }
    };

    @Test
    @RunAsClient
    @InSequence(50)
    public void t0050_seed_database() throws Exception {
        response = connect(DBUnitServlet.class);
        assertResponseStatus(200, response);
    }

    @Test
    @RunAsClient
    @InSequence(100)
    public void t0100_can_connect() {
        connect200(AccountServlet.class, "id", 10l, "relDate", "currentMonth");
    }

    @Test
    @RunAsClient
    @InSequence(110)
    public void t0110_can_see_all_transactions() {
        response = connect(AccountServlet.class,
            "id", 17l, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> transactionLinks = doc.getElementsByTag("a").stream()
            .filter(e -> e.attr("href").startsWith(servletPath(TransactionView.class) + "?"))
            .collect(Collectors.toList());

        assertThat("AccountServlet should respect date range and display 3 transactions.",
            transactionLinks.size(), is(3));
    }

    @Test
    @RunAsClient
    @InSequence(200)
    public void t0200_display_correct_add_transaction_link() {
        response = connect(AccountServlet.class,
            "id", 17l, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> links = doc.getElementsByTag("a").stream()
            .filter(e -> e.attr("href").startsWith(servletPath(AddTransactionServlet.class) + "?"))
            .collect(Collectors.toList());

        assertThat("There should only be one link to add transactions", links.size(), is(1));

        Element addTransactionLink = links.get(0);

        String correctLink = servletPath(AddTransactionServlet.class, "user.id", 3l, "account.id", 17l);

        assertThat(addTransactionLink.attr("href"), is(equalTo(correctLink)));
    }

    @Test
    @RunAsClient
    @InSequence(300)
    public void t0300_display_correct_accounts_link() {
        response = connect(AccountServlet.class,
                "id", 17l, "start", "01-01-2015", "end", "01-01-2015");

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        List<Element> links = doc.getElementsByTag("a").stream()
                .filter(e -> e.attr("href").startsWith(servletPath(UserSummaryServlet.class) + "?"))
                .collect(Collectors.toList());

        assertThat("There should be one link to the user summary page.", links.size(), is(1));

        Element addTransactionLink = links.get(0);

        String correctLink = servletPath(UserSummaryServlet.class, "user.id", 3l);

        assertThat(addTransactionLink.attr("href"), is(equalTo(correctLink)));
    }
}
