package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/accounts/manage")
public class AccountManagerServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/accounts/manage.ftl";

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = StringEscapeUtils.escapeHtml4(request.getParameter("user.id"));

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            User currentUser = User.findById(Long.parseLong(userId));

            request.setAttribute("user", currentUser);
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountName = StringEscapeUtils.escapeHtml4(request.getParameter("account.name"));
        String userId = StringEscapeUtils.escapeHtml4(request.getParameter("user.id"));

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(Long.parseLong(userId));
            request.setAttribute("user", currentUser);

            // Handle actions
            if ( "store".equals(request.getParameter("action")) ) {
                if ("".equals(accountName)) {
                    request.setAttribute("message", "<b><i>Please enter account name.</i></b>");
                } else {
                    createAndStoreAccount(currentUser, accountName);
                    request.setAttribute("message", "<b><i>Added account.</i></b>");
                }
            }

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }

        request.getRequestDispatcher(TEMPLATE).forward(request,response);
    }

    protected void createAndStoreAccount(User currentUser, String accountName) {
        Account theAccount = new Account();
        theAccount.setName(accountName);
        theAccount.setUser(currentUser);

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(theAccount);
    }
}
