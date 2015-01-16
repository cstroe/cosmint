package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.web.user.UserManagerServlet;
import com.github.cstroe.spendhawk.web.user.UsersServlet;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class BasicFeaturesIT {

    @ArquillianResource
    URL deploymentUrl;

    private static String userDetailUrl;

    @Test
    @RunAsClient
    @InSequence(100)
    public void connectToWelcomeServlet() throws Exception {
        String url = deploymentUrl + WelcomeServlet.PATH.substring(1);
        HttpResponse<String> response = Unirest.get(url).asString();
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByTag("a");
        String usersUrl = servletPath(deploymentUrl, UsersServlet.class);

        assertEquals("A link from the welcome page to the users page must exist. Url: " + usersUrl,
                findLink(usersUrl, links), true);
    }

    @Test
    @RunAsClient
    @InSequence(200)
    public void connectToUsersServlet() throws Exception {
        String url = deploymentUrl + UsersServlet.PATH.substring(1);
        HttpResponse<String> response = Unirest.get(url).asString();
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByClass("userLink");

        assertThat(links.size(), is(equalTo(0)));
    }

    @Test
    @RunAsClient
    @InSequence(300)
    public void connectToUserManagerServlet() throws Exception {
        String url = deploymentUrl + UserManagerServlet.PATH.substring(1);
        HttpResponse<String> response = Unirest.get(url).asString();
        assertResponseStatus(200, response);
    }

    @Test
    @RunAsClient
    @InSequence(400)
    public void createUser() throws Exception {
        String url = deploymentUrl + UserManagerServlet.PATH.substring(1);
        HttpResponse<String> response = Unirest.post(url)
            .field("user.name", "testuser")
            .field("action", "Add User")
            .asString();
        assertResponseStatus(302, response);

        url = deploymentUrl + UsersServlet.PATH.substring(1);
        response = Unirest.get(url).asString();

        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByClass("userLink");

        assertThat(links.size(), is(equalTo(1)));

        Element link = links.get(0);
        userDetailUrl = link.attr("href");

        assertTrue(userDetailUrl.contains(AccountsServlet.PATH + "?"));
    }

    @Test
    @RunAsClient
    @InSequence(500)
    public void viewAccounts() throws Exception {
        String url = deploymentUrl + userDetailUrl;
        HttpResponse<String> response = Unirest.get(url).asString();
        assertResponseStatus(200, response);
    }

    /**
     * Build the path of a given servlet, to be used when verifying that links
     * exist.
     */
    private static String servletPath(URL deploymentUrl, Class<? extends HttpServlet> servlet) {
        StringBuilder servletUrl = new StringBuilder();
        try {
            Field path = servlet.getDeclaredField("PATH");
            String servletPath = (String) path.get(servlet);
            String deploymentPath = deploymentUrl.getPath();
            servletUrl.append(deploymentPath.substring(0, deploymentPath.length()-1))
                      .append(servletPath);

            return servletUrl.toString();
        } catch(IllegalAccessException | NoSuchFieldException ex) {
            return null;
        }
    }

    /**
     * Find a link to the given path.
     * @param links A list of links.
     * @return true if the path was found as one of the links' href, false if none was found
     */
    private static boolean findLink(String path, Elements links) {
        if(path == null || links == null) {
            return false;
        }

        boolean linkFound = false;
        for(Element link : links) {
            String currentLinkPath = link.attr("href");
            if(currentLinkPath.equals(path)) {
                linkFound = true;
            }
        }
        return linkFound;
    }


    /**
     * Save response output to a file if the response status assertion fails.
     */
    private static void assertResponseStatus(int expected, HttpResponse<String> response) throws IOException {
        if(response.getStatus() != expected) {
            File tempFile = File.createTempFile("integrationTest", ".html");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(response.getBody());
            writer.close();
            System.out.println("Saved error output to: file://" + tempFile.getAbsolutePath());
        }

        assertEquals(expected, response.getStatus());
    }
}
