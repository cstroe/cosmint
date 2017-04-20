package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.util.ServletUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

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
    protected String servletPath(Class<? extends HttpServlet> servlet) {
        return servletPath(servlet, new Object[0]);
    }

    protected String servletPath(Class<? extends HttpServlet> servlet, Object... params) {
        StringBuilder servletUrl = new StringBuilder();
        final String deploymentPath = deploymentUrl.getPath();
        servletUrl.append(deploymentPath.substring(0, deploymentPath.length()-1));
        servletUrl.append(ServletUtil.servletPath(servlet));

        for(int i = 0; i < params.length; i=i+2) {
            if(i == 0) {
                servletUrl.append("?");
            } else {
                servletUrl.append("&");
            }
            servletUrl.append(params[i].toString())
                    .append("=")
                    .append(params[i+1]);
        }

        return servletUrl.toString();
    }

    /**
     * A helper method to connect to a specific servlet on the test deployment.
     */
    protected HttpResponse<String> connect(Class<? extends HttpServlet> servlet) {
        try {
            return Unirest.get(fullURL(servlet)).asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected HttpResponse<String> connect200(Class<? extends HttpServlet> servlet, Object... params) {
        HttpResponse<String> resp = connect(servlet, params);
        assertResponseStatus(200, resp);
        return resp;
    }

    /**
     * A helper method to connect to a specific servlet on the test deployment.
     */
    protected HttpResponse<String> connect(Class<? extends HttpServlet> servlet, Object... params) {
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
    protected String fullURL(Class<? extends HttpServlet> servlet) {
        return "http://" +
                deploymentUrl.getHost() +
                ":" +
                deploymentUrl.getPort() +
                servletPath(servlet);
    }

    protected String url(Class<? extends HttpServlet> servlet, Object... params) {
        return "http://" +
               deploymentUrl.getHost() +
               ":" +
               deploymentUrl.getPort() +
               servletPath(servlet, params);
    }

    /**
     * @return the link url that has the given text, null if a link with that
     *         text is not found
     */
    protected static Optional<String> findLinkByText(Elements links, String text) {
        for(Element e : links) {
            if(e.ownText().equals(text)) {
                return Optional.of(e.attr("href"));
            }
        }
        return Optional.empty();
    }

    /**
     * Save response output to a file if the response status assertion fails.
     */
    protected static void assertResponseStatus(int expected, HttpResponse<String> response) {
        if (response.getStatus() != expected) {
            saveResponseToFile(response);
        }

        assertEquals(expected, response.getStatus());
    }

    protected static void saveResponseToFile(HttpResponse<String> response) {
        File tempFile;
        try (FileWriter writer = new FileWriter(tempFile = File.createTempFile("integrationTest", ".html"))) {
            writer.write(response.getBody());
            System.out.println("Saved response page to: file://" + tempFile.getAbsolutePath());
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected AssertionError saveAndFail(String message, HttpResponse<String> response) {
        saveResponseToFile(response);
        return new AssertionError(message);
    }
}
