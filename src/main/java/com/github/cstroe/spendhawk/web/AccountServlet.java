package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(AccountServlet.PATH)
public class AccountServlet extends HttpServlet {

    public static final String PATH = "/account";
    public static final String TEMPLATE = "/template/account.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accountIdRaw = request.getParameter("id");
            if(accountIdRaw != null) {
                Long accountId = Long.parseLong(accountIdRaw);
                // Begin unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                Account account = Account.findById(accountId);
                if(account == null) {
                    throw new IllegalArgumentException("Account not found.");
                }
                List result = HibernateUtil.getSessionFactory()
                        .getCurrentSession()
                        .createCriteria(Transaction.class)
                        .add(Restrictions.eq("account", account))
                        .addOrder(Order.desc("effectiveDate")).list();

                request.setAttribute("account", account);
                request.setAttribute("transactions", result);
                request.getRequestDispatcher(TEMPLATE).forward(request,response);
            }
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
