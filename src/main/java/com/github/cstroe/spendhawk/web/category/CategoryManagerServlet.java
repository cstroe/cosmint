package com.github.cstroe.spendhawk.web.category;

import com.github.cstroe.spendhawk.bean.CategoryManagerBean;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/category/manage")
public class CategoryManagerServlet extends HttpServlet {

    private final static String TEMPLATE = "/template/category/manage.ftl";

    @EJB
    private CategoryManagerBean categoryManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Optional.ofNullable(req.getParameter("user.id"))
                .map(Long::parseLong)
                .orElseThrow(Exceptions::userIdRequired);

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(userId).orElseThrow(Exceptions::userNotFound);
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher(TEMPLATE).forward(req, resp);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Optional.ofNullable(req.getParameter("user.id"))
                .map(Long::parseLong)
                .orElseThrow(Exceptions::userIdRequired);

        String categoryName = req.getParameter("category.name");
        String action = req.getParameter("action");

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(userId).orElseThrow(Exceptions::userNotFound);

            if("store".equals(action)) {
                Optional<Category> categoryOptional = categoryManager.createCategory(userId, categoryName);

                if(categoryOptional.isPresent()) {
                    resp.sendRedirect(req.getContextPath() + servletPath(UserSummaryServlet.class) + "?user.id=" + userId.toString());
                } else {
                    req.setAttribute("message", categoryManager.getMessage());
                    req.setAttribute("user", currentUser);
                    req.getRequestDispatcher(TEMPLATE).forward(req, resp);
                }
            }

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
