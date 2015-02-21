package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.helper.TransactionsHelper;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/transactions/upload")
@MultipartConfig
public class TransactionsUploadServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/transactions/upload.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Long accountId = Long.parseLong(req.getParameter("id"));
            Account account = Account.findById(accountId)
                .orElseThrow(Exceptions::accountNotFound);
            req.setAttribute("messages", new LinkedList<String>());
            req.setAttribute("account", account);

            req.getRequestDispatcher(TEMPLATE).forward(req, resp);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create path components to save the file
        final String fileFormat = request.getParameter("format");
        final Part filePart = request.getPart("file");
        final Long accountId = Long.parseLong(request.getParameter("id"));
        final boolean duplicateCheck = request.getParameter("duplicate_check") != null;

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            InputStream filecontent = filePart.getInputStream();
            Account account = Account.findById(accountId)
                .orElseThrow(Exceptions::accountNotFound);

            List<String> messages = TransactionsHelper.processFile(filecontent, account, duplicateCheck, fileFormat);

            messages.add("Duplicate check: " + Boolean.toString(duplicateCheck));

            request.setAttribute("account", account);
            request.setAttribute("messages", messages);
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
