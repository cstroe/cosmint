package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.AccountManagerBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/accounts/manage")
public class AccountManagerServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/accounts/manage.ftl";

    @EJB
    private AccountManagerBean accountManager;

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
        String userIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("user.id"));

        Long userId;
        try {
            userId = Long.parseLong(userIdRaw);
        } catch (NumberFormatException ex) {
            throw new ServletException("User id is not a valid long number.", ex);
        }

        Optional<Account> newAccount = accountManager.createAccount(userId, accountName);

        if(newAccount.isPresent()) {
            request.setAttribute("user", newAccount.get().getUser());
            request.setAttribute("message", "Added account <b>" + newAccount.get().getName() + "</b>.");
        } else {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            request.setAttribute("user", User.findById(userId));
            request.setAttribute("message", accountManager.getMessage());
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }

        request.getRequestDispatcher(TEMPLATE).forward(request,response);
    }
}
