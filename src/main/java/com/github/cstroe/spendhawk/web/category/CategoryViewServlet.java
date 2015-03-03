package com.github.cstroe.spendhawk.web.category;

import com.github.cstroe.spendhawk.bean.CategoryManagerBean;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/category")
public class CategoryViewServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/category/view.ftl";

    @EJB
    private CategoryManagerBean categoryManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryId = Optional.ofNullable(req.getParameter("id"))
            .orElseThrow(Exceptions::categoryIdNotSpecified);
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Category currentCategory = Category.findById(Long.parseLong(categoryId))
                .orElseThrow(Exceptions::categoryNotFound);

            final User currentUser = currentCategory.getUser();
            List<Category> userCategories = Category.findAll(currentUser).stream()
                .filter(c -> !c.equals(currentCategory)).sorted().collect(Collectors.toList());

            req.setAttribute("fw", new TemplateForwarder(req));
            req.setAttribute("user", currentUser);
            req.setAttribute("userCategories", userCategories);
            req.setAttribute("category", currentCategory);
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
        String action = Optional.ofNullable(req.getParameter("action"))
            .orElseThrow(Exceptions::actionNotSpecified);
        String categoryId = Optional.ofNullable(req.getParameter("category.id"))
            .orElseThrow(Exceptions::categoryIdNotSpecified);

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Category currentCategory = Category.findById(Long.parseLong(categoryId))
                    .orElseThrow(Exceptions::categoryNotFound);
            User currentUser = currentCategory.getUser();

            if("Delete".equals(action)) {
                if(categoryManager.deleteCategory(currentUser.getId(), currentCategory.getId())) {
                    resp.sendRedirect(req.getContextPath() + servletPath(UserSummaryServlet.class) +
                        "?user.id=" + currentUser.getId().toString());
                } else {
                    showError(req, resp, currentUser);
                }
            } else if("Set Parent Category".equals(action)) {
                String parentCategoryIdRaw = Optional.ofNullable(req.getParameter("parentCategory.id"))
                        .orElseThrow(Exceptions::categoryParentNotSpecified);

                Long parentCategoryId;
                if("null".equals(parentCategoryIdRaw)) {
                    parentCategoryId = null;
                } else {
                    parentCategoryId = Long.parseLong(parentCategoryIdRaw);
                }

                if(categoryManager.setParent(currentUser.getId(), currentCategory.getId(), parentCategoryId)) {
                    resp.sendRedirect(req.getContextPath() + servletPath(CategoryViewServlet.class, "id", currentCategory.getId()));
                } else {
                    showError(req, resp, currentUser);
                }
            }

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException, ServletException {
        req.setAttribute("fw", new TemplateForwarder(req));
        req.setAttribute("message", categoryManager.getMessage());
        req.setAttribute("user", currentUser);
        req.getRequestDispatcher(TEMPLATE).forward(req, resp);
    }

}
