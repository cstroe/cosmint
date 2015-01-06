package com.github.cstroe.spendhawk.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest extends EntityTestSetup {

    {
        accountsTemplate = new String[] {
                // Account ID, Account Name
                "1", "Account1"
        };
        transactionsTemplate = new String[] {
                // Transaction ID, Account ID, Amount, Date ("yyMMddHHmmss"), Description
                "1", "1", "10.99", "010101000000", "Transaction 1",
                "1", "1", "11.40", "010101000000", "Transaction 2"
        };
    }

    @Test
    public void balance() {
        Account a1 = accountsList.stream().filter(a -> a.getId() == 1).findFirst().get();
        assertEquals(new Double(22.39d), a1.getBalance());
    }
}
