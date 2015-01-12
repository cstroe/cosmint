package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {

    public static final String PATH = "/accounts";
    private static final String TEMPLATE = "/template/accounts.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
            throw new ServletException(ex);
        }
    }
}
