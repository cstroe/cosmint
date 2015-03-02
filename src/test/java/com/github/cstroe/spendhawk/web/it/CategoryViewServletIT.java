package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.testutil.web.DBUnitServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.category.CategoryViewServlet;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import com.mashape.unirest.http.HttpResponse;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.cstroe.spendhawk.util.TestUtil.createArgumentMap;
import static com.github.cstroe.spendhawk.util.TestUtil.getArguments;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CategoryViewServletIT extends BaseClientIT {

    private HttpResponse<String> response;

    @Test
    @RunAsClient
    @InSequence(50)
    public void t0050_seedDatabase() throws Exception {
        response = connect(DBUnitServlet.class);
        assertResponseStatus(200, response);
    }


    @Test
    @RunAsClient
    @InSequence(100)
    public void t0100_shouldViewCategoryWithCorrectForm() throws Exception {
        createCategory(7l, "Category A");
        createCategory(7l, "Category B");
        createCategory(7l, "Category C");
        createCategory(7l, "Category D");

        response = connect(UserSummaryServlet.class, "user.id", "7");

        Document doc = Jsoup.parse(response.getBody());
        Elements categoryLinks = doc.getElementsByClass("categoryLink");

        Element categoryDLink = categoryLinks.stream()
                .filter(link -> link.text().equals("Category D")).findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find link for 'Category D'."));

        String queryArguments = getArguments(categoryDLink.attr("href"))
            .orElseThrow(() -> new RuntimeException("Link should have arguments."));
        String categoryD = createArgumentMap(queryArguments).get("id");

        response = connect(CategoryViewServlet.class, "id", categoryD);
        assertResponseStatus(200, response);

        doc = Jsoup.parse(response.getBody());
        Elements selectOptions = doc.getElementsByClass("categoryOption");
        assertThat(selectOptions.size(), is(3));

        List<String> categoryNames = selectOptions.stream()
            .map(Element::text).collect(Collectors.toList());

        assertTrue(categoryNames.contains("Category A"));
        assertTrue(categoryNames.contains("Category B"));
        assertTrue(categoryNames.contains("Category C"));
    }

}
