package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import lombok.Data;
import lombok.ToString;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Collection;
import java.util.Comparator;
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
@Entity
@Table(name = "accounts")
@Data
@ToString(exclude = "user")
public class Account implements Comparable<Account> {
    private static final Double ZERO = 0d;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;

    //private Collection<CashFlow> cashFlows;
    //private Account parent;
    //private Set<Account> subAccounts;

    public static Comparator<Account> HIERARCHICAL_COMPARATOR =
            Comparator.comparing(Account::getPath);

    /**
     * @return The balance of the account as of the current date and time.
     */
    public Double getBalance() {
        final Date now = new Date();
        return ZERO;
//        if(cashFlows == null || cashFlows.isEmpty()) {
//            return ZERO;
//        }
//        return cashFlows.stream()
//                .filter(c -> !c.getEffectiveDate().after(now))
//                .mapToDouble(CashFlow::getAmount)
//                .sum();
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

    public static Optional<Account> findById(User currentUser, Integer id) {
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
     * Find transactions whose description match the search string and return
     * the cashflows that are recorded on the current account.
     * @param query SQL LIKE parameter
     */
    @SuppressWarnings("unchecked")
    public List<CashFlow> findCashFlows(String query) {
        return (List<CashFlow>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(CashFlow.class)
                .add(Restrictions.eq("account", this))
                .add(Restrictions.ilike("description", query, MatchMode.ANYWHERE))
                .list();
    }

    public boolean save() {
        Long id = (Long) HibernateUtil.getSessionFactory().getCurrentSession().save(this);
        return id != null;
    }

    public void delete() {
        HibernateUtil.getSessionFactory().getCurrentSession().delete(this);
    }

    public int getDepth() {
//        if(getParent() == null) {
            return 0;
//        }
//        return 1 + getParent().getDepth();
    }

    public String getPath() {
//        if(parent != null) {
//            return parent.getPath() + getName();
//        }
        return getName();
    }


    public Account withName(String name) {
        this.setName(name);
        return this;
    }

    public Account andParent(Account parent) {
//        this.setParent(parent);
        return this;
    }

    public Account withParent(Account parent) {
//        this.setParent(parent);
        return this;
    }
}
