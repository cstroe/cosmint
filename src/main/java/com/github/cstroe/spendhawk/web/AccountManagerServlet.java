package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/accounts")
public class AccountManagerServlet extends HttpServlet {

    private static final String TEMPLATE = "/accounts.ftl";

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            List result = HibernateUtil.getSessionFactory()
                    .getCurrentSession().createCriteria(Account.class).list();

            request.setAttribute("accounts", result);
            request.getRequestDispatcher(TEMPLATE).forward(request,response);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            // Handle actions
            if ( "store".equals(request.getParameter("action")) ) {

                String accountName = StringEscapeUtils.escapeHtml4(request.getParameter("accountName"));

                if ("".equals(accountName)) {
                    request.setAttribute("message", "<b><i>Please enter account name.</i></b>");
                }
                else {
                    createAndStoreAccount(accountName);
                    request.setAttribute("message", "<b><i>Added account.</i></b>");
                }
            }

            List result = HibernateUtil.getSessionFactory()
                    .getCurrentSession().createCriteria(Account.class).list();

            request.setAttribute("accounts", result);
            request.getRequestDispatcher(TEMPLATE).forward(request,response);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    protected void createAndStoreAccount(String accountName) {
        Account theAccount = new Account();
        theAccount.setName(accountName);

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(theAccount);
    }
}
