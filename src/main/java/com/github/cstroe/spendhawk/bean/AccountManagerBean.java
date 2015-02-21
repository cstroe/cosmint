package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateful;
import java.util.Optional;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.apache.commons.lang3.StringEscapeUtils.escapeXml10;

@Stateful
public class AccountManagerBean extends DatabaseBean {

    private String message;

    public String getMessage() {
        return message;
    }

    public Optional<Account> createAccount(Long userId, String accountName) {
        if(StringUtils.isBlank(accountName)) {
            message = "Account name cannot be blank.";
            return Optional.empty();
        }

        accountName = escapeXml10(escapeHtml4(accountName));

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
