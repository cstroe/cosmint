package com.github.cstroe.spendhawk.util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

public class ServletUtil {

    public static String servletPath(Class<? extends HttpServlet> servlet) {
        WebServlet servletAnnotation = servlet.getAnnotation(WebServlet.class);
        String[] urlPaths = servletAnnotation.value();
        return urlPaths[0]; // return the first one
    }
}
