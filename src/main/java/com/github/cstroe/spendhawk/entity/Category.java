package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The category for an expense.  Categories are at the user level, so that
 * many accounts can use the same categories to categorize their expenditures.
 */
public class Category implements Comparable<Category> {
    private Long id;
    private User user;
    private String name;
    private Category parent;
    private Set<Category> children;

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

    public Category getParent() {
        return parent;
    }

    public void setParent(@Nullable Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    private void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Override
    public int compareTo(@Nonnull Category o) {
        if(name == null) { return -1; }
        if(o.name == null) { return 1; }
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Category) {
            Category otherCategory = (Category) other;
            return getId().equals(otherCategory.getId());
        } else {
            return false;
        }
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    @SuppressWarnings("unchecked")
    public static List<Category> findAll(User currentUser) {
        return (List<Category>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Category.class)
                .add(Restrictions.eq("user", currentUser))
                .addOrder(Order.asc("name"))
                .list();
    }

    public static Optional<Category> findById(Long id) {
        return Optional.ofNullable((Category) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Category.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult());
    }

    public static Optional<Category> findById(User currentUser, Long id) {
        return Optional.ofNullable((Category) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Category.class)
                .add(Restrictions.eq("id", id))
                .add(Restrictions.eq("user", currentUser))
                .uniqueResult());
    }

}
