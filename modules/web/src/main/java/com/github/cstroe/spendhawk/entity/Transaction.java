package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * <p>
 * A monetary transaction against accounts, implemented as a collection of
 * cash flows.
 * </p>
 *
 * A transaction has:
 * <ul>
 *     <li>some notes about this transaction</li>
 *     <li>a collection of cash flows</li>
 * </ul>
 */
public class Transaction {
    private Long id;
    private String notes;
    private Collection<CashFlow> cashFlows = new HashSet<>();

    public Transaction() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
