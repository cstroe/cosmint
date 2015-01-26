package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * A categorized expenditure.
 */
public class Expense {
    private Long id;
    private Transaction transaction;
    private Double amount;
    private Category category;
    private String merchant;

    public Expense() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    @SuppressWarnings("unchecked")
    public static List<Expense> findByCategory(Category category) {
        return (List<Expense>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Expense.class)
                .add(Restrictions.eq("category", category))
                .list();
    }
}
