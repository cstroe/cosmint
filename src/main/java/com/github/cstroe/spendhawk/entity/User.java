package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Optional;

/**
 * A user has:
 * <ul>
 *     <li>A unique database id.</li>
 *     <li>A user name.  Used for identification in the system.</li>
 *     <li>Many accounts.</li>
 *     <li>Many expense categories.</li>
 * </ul>
 */
public class User {

    public static final String DEFAULT_INCOME_ACCOUNT_NAME = "Income";
    public static final String DEFAULT_EXPENSE_ACCOUNT_NAME = "Expenses";
    public static final String DEFAULT_ASSET_ACCOUNT_NAME = "Assets";

    private Long id;
    private String name;
    private Collection<Account> accounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        this.accounts = accounts;
    }

    public Optional<Account> getAccountByName(String name) {
        return getAccountByName(name, false);
    }

    private Optional<Account> getAccountByName(String name, boolean createIfNotExists) {
        Optional<Account> accountOptional = this.accounts.stream()
            .filter(a -> a.getName().equals(name))
            .findFirst();

        if(createIfNotExists && !accountOptional.isPresent()) {
            Account newAccount = new Account();
            newAccount.setName(name);
            newAccount.setUser(this);
            newAccount.save();
            return Optional.of(newAccount);
        } else {
            return accountOptional;
        }
    }

    public Account getDefaultExpenseAccount() {
        return getAccountByName(DEFAULT_EXPENSE_ACCOUNT_NAME, true).get();
    }

    public Account getDefaultIncomeAccount() {
        return getAccountByName(DEFAULT_INCOME_ACCOUNT_NAME, true).get();
    }

    public Account getDefaultAssetAccount() {
        return getAccountByName(DEFAULT_ASSET_ACCOUNT_NAME, true).get();
    }

    public static Optional<User> findById(Long id) {
        return Optional.ofNullable((User) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(User.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult());
    }

}
