package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.AccountServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/transaction")
public class TransactionView extends HttpServlet {

    private static final String TEMPLATE = "/template/transactions/view.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String transactionIdRaw = request.getParameter("id");
            if(transactionIdRaw != null) {
                Long transactionId = Long.parseLong(transactionIdRaw);
                // Begin unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                Transaction transaction = Transaction.findById(transactionId);
                if(transaction == null) {
                    throw new IllegalArgumentException("Transaction not found.");
                }
                request.setAttribute("transaction", transaction);
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
            String transactionIdRaw = request.getParameter("id");
            if(transactionIdRaw != null) {
                Long transactionId = Long.parseLong(transactionIdRaw);
                // Begin unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                Transaction transaction = Transaction.findById(transactionId);
                if(transaction == null) {
                    throw new IllegalArgumentException("Transaction not found.");
                }
                account = transaction.getAccount();
                transaction.delete();
            } else {
                throw new IllegalAccessException("Transaction id is required.");
            }
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }

        response.sendRedirect(request.getContextPath() + AccountServlet.PATH + "?id=" + account.getId());
    }
}
