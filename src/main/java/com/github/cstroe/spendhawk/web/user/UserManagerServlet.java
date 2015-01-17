package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.AccountsServlet;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/users/manage")
public class UserManagerServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/user/manager.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if("Add User".equals(action)) {
            User newUser = doAddUser(request);
            if(newUser != null) {
                response.sendRedirect(request.getContextPath() + servletPath(AccountsServlet.class) + "?id=" + newUser.getId());
            } else {
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }
        } else {
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }
    }

    /**
     * @return null if there was an error
     */
    private User doAddUser(HttpServletRequest request) {
        String username = StringEscapeUtils.escapeHtml4(request.getParameter("user.name"));

        if(username == null || username.isEmpty()) {
            request.setAttribute("message", "User name is empty.");
            return null;
        }

        User newUser = new User();
        newUser.setName(username);

        Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            currentSession.beginTransaction();
            currentSession.save(newUser);
            currentSession.getTransaction().commit();
            return newUser;
        } catch(Exception ex) {
            currentSession.getTransaction().rollback();
            throw ex;
        }
    }
}
