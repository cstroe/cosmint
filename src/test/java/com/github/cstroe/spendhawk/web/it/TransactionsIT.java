package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests having to do with transactions.
 */
@RunWith(Arquillian.class)
public class TransactionsIT extends BaseIT {

    @Test
    @InSequence(100)
    public void seedDatabaseWorks() {
        startTransaction();
        List<Account> accounts = Account.findAll();
        assertEquals(1, accounts.size());
        Account firstAccount = accounts.get(0);
        assertEquals("Main Checking", firstAccount.getName());
        commitTransaction();
    }

    @Test
    @InSequence(200)
    public void seedDatabaseWorks2() {
        startTransaction();
        List<Account> accounts = Account.findAll();
        assertEquals(1, accounts.size());
        Account firstAccount = accounts.get(0);
        assertEquals("Main Checking", firstAccount.getName());
        commitTransaction();
    }
}
