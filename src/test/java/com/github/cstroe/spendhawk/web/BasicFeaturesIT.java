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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    }

    @Test
    @RunAsClient
    @InSequence(200)
    public void connectToUsersServlet() throws Exception {
        String url = deploymentUrl + UsersServlet.PATH.substring(1);
        HttpResponse<String> response = Unirest.get(url).asString();
        assertResponseStatus(200, response);
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
