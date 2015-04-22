package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
 *     of some date.
 *     <ul>
 *         <li>The balance is the sum of the amounts of the transactions
 *         recorded against the account up and including the given date plus
 *         the balance of its sub-accounts.  If the
 *         account has no transactions or sub-accounts, the balance will be $0.
 *         </li>
 *         <li>The balance of the sub-accounts are added to the account's
 *         balance.</li>
 *     </ul>
 *     </li>
 *     <li>a parent account</li>
 *     <li>a list of sub accounts (accounts whose parent is this account)</li>
 * </ul>
 */
public class Account implements Comparable<Account> {
    private static final Double ZERO = 0d;

    private Long id;
    private User user;
    private String name;
    private Collection<Transaction> transactions;
    private Account parent;
    private Set<Account> subAccounts;

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

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Account getParent() {
        return parent;
    }

    public void setParent(Account parent) {
        this.parent = parent;
    }

    public Set<Account> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(Set<Account> subAccounts) {
        this.subAccounts = subAccounts;
    }

    @Override
    public String toString() {
        return "Account " + name;
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

    @SuppressWarnings("unchecked")
    public static List<Account> findAll(User currentUser) {
        return (List<Account>) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(Account.class)
                .add(Restrictions.eq("user", currentUser))
            .list();
    }

    public static Optional<Account> findById(Long id) {
        return Optional.ofNullable((Account) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(Account.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult());
    }

    public static Optional<Account> findById(User currentUser, Long id) {
        return Optional.ofNullable((Account) HibernateUtil.getSessionFactory().getCurrentSession()
            .createCriteria(Account.class)
            .add(Restrictions.eq("id", id))
            .add(Restrictions.eq("user", currentUser))
            .uniqueResult());
    }

    @Override
    public int compareTo(@Nonnull Account o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Find transactions whose description match the search string.
     * @param query SQL LIKE parameter
     */
    @SuppressWarnings("unchecked")
    public List<Transaction> findTransactions(String query) {
        query = "%" + query + "%";
        return (List<Transaction>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Transaction.class)
                .add(Restrictions.eq("account", this))
                .add(Restrictions.ilike("description", query))
                .addOrder(Order.desc("effectiveDate"))
                .list();
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }
}
