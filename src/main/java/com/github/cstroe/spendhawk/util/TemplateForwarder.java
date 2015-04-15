package com.github.cstroe.spendhawk.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * A utility class used to create well formed urls to be inserted in our templates.
 */
public class TemplateForwarder {
    String contextPath0;
    String contextPath;

    public TemplateForwarder(HttpServletRequest request) {
        contextPath0 = request.getContextPath();
        contextPath = contextPath0 + "/";
    }

    @SuppressWarnings("unused")
    public String getContextPath() {
        return contextPath0;
    }

    public String url(String path) {
        if (path.startsWith("/")) {
            return contextPath0 + path;
        } else {
            return contextPath + path;
        }
    }

    public String url(String path, Object... param) {
        StringBuilder url = new StringBuilder();
        if (path.startsWith("/")) {
            url.append(contextPath0).append(path);
        } else {
            url.append(contextPath).append(path);
        }

        url.append("?");
        for(int i = 0; i < param.length; i=i+2) {
            if(i > 0) {
                url.append("&");
            }
            url.append(param[i]).append("=").append(param[i+1]);
        }

        return url.toString();
    }

    public String servlet(String className, Object... param) throws ClassNotFoundException {
        Class<?> classObject = Class.forName(className);
        if(HttpServlet.class.isAssignableFrom(classObject)) {
            @SuppressWarnings("unchecked")
            Class<? extends HttpServlet> servletClass = (Class<? extends HttpServlet>) classObject;
            return url(ServletUtil.servletPath(servletClass), param);
        } else {
            throw new IllegalArgumentException(className + " does not extend HttpServlet.");
        }
    }
}
