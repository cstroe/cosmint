package com.github.cstroe.spendhawk.web.category;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.helper.TListTotaler;
import com.github.cstroe.spendhawk.helper.TransactionsHelper;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.AccountServlet;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/category/bulk")
public class BulkCategorizeServlet extends HttpServlet {
    private static final String CONFIGURE_TEMPLATE = "/template/category/bulk_configure.ftl";
    private static final String PREVIEW_TEMPLATE = "/template/category/bulk_preview.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long accountId = Long.parseLong(req.getParameter("account.id"));
        String queryString = req.getParameter("q");
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Account account = Account.findById(accountId)
                .orElseThrow(Exceptions::accountNotFound);
            if(account == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            User user = account.getUser();
            req.setAttribute("account", account);
            req.setAttribute("query", queryString);
            req.setAttribute("categories", user.getCategories());
            req.getRequestDispatcher(CONFIGURE_TEMPLATE).forward(req,resp);
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String action = StringEscapeUtils.escapeHtml4(req.getParameter("action"));
        if("Preview".equals(action)) {
            doPreview(req, resp);
        } else if("Apply categories".equals(action)) {
            doCategorize(req, resp);
        }
    }

    private void doPreview(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Long accountId = Long.parseLong(req.getParameter("account.id"));
        final String query = req.getParameter("q");
        final String merchant = StringEscapeUtils.escapeHtml4(req.getParameter("expense.merchant"));
        final Long categoryId = Long.parseLong(req.getParameter("category.id"));
        final boolean duplicateCheck = req.getParameter("duplicate_check") != null;

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Account account = Account.findById(accountId)
                .orElseThrow(Exceptions::accountNotFound);
            Category category = Category.findById(account.getUser(), categoryId)
                .orElseThrow(Exceptions::categoryNotFound);

            List<Transaction> transactions = account.findTransactions(query);

            List<Transaction> updatedTransactions =
                TransactionsHelper.createExpenses(transactions, category, merchant, duplicateCheck, false);

            req.setAttribute("account", account);
            req.setAttribute("category", category);
            req.setAttribute("q", query);
            req.setAttribute("duplicateCheck", duplicateCheck ? "yes" : "no");
            req.setAttribute("merchant", merchant);
            req.setAttribute("transactions",updatedTransactions);
            req.setAttribute("totaler", new TListTotaler(updatedTransactions));
            req.getRequestDispatcher(PREVIEW_TEMPLATE).forward(req, resp);

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    private void doCategorize(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Long accountId = Long.parseLong(req.getParameter("account.id"));
        final String query = req.getParameter("q");
        final String merchant = StringEscapeUtils.escapeHtml4(req.getParameter("expense.merchant"));
        final Long categoryId = Long.parseLong(req.getParameter("category.id"));
        final boolean duplicateCheck = req.getParameter("duplicate_check") != null;

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Account account = Account.findById(accountId)
                .orElseThrow(Exceptions::accountNotFound);
            Category category = Category.findById(account.getUser(), categoryId)
                .orElseThrow(Exceptions::categoryNotFound);

            List<Transaction> transactions = account.findTransactions(query);
            TransactionsHelper.createExpenses(transactions, category, merchant, duplicateCheck, true);

            resp.sendRedirect(req.getContextPath() + servletPath(AccountServlet.class) +
                    "?id=" + account.getId().toString() +
                    "&relDate=currentMonth");

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
