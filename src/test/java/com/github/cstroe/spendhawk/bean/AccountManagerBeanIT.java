package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class AccountManagerBeanIT extends BaseIT {

    @Inject
    private AccountManagerBean accountManager;

    @Test
    public void doSomething() {
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

}
