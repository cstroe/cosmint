package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.bean.UserManagerBean;
import com.github.cstroe.spendhawk.dao.UserDao;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/users/manage")
public class UserManagerServlet extends HttpServlet {

    @EJB
    private UserManagerBean userManager;

    private static final String TEMPLATE = "/template/user/manager.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if("Add UserDao".equals(action)) {
            String username = StringEscapeUtils.escapeHtml4(request.getParameter("user.name"));
            Optional<UserDao> newUser = userManager.addUser(username);
            if(newUser.isPresent()) {
                response.sendRedirect(request.getContextPath() +
                    servletPath(UserSummaryServlet.class, "user.id", newUser.get().getId()));
            } else {
                request.setAttribute("message", userManager.getMessage());
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }
        } else {
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }
    }
}
