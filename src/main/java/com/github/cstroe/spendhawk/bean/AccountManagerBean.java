package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Operations on accounts.
 */
@Stateful
public class AccountManagerBean extends DatabaseBean {

    private String message;

    @Inject
    private JanitorBean janitor;

    public String getMessage() {
        return message;
    }

    public Optional<Account> createAccount(Long userId, String accountName) {
        if(janitor.isBlank(accountName)) {
            message = "Account name cannot be blank.";
            return Optional.empty();
        }

        accountName = janitor.sanitize(accountName);

        try {
            startTransaction();

            User currentUser = User.findById(userId)
                .orElseThrow(Exceptions::userNotFound);

            Account theAccount = new Account();
            theAccount.setName(accountName);
            theAccount.setUser(currentUser);

            HibernateUtil.getSessionFactory().getCurrentSession().save(theAccount);
            commitTransaction();

            return Optional.of(theAccount);
        } catch(Exception ex) {
            rollbackTransaction();
            message = ex.getMessage();
            return Optional.empty();
        }
    }

    public boolean deleteAccount(Long userId, Long accountId) {
        try {
            startTransaction();

            User currentUser = User.findById(userId)
                .orElseThrow(Exceptions::userNotFound);

            Account account = currentUser.getAccounts().stream()
                .filter(a->a.getId().equals(accountId)).findFirst()
                .orElseThrow(Exceptions::accountNotFound);

            // I have a feeling that this can be handled by hibernate.
            account.getTransactions().stream().forEach(t -> {
                t.getExpenses().stream().forEach(Expense::delete);
                t.delete();
            });

            currentUser.getAccounts().remove(account);
            account.delete();
            commitTransaction();
            return true;
        } catch(Exception ex) {
            rollbackTransaction();
            message = Exceptions.getDescriptiveMessage(ex);
            return false;
        }
    }
}
