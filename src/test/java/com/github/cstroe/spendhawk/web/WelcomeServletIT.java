package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.web.user.UserManagerServlet;
import com.github.cstroe.spendhawk.web.user.UsersServlet;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class WelcomeServletIT {

    @ArquillianResource
    URL deploymentUrl;

    @Test
    @RunAsClient
    @InSequence(1)
    public void connectToWelcomeServlet() throws Exception {
        String requestUrl = deploymentUrl + WelcomeServlet.PATH.substring(1);
        URL url = new URL(requestUrl);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        int statusCode = http.getResponseCode();

        assertEquals(200, statusCode);
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void connectToUsersServlet() throws Exception {
        String requestUrl = deploymentUrl + UsersServlet.PATH.substring(1);

        URL url = new URL(requestUrl);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        int statusCode = http.getResponseCode();

        assertEquals(200, statusCode);
    }

    @Test
    @RunAsClient
    @InSequence(3)
    public void connectToUserManagerServlet() throws Exception {
        String requestUrl = deploymentUrl + UserManagerServlet.PATH.substring(1);

        URL url = new URL(requestUrl);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        int statusCode = http.getResponseCode();

        assertEquals(200, statusCode);
    }
}
