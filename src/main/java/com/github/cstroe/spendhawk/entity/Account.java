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

    @OneToMany
    private List<Transaction> debits;

    @OneToMany
    private List<Transaction> credits;

    public Double getBalance() {
        return ZERO;
    }

    @Override
    public int compareTo(@Nonnull Account o) {
        return this.getName().compareTo(o.getName());
    }
}
