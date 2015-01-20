package com.github.cstroe.spendhawk.web.category;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/category/manage")
public class CategoryManagerServlet extends HttpServlet {

    private final static String TEMPLATE = "/template/category/manage.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = StringEscapeUtils.escapeHtml4(req.getParameter("user.id"));

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(Long.parseLong(userId));
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
        String userId = StringEscapeUtils.escapeHtml4(req.getParameter("user.id"));
        String categoryName = StringEscapeUtils.escapeHtml4(req.getParameter("category.name"));
        String action = StringEscapeUtils.escapeHtml4(req.getParameter("action"));

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(Long.parseLong(userId));

            if("store".equals(action)) {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                newCategory.setUser(currentUser);
                boolean saveSuccess = newCategory.save();

                if (saveSuccess) {
                    resp.sendRedirect(req.getContextPath() + servletPath(UserSummaryServlet.class) + "?user.id=" + currentUser.getId().toString());
                } else {
                    req.setAttribute("message", "Could not add category.");
                    req.setAttribute("user", currentUser);
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
