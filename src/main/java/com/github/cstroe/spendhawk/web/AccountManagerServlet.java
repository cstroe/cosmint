package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.AccountManagerBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
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
            User currentUser = User.findById(Long.parseLong(userId))
                .orElseThrow(Exceptions::userNotFound);

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
        String userIdRaw = Optional.ofNullable(request.getParameter("user.id"))
                .map(StringEscapeUtils::escapeHtml4)
                .orElseThrow(Exceptions::userIdRequired);
        String actionRaw = Optional.ofNullable(request.getParameter("action"))
                .orElseThrow(Exceptions::userIdRequired);

        Long userId, accountId;
        try {
            userId = Long.parseLong(userIdRaw);
        } catch (NumberFormatException ex) {
            throw new ServletException(ex);
        }

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        if("store".equals(actionRaw)) {
            String accountName = Optional.ofNullable(request.getParameter("account.name"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .orElseThrow(Exceptions::accountNameRequired);

            Optional<Account> newAccount = accountManager.createAccount(userId, accountName);

            request.setAttribute("user", User.findById(userId).orElseThrow(Exceptions::userNotFound));
            if(newAccount.isPresent()) {
                request.setAttribute("message", "Added account <b>" + newAccount.get().getName() + "</b>.");
            } else {
                request.setAttribute("message", accountManager.getMessage());
            }
        } else if("delete".equals(actionRaw)) {
            String accountIdRaw = Optional.ofNullable(request.getParameter("account.id"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .orElseThrow(Exceptions::accountIdRequired);

            accountId = Long.parseLong(accountIdRaw);
            boolean deleteSuccessful = accountManager.deleteAccount(userId, accountId);

            request.setAttribute("user", User.findById(userId).orElseThrow(Exceptions::userNotFound));
            if(deleteSuccessful) {
                request.setAttribute("message", "Account deleted.");
            } else {
                request.setAttribute("message", accountManager.getMessage());
            }
        }

        request.getRequestDispatcher(TEMPLATE).forward(request,response);
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
    }
}
