package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * A monetary transaction against accounts, implemented as a collection of
 * cash flows.
 * </p>
 *
 * A transaction has:
 * <ul>
 *     <li>an effective date and time, at which the cashflows are valid</li>
 *     <li>a description</li>
 *     <li>some notes about this transaction</li>
 *     <li>a collection of cash flows</li>
 * </ul>
 */
public class Transaction {
    private Long id;
    private Date effectiveDate;
    private String description;
    private String notes;
    private Collection<CashFlow> cashFlows;

    public Transaction() {}

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
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

    public Collection<CashFlow> getCashFlows() {
        return cashFlows;
    }

    public void setCashFlows(Collection<CashFlow> cashFlows) {
        this.cashFlows = cashFlows;
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    public static Optional<Transaction> findById(Long id) {
        return Optional.ofNullable((Transaction) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(Transaction.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult());
    }
}
