package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.transaction.TransactionSearchServlet;
import com.github.cstroe.spendhawk.web.transaction.TransactionView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionSearchServletIT extends BaseClientIT {
    @Test
    public void transaction_search_should_work() throws Exception {

        response = connect(TransactionSearchServlet.class, "account.id", 17L, "q", "Park");
        assertResponseStatus(200, response);

        // assert on link back to user summary page
        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByTag("a");

        //noinspection ThrowableResultOfMethodCallIgnored
        String transactionLink = findLinkByText(links, "Parking Ticket")
                .orElseThrow(() -> saveAndFail("Could not find the 'Parking Ticket' transaction link.", response));

        assertThat(transactionLink,
                is(equalTo(servletPath(TransactionView.class, "id", 11L, "fromAccountId", 17L))));
    }
}
