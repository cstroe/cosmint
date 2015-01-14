package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(AccountsServlet.PATH)
public class AccountsServlet extends HttpServlet {

    public static final String PATH = "/accounts";
    private static final String TEMPLATE = "/template/accounts.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = Long.parseLong(request.getParameter("id"));

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            User user = (User) HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(User.class)
                    .add(Restrictions.eq("id", userId))
                    .uniqueResult();

            request.setAttribute("accounts", user.getAccounts());
            request.getRequestDispatcher(TEMPLATE).forward(request,response);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch(Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }
}
