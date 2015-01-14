package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * A monetary transaction against an account.  Implemented as a JavaBean.
 * </p>
 *
 * A transaction has:
 * <ul>
 *     <li>an amount, can be positive or negative, two decimal places of precision</li>
 *     <li>an effective date and time, at which the expense is applied to an account</li>
 *     <li>an account against which this transaction is executed</li>
 *     <li>a description</li>
 *     <li>some notes about this description</li>
 * </ul>
 */
public class Transaction {
    private Long id;
    private Account account;
    private Double amount;
    private Date effectiveDate;
    private String description;
    private String notes;
    private Collection<CategorySpend> categorySpends;

    public Transaction() {}

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Collection<CategorySpend> getCategorySpends() {
        return categorySpends;
    }

    public void setCategorySpends(Collection<CategorySpend> categorySpends) {
        this.categorySpends = categorySpends;
    }

    public boolean isDuplicate() {
        Transaction t = (Transaction) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Transaction.class)
                .add(Restrictions.eq("effectiveDate", effectiveDate))
                .add(Restrictions.eq("amount", amount))
                .add(Restrictions.eq("account", account))
                .add(Restrictions.eq("description", description))
                .uniqueResult();
        return t != null;
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    public static Transaction findById(Long id) {
        return (Transaction) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Transaction.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
