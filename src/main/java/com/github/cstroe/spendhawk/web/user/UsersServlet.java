package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(UsersServlet.PATH)
public class UsersServlet extends HttpServlet {

    public static final String PATH = "/users";
    private static final String TEMPLATE = "/template/user/view.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            List users = HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(User.class)
                    .list();

            req.setAttribute("fw", new TemplateForwarder(req));
            req.setAttribute("users", users);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch(Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }

        req.getRequestDispatcher(TEMPLATE).forward(req, resp);
    }
}
