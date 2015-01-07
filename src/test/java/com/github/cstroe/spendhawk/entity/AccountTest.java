package com.github.cstroe.spendhawk.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    private EntityTestScenario scenario;

    @Before
    public void setUp() {
        String[] accountsTemplate = new String[] {
                // Account ID, Account Name
                "1", "Account1"
        };
        String[] transactionsTemplate = new String[] {
                // Transaction ID, Account ID, Amount, Date ("yyMMddHHmmss"), Description
                "1", "1", "10.99", "010101000000", "Transaction 1",
                "1", "1", "11.40", "010101000000", "Transaction 2"
        };
        scenario = new EntityTestScenario(accountsTemplate, transactionsTemplate);
    }

    @Test
    public void balance() {
        Account a1 = scenario.getAccountsList().stream().filter(a -> a.getId() == 1).findFirst().get();
        assertEquals(new Double(22.39d), a1.getBalance());
        assertEquals(2, a1.getTransactions().size());
    }

    @Test
    public void balanceWithoutAnyTransactions() {
        Account a1 = new Account();
        a1.setName("Test Account");
        assertEquals(new Double(0d), a1.getBalance());
    }
}
