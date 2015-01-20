package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * The category for an expense.  Categories are at the user level, so that
 * many accounts can use the same categories to categorize their expenditures.
 */
public class Category {
    private Long id;
    private User user;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    @SuppressWarnings("unchecked")
    public static List<Category> findAll() {
        return (List<Category>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Category.class)
                .addOrder(Order.asc("name"))
                .list();
    }

    public static Category findById(Long id) {
        return (Category) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Category.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

}
