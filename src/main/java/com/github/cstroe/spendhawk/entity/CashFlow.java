package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.Optional;

/**
 * A CashFlow represents the change in value in a single account.  Value might
 * be put in to the account or might be taken out of the account.
 */
public class CashFlow {
    private Long id;
    private Transaction transaction;
    private Account account;
    private Double amount;

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

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    public static Optional<CashFlow> findById(Long id) {
        return Optional.ofNullable((CashFlow) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(CashFlow.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CashFlow) {
            if (this.getId() == null) {
                return false;
            }
            return this.getId().equals(((CashFlow) obj).getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if(id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }
}
