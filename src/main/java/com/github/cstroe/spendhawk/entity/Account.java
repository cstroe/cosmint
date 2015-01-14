package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.sun.istack.internal.NotNull;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * An account represents a pool of money, against which Transactions are
 * recorded.  Implemented as a JavaBean.
 * </p>
 *
 * An account has:
 * <ul>
 *     <li>a name</li>
 *     <li>a list of transactions recorded against the account</li>
 *     <li>a balance, which represents the value of the money in the account as
 *     of some date.  The balance is the sum of the amounts of the transactions
 *     recorded against the account up and including the given date.  If the
 *     account has no transactions, the balance will be $0.</li>
 * </ul>
 */
public class Account implements Comparable<Account> {
    private static final Double ZERO = 0d;

    private Long id;
    private User user;
    private String name;
    private Collection<Transaction> transactions;

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
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

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return The balance of the account as of the current date and time.
     */
    public Double getBalance() {
        final Date now = new Date();
        if(transactions == null || transactions.isEmpty()) {
            return ZERO;
        }
        return transactions.stream()
                .filter(t -> !t.getEffectiveDate().after(now))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static Account findById(Long id) {
        return (Account) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(Account.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult();
    }

    @Override
    public int compareTo(Account o) {
        return this.getName().compareTo(o.getName());
    }
}
