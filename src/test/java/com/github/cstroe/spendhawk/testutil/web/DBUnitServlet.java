package com.github.cstroe.spendhawk.testutil.web;

import com.github.cstroe.spendhawk.util.BaseIT;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dbunit/setup")
public class DBUnitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseIT baseIT = new BaseIT();
        baseIT.setUp();
    }
}
