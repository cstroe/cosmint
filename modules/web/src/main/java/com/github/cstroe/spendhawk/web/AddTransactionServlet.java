package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.TransactionManagerBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.DateUtil;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/transactions/add")
public class AddTransactionServlet extends HttpServlet {

    @Inject
    private TransactionManagerBean tMan;

    private static final String TEMPLATE = "/template/transactions/add.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        org.hibernate.Transaction transaction = null;
        try {
            Long userId = Long.parseLong(Optional.ofNullable(request.getParameter("user.id"))
                .orElseThrow(Ex::userIdRequired));
            Long accountId = Long.parseLong(Optional.ofNullable(request.getParameter("account.id"))
                .orElseThrow(Ex::accountIdRequired));
            // Begin unit of work
            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            User user = User.findById(userId).orElseThrow(Ex::userNotFound);
            Account account = Account.findById(accountId).orElseThrow(Ex::accountNotFound);

            request.setAttribute("user", user);
            request.setAttribute("account", account);
            request.getRequestDispatcher(TEMPLATE).forward(request,response);
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            if(transaction != null) {
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            }
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account account;
        Transaction t;
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            // Handle actions
            String userIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("user.id"));
            String accountIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("account.id"));
            String dateRaw = StringEscapeUtils.escapeHtml4(request.getParameter("date"));
            String description = StringEscapeUtils.escapeHtml4(request.getParameter("description"));
            String notes = StringEscapeUtils.escapeHtml4(request.getParameter("notes"));

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormatter.parse(dateRaw);
            Long accountId = Long.parseLong(accountIdRaw);

            account = Account.findById(accountId)
                .orElseThrow(Ex::accountNotFound);

            Optional<Transaction> newT = tMan.createTransaction(
                Long.parseLong(userIdRaw), date, description, notes,
                request.getParameterValues("cfaccount[]"),
                request.getParameterValues("cfamount[]"));

            t = newT.get();

            //HibernateUtil.getSessionFactory().getCurrentSession().save(t);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            // End unit of work
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex.getMessage(), ex);
        }

        if(t == null) {
            response.sendRedirect(request.getContextPath() + servletPath(WelcomeServlet.class));
        } else {
            response.sendRedirect(request.getContextPath() + servletPath(AccountServlet.class) +
                "?id=" + account.getId() + "&relDate=" + "currentMonth");
        }
    }
}