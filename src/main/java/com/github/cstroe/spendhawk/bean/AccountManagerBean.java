package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.List;
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
        return createAccount(userId, accountName, Optional.<Long>empty());
    }

    public Optional<Account> createAccount(Long userId, String accountName, Optional<Long> parentId) {
        if(janitor.isBlank(accountName)) {
            message = "Account name cannot be blank.";
            return Optional.empty();
        }

        accountName = janitor.sanitize(accountName);

        try {
            startTransaction();

            final User currentUser = User.findById(userId)
                .orElseThrow(Exceptions::userNotFound);

            final Account theAccount = new Account();
            theAccount.setName(accountName);
            theAccount.setUser(currentUser);

            parentId.ifPresent((pId) -> {
                Account parentAccount = Account.findById(currentUser, pId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent account id is not valid."));
                theAccount.setParent(parentAccount);
            });

            HibernateUtil.getSessionFactory().getCurrentSession().save(theAccount);
            commitTransaction();

            return Optional.of(theAccount);
        } catch(Exception ex) {
            rollbackTransaction();
            message = ex.getMessage();
            return Optional.empty();
        }
    }

    public Optional<Account> nestAccount(Long userId, Long parentAccountId, Long subAccountId) {
        try {
            startTransaction();

            User currentUser = User.findById(userId)
                .orElseThrow(Exceptions::userNotFound);

            final Account parentAccount = Account.findById(currentUser, parentAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Parent account id is not valid."));

            final Account subAccount = Account.findById(currentUser, subAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Sub account id is not valid."));

            subAccount.setParent(parentAccount);

            HibernateUtil.getSessionFactory().getCurrentSession().save(subAccount);
            commitTransaction();

            return Optional.of(subAccount);
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

    public boolean convertCategoriesToAccounts(Long userId) {
        try {
            startTransaction();
            User currentUser = User.findById(userId).orElseThrow(Exceptions::userNotFound);
            List<Category> categories = Category.findAll(currentUser);
            List<Account> accounts = Account.findAll(currentUser);
            Account expensesAccount = accounts.stream()
                .filter(a -> a.getName().equals("Expenses")).findFirst().orElse(null);
            for(Category category : categories) {
                boolean accountExists = false;
                for(Account account : accounts) {
                    if(account.getName().equals(category.getName())) {
                        accountExists = true;
                        break;
                    }
                }
                if(!accountExists) {
                    Account newAccount = new Account()
                        .withName(category.getName()).andParent(expensesAccount);
                    newAccount.setUser(currentUser);
                    newAccount.save();
                }
            }
            commitTransaction();
            return true;
        } catch(Exception ex) {
            rollbackTransaction();
            message = Exceptions.getDescriptiveMessage(ex);
            return false;
        }
    }
}
