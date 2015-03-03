package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.testutil.web.DBUnitServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import com.github.cstroe.spendhawk.web.category.CategoryViewServlet;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.cstroe.spendhawk.util.TestUtil.createArgumentMap;
import static com.github.cstroe.spendhawk.util.TestUtil.getArguments;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryViewServletIT extends BaseClientIT {

    private HttpResponse<String> response;

    private static final String CATEGORY_A = "Category A";
    private static final String CATEGORY_B = "Category B";
    private static final String CATEGORY_C = "Category C";
    private static final String CATEGORY_D = "Category D";

    @Test
    @RunAsClient
    @InSequence(50)
    public void t0050_seedDatabase() throws Exception {
        connect200(DBUnitServlet.class);
    }


    @Test
    @RunAsClient
    @InSequence(100)
    public void t0100_shouldViewCategoryWithCorrectForm() throws Exception {
        createCategory(7l, CATEGORY_A);
        createCategory(7l, CATEGORY_B);
        createCategory(7l, CATEGORY_C);
        createCategory(7l, CATEGORY_D);

        response = connect(UserSummaryServlet.class, "user.id", "7");

        Document doc = Jsoup.parse(response.getBody());
        Elements categoryLinks = doc.getElementsByClass("categoryLink");

        Long categoryD = getCategoryIdFromLinks(CATEGORY_D, categoryLinks);
        response = connect200(CategoryViewServlet.class, "id", categoryD);

        doc = Jsoup.parse(response.getBody());
        Elements selectOptions = doc.getElementsByClass("categoryOption");
        assertThat(selectOptions.size(), is(3));

        List<String> categoryNames = selectOptions.stream()
            .map(Element::text).collect(Collectors.toList());

        assertTrue(categoryNames.contains(CATEGORY_A));
        assertTrue(categoryNames.contains(CATEGORY_B));
        assertTrue(categoryNames.contains(CATEGORY_C));

        Element parent = doc.getElementById("categoryParent");
        assertNull("No parent should be displayed.", parent);
    }

    @Test
    @RunAsClient
    @InSequence(200)
    public void t0200_setCategoryParent() throws Exception {
        response = connect(UserSummaryServlet.class, "user.id", "7");
        assertResponseStatus(200, response);
        Document doc = Jsoup.parse(response.getBody());
        Elements categoryLinks = doc.getElementsByClass("categoryLink");
        assertThat(categoryLinks.size(), is(4));

        Long catBId = getCategoryIdFromLinks(CATEGORY_B, categoryLinks);
        Long catCId = getCategoryIdFromLinks(CATEGORY_C, categoryLinks);

        response = connect(CategoryViewServlet.class, "id", catCId);

        response = Unirest.post(url(CategoryViewServlet.class))
            .field("action", "Set Parent Category")
            .field("category.id", catCId)
            .field("parentCategory.id", catBId)
            .asString();

        assertResponseStatus(302, response);

        String redirectUrl = response.getHeaders().getFirst("location");
        URL url = new URL(redirectUrl);
        assertTrue("Setting a parent category should take you to the view page for that category.",
            url.getPath().startsWith(fullServletPath(CategoryViewServlet.class)) &&
                url.getQuery().contains("id=" + catCId));

        response = connect200(CategoryViewServlet.class, "id", catCId);

        doc = Jsoup.parse(response.getBody());
        Element parent = doc.getElementById("categoryParent");
        assertNotNull("Parent should be displayed.", parent);
        assertThat("Displayed parent should be correct.",
                parent.text(), is(equalTo(CATEGORY_B)));
    }

    private Long getCategoryIdFromLinks(String categoryName, Elements categoryLinks) {
        Element categoryDLink = categoryLinks.stream()
            .filter(link -> link.text().equals(categoryName)).findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find link for '" + categoryName + "'."));

        String queryArguments = getArguments(categoryDLink.attr("href"))
            .orElseThrow(() -> new RuntimeException("Link for '" + categoryName + "' should the href argument."));

        return Long.parseLong(createArgumentMap(queryArguments).get("id"));
    }
}
