package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/expense/manage")
public class AddExpenseServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/expense/manage.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String transactionId = StringEscapeUtils.escapeHtml4(req.getParameter("transaction.id"));

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Transaction transaction = Transaction.findById(Long.parseLong(transactionId))
                .orElseThrow(Exceptions::transactionNotFound);

            req.setAttribute("transaction", transaction);
            req.setAttribute("categories", Category.findAll(transaction.getAccount().getUser()));
            req.getRequestDispatcher(TEMPLATE).forward(req, resp);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String amountRaw = StringEscapeUtils.escapeHtml4(req.getParameter("expense.amount"));
        String categoryId = StringEscapeUtils.escapeHtml4(req.getParameter("category.id"));
        String merchant = StringEscapeUtils.escapeHtml4(req.getParameter("expense.merchant"));
        String transactionId = StringEscapeUtils.escapeHtml4(req.getParameter("transaction.id"));
        String action = StringEscapeUtils.escapeHtml4(req.getParameter("action"));

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            if("store".equals(action)) {
                Transaction transaction = Transaction.findById(Long.parseLong(transactionId))
                    .orElseThrow(Exceptions::transactionNotFound);
                Category category =
                        Category.findById(transaction.getAccount().getUser(), Long.parseLong(categoryId));
                Double amount = Double.parseDouble(amountRaw);

                Expense newExpense = new Expense();
                newExpense.setAmount(amount);
                newExpense.setCategory(category);
                newExpense.setMerchant(merchant);
                newExpense.setTransaction(transaction);

                boolean saveSuccess = newExpense.save();

                if (saveSuccess) {
                    transaction.getExpenses().add(newExpense);
                    resp.sendRedirect(req.getContextPath() + servletPath(TransactionView.class) + "?id=" + transaction.getId().toString());
                } else {
                    req.setAttribute("message", "Could not add expense.");
                    req.setAttribute("transaction", transaction);
                    req.setAttribute("categories", Category.findAll(transaction.getAccount().getUser()));
                    req.getRequestDispatcher(TEMPLATE).forward(req, resp);
                }
            }

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }

    }
}
