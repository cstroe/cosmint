package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.Transaction;

/**
 * Meant to be extended by beans that need database access.
 */
public abstract class DatabaseBean {

    protected Transaction currentTransaction = null;

    protected void startTransaction() {
        if(!HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            currentTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        }
    }

    protected void commitTransaction() {
        if(currentTransaction != null && !currentTransaction.wasCommitted()) {
            currentTransaction.commit();
        }
    }

    protected void rollbackTransaction() {
        if(currentTransaction != null && currentTransaction.isActive() && !currentTransaction.wasCommitted()) {
            currentTransaction.rollback();
        }
    }
}
