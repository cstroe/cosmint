package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.testutil.web.DBUnitServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.transaction.TransactionSearchServlet;
import com.github.cstroe.spendhawk.web.transaction.TransactionView;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class TransactionSearchServletIT extends BaseClientIT {

    @Before
    public void setUp() {
        connect200(DBUnitServlet.class); // reset the DB each time
    }

    @Test
    @RunAsClient
    public void transaction_search_should_work() throws Exception {

        response = connect(TransactionSearchServlet.class, "account.id", 17l, "q", "Park");
        assertResponseStatus(200, response);

        // assert on link back to user summary page
        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByTag("a");

        //noinspection ThrowableResultOfMethodCallIgnored
        String transactionLink = findLinkByText(links, "Parking Ticket")
                .orElseThrow(() -> saveAndFail("Could not find the 'Parking Ticket' transaction link.", response));

        assertThat(transactionLink,
                is(equalTo(servletPath(TransactionView.class, "id", 11l, "fromAccountId", 17l))));
    }
}
