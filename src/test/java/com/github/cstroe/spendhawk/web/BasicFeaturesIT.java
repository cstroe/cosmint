package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.web.user.UserManagerServlet;
import com.github.cstroe.spendhawk.web.user.UsersServlet;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
import java.net.MalformedURLException;
import java.net.URL;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BasicFeaturesIT {

    @ArquillianResource
    URL deploymentUrl;

    private static String userDetailPath;

    private HttpResponse<String> response;

    @Test
    @RunAsClient
    @InSequence(100)
    public void connectToWelcomeServlet() throws Exception {
        response = connect(WelcomeServlet.class);
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        boolean usersLinkExists = findLink(fullServletPath(UsersServlet.class), doc);
        assertEquals("A link from the welcome page to the users page must exist.", usersLinkExists, true);
    }

    @Test
    @RunAsClient
    @InSequence(200)
    public void connectToUsersServlet() throws Exception {
        response = connect(UsersServlet.class);
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByClass("userLink");

        assertThat(links.size(), is(equalTo(0)));
    }

    @Test
    @RunAsClient
    @InSequence(300)
    public void connectToUserManagerServlet() throws Exception {
        response = connect(UserManagerServlet.class);
        assertResponseStatus(200, response);
    }

    @Test
    @RunAsClient
    @InSequence(400)
    public void createUser() throws Exception {
        HttpResponse<String> response = Unirest.post(url(UserManagerServlet.class))
            .field("user.name", "testuser")
            .field("action", "Add User")
            .asString();
        assertResponseStatus(302, response);

        // location header takes you to accounts page for that user
        String redirectUrl = response.getHeaders().getFirst("location");
        String urlPath = new URL(redirectUrl).getPath();
        assertTrue(urlPath.startsWith(fullServletPath(AccountsServlet.class)));

        String viewUsersUrl = url(UsersServlet.class);
        response = Unirest.get(viewUsersUrl).asString();

        Document doc = Jsoup.parse(response.getBody());
        Elements links = doc.getElementsByClass("userLink");

        assertThat(links.size(), is(equalTo(1)));

        Element link = links.get(0);
        userDetailPath = link.attr("href");

        assertTrue(userDetailPath.startsWith(fullServletPath(AccountsServlet.class) + "?"));
    }

    @Test
    @RunAsClient
    @InSequence(500)
    public void viewAccounts() throws Exception {
        response = connect(userDetailPath);
        assertResponseStatus(200, response);

        Document doc = Jsoup.parse(response.getBody());
        boolean accountManagerLinkExists = findLink(fullServletPath(AccountManagerServlet.class), doc);
        assertEquals("A link from the accounts page to the account manager page must exist.", accountManagerLinkExists, true);
    }

    /**
     * Add the deployment context path to a servlet's path to create an absolute path.
     *
     * For example, given:
     *    deploymentUrl: "http://127.0.0.1:8080/a1dgfdw"
     *    servlet path: "/accounts/manage"
     *
     * Return: "/a1dgfdw/accounts/manage"
     */
    private String fullServletPath(Class<? extends HttpServlet> servlet) {
        StringBuilder servletUrl = new StringBuilder();
        final String deploymentPath = deploymentUrl.getPath();
        servletUrl.append(deploymentPath.substring(0, deploymentPath.length()-1));
        servletUrl.append(servletPath(servlet));
        return servletUrl.toString();
    }

    /**
     * A helper method to connect to a specific servlet on the test deployment.
     */
    private HttpResponse<String> connect(Class<? extends HttpServlet> servlet) {
        try {
            return Unirest.get(url(servlet)).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> connect(String absolutePath) {
        try {
            return Unirest.get(url(absolutePath)).asString();
        } catch (UnirestException | MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Build the deployment url given the request path on the server.
     */
    private String url(String requestPath) throws MalformedURLException {
        StringBuilder url = new StringBuilder();
        url.append("http://")
           .append(deploymentUrl.getHost())
           .append(":")
           .append(deploymentUrl.getPort());

        if(requestPath.startsWith(deploymentUrl.getPath())) {
            url.append(requestPath);
        } else {
            url.append(deploymentUrl.getPath());
            if(requestPath.startsWith("/")) {
                url.append(requestPath.substring(1));
            }
            url.append(requestPath);
        }

        return url.toString();
    }

    /**
     * Create the url for a servlet on the test deployment.
     */
    private String url(Class<? extends HttpServlet> servlet) {
        return "http://" +
               deploymentUrl.getHost() +
               ":" +
               deploymentUrl.getPort() +
               fullServletPath(servlet);
    }

    /**
     * Find a link to the given path.
     * @return true if the path was found as one of the links' href, false if none was found
     */
    private static boolean findLink(String path, Document doc) {
        Elements links = doc.getElementsByTag("a");
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

        if(!linkFound) {
            try {
                File tempFile = File.createTempFile("integrationTest", ".html");
                FileWriter writer = new FileWriter(tempFile);
                writer.write(doc.toString());
                writer.close();
                System.out.println("Saved response page to: file://" + tempFile.getAbsolutePath());
            } catch(Exception ex) {
                ex.printStackTrace();
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
            System.out.println("Saved response page to: file://" + tempFile.getAbsolutePath());
        }

        assertEquals(expected, response.getStatus());
    }
}
