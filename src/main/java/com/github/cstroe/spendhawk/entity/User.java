package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;

/**
 * A user has:
 * <ul>
 *     <li>A unique database id.</li>
 *     <li>A user name.  Used for identification in the system.</li>
 *     <li>Many accounts.</li>
 *     <li>Many expense categories.</li>
 * </ul>
 */
public class User {
    private Long id;
    private String name;
    private Collection<Account> accounts;
    private Collection<Category> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        this.accounts = accounts;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public static User findById(Long id) {
        return (User) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(User.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

}
