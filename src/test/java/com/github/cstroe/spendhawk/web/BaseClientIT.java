package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.web.category.CategoryManagerServlet;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;
import static com.github.cstroe.spendhawk.util.TestUtil.getLink;
import static org.junit.Assert.assertEquals;

/**
 * A base class for integration tests that are on run on the client side.
 */
@RunWith(Arquillian.class)
public class BaseClientIT {

    @ArquillianResource
    protected URL deploymentUrl;

    protected HttpResponse<String> response;

    /**
     * Add the deployment context path to a servlet's path to create an absolute path.
     *
     * For example, given:
     *    deploymentUrl: "http://127.0.0.1:8080/a1dgfdw"
     *    servlet path: "/accounts/manage"
     *
     * Return: "/a1dgfdw/accounts/manage"
     */
    protected String fullServletPath(Class<? extends HttpServlet> servlet) {
        StringBuilder servletUrl = new StringBuilder();
        final String deploymentPath = deploymentUrl.getPath();
        servletUrl.append(deploymentPath.substring(0, deploymentPath.length()-1));
        servletUrl.append(servletPath(servlet));
        return servletUrl.toString();
    }

    /**
     * A helper method to connect to a specific servlet on the test deployment.
     */
    protected HttpResponse<String> connect(Class<? extends HttpServlet> servlet) {
        try {
            return Unirest.get(url(servlet)).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A helper method to connect to a specific servlet on the test deployment.
     */
    protected HttpResponse<String> connect(Class<? extends HttpServlet> servlet, String... params) {
        try {
            return Unirest.get(url(servlet, params)).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected HttpResponse<String> connect(String absolutePath) {
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
            } else {
                url.append(requestPath);
            }
        }

        return url.toString();
    }

    /**
     * Create the url for a servlet on the test deployment.
     */
    protected String url(Class<? extends HttpServlet> servlet) {
        return "http://" +
                deploymentUrl.getHost() +
                ":" +
                deploymentUrl.getPort() +
                fullServletPath(servlet);
    }

    protected String url(Class<? extends HttpServlet> servlet, String... params) {
        StringBuilder url = new StringBuilder();
        url.append("http://")
                .append(deploymentUrl.getHost())
                .append(":")
                .append(deploymentUrl.getPort())
                .append(fullServletPath(servlet));

        for(int i = 0; i < params.length; i=i+2) {
            if(i == 0) {
                url.append("?");
            } else {
                url.append("&");
            }
            url.append(params[i])
                    .append("=")
                    .append(params[i+1]);
        }

        return url.toString();
    }

    /**
     * Find a link to the given path.
     * @return true if the path was found as one of the links' href, false if none was found
     */
    protected boolean hasLink(Document doc, String path, String... arguments) {
        return getLink(doc, path, arguments) != null;
    }

    /**
     * @return the link url that has the given text, null if a link with that
     *         text is not found
     */
    protected static String findLinkByText(Elements links, String text) {
        for(Element e : links) {
            if(e.ownText().equals(text)) {
                return e.attr("href");
            }
        }
        return null;
    }

    /**
     * Save response output to a file if the response status assertion fails.
     */
    protected static void assertResponseStatus(int expected, HttpResponse<String> response) throws IOException {
        if(response.getStatus() != expected) {
            File tempFile = File.createTempFile("integrationTest", ".html");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(response.getBody());
            writer.close();
            System.out.println("Saved response page to: file://" + tempFile.getAbsolutePath());
        }

        assertEquals(expected, response.getStatus());
    }

    /**
     * Create a category via the CategoryManagerServlet.
     */
    protected HttpResponse<String> createCategory(Long userId, String categoryName) throws Exception {
        HttpResponse<String> response = Unirest.post(url(CategoryManagerServlet.class))
                .field("user.id", userId)
                .field("category.name", categoryName)
                .field("action", "store")
                .asString();

        assertResponseStatus(302, response);
        return response;
    }
}
