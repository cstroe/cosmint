package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.util.TemplateForwarder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(WelcomeServlet.PATH)
public class WelcomeServlet extends HttpServlet {
    public static final String PATH = "/";
    private static final String TEMPLATE = "/template/welcome.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("fw", new TemplateForwarder(req
        ));
        req.getRequestDispatcher(TEMPLATE).forward(req, resp);
    }
}
