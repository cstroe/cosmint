package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/transactions/add")
public class AddTransactionServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/transactions/add.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accountIdRaw = request.getParameter("id");
            if(accountIdRaw != null) {
                Long accountId = Long.parseLong(accountIdRaw);
                // Begin unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                Account account = (Account) HibernateUtil.getSessionFactory().getCurrentSession()
                        .createCriteria(Account.class)
                        .add(Restrictions.eq("id", accountId))
                        .uniqueResult();
                if(account == null) {
                    throw new IllegalArgumentException("Account not found.");
                }

                request.setAttribute("account", account);
                request.getRequestDispatcher(TEMPLATE).forward(request,response);
            }
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account account;
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            // Handle actions
            String dateRaw = StringEscapeUtils.escapeHtml4(request.getParameter("date"));
            String amountRaw = StringEscapeUtils.escapeHtml4(request.getParameter("amount"));
            String description = StringEscapeUtils.escapeHtml4(request.getParameter("description"));
            String notes = StringEscapeUtils.escapeHtml4(request.getParameter("notes"));
            String accountIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("account_id"));

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormatter.parse(dateRaw);
            Double amount = Double.parseDouble(amountRaw);
            Long accountId = Long.parseLong(accountIdRaw);

            account = Account.findById(accountId);

            Transaction t = new Transaction();
            t.setEffectiveDate(date);
            t.setAmount(amount);
            t.setDescription(description);
            t.setNotes(notes);
            t.setAccount(account);

            HibernateUtil.getSessionFactory().getCurrentSession().save(t);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            // End unit of work
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }

        if(account == null) {
            response.sendRedirect(request.getContextPath() + servletPath(AccountsServlet.class));
        } else {
            response.sendRedirect(request.getContextPath() + servletPath(AccountsServlet.class) + "?id=" + account.getId());
        }
    }
}
