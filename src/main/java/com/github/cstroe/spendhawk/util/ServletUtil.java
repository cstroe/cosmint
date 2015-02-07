package com.github.cstroe.spendhawk.util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

public class ServletUtil {

    public static String servletPath(Class<? extends HttpServlet> servlet) {
        WebServlet servletAnnotation = servlet.getAnnotation(WebServlet.class);
        String[] urlPaths = servletAnnotation.value();
        return urlPaths[0]; // return the first one
    }

    public static String servletPath(Class<? extends HttpServlet> servlet, String... args) {
        StringBuilder path = new StringBuilder();
        path.append(servletPath(servlet));

        if(args.length > 0) {
            path.append("?");
        }

        for(int i = 0; i < args.length; i+=2) {
            if(i > 0) {
                path.append("&");
            }
            path.append(args[i])
                .append("=")
                .append(args[i+1]);
        }

        return path.toString();
    }
}
