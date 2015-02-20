package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AccountManagerBeanIT extends BaseIT {

    @Inject
    private AccountManagerBean accountManager;

    @Test
    public void testCreateAccount() {
        String accountName = "Test Created Account";
        Optional<Account> account = accountManager.createAccount(1l, accountName);

        assertTrue("Account should be created.", account.isPresent());

        startTransaction();
        Account retrieved = Account.findById(account.get().getId());

        assertEquals("Account name should be correctly persisted.", accountName, retrieved.getName());
        assertEquals("Account user should be the same.", 1l, (long)retrieved.getUser().getId());
        assertEquals("Empty account should have 0 transactions.", 0, retrieved.getTransactions().size());
        assertEquals("Empty account should have a 0 balance.", 0d, retrieved.getBalance(), 0.0001);
        commitTransaction();
    }

    @Test
    public void testCreateAccountWithBlankName() {
        String accountName = "";
        Optional<Account> account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with blank name should not be created.", account.isPresent());

        accountName = "   ";
        account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with only spaces in name should not be created.", account.isPresent());

        accountName = "\t\t\t";
        account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with only tabs in name should not be created.", account.isPresent());
    }

    @Test
    public void testHTMLCodeInAccountName() {
        String accountName = "</a><a href=\"bad_place.com\">";
        Account account = accountManager.createAccount(1l, accountName).get();
        assertNotEquals("HTML should not show up in the account name.", account.getName(), accountName);
    }

    @Test
    public void testSQLInjectionInAccountName() {
        String accountName = "' or 1";
        Account account = accountManager.createAccount(1l, accountName).get();
        assertNotEquals("HTML should not show up in the account name.", account.getName(), accountName);
    }
}
