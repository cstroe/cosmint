package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class AccountManagerBeanIT {
    @Autowired
    private AccountService accountManager;

    @Test
    public void testCreateAccount() throws ServiceException {
        String accountName = "Test Created Account";
        Account account = accountManager.createAccount(1L, accountName);

        assertNotNull("Account should be created.", account);

        User currentUser = null; //User.findById(1L).orElseThrow(Ex::userNotFound);
//        Optional<Account> maybeRetrieved = Account.findById(currentUser, account.getId());
//
//        assertTrue("Account should be persisted.", maybeRetrieved.isPresent());
//
//        Account retrieved = maybeRetrieved.get();
//        assertEquals("Account name should be correctly persisted.", accountName, retrieved.getName());
//        assertEquals("Account user should be correctly persisted.", 1L, (long)retrieved.getUser().getId());
//        assertEquals("Empty account should have 0 transactions.", 0, retrieved.getTransactions().size());
//        assertEquals("Empty account should have a 0 balance.", 0d, retrieved.getBalance(), 0.0001);
    }

    @Test
    public void testCreateAccountWithParent() throws ServiceException {
        User currentUser = null; //User.findById(1L).orElseThrow(Ex::userNotFound);
//        Account parent = Account.findById(currentUser, 1)
//            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));
//
//        Account newAccount =
//            accountManager.createAccount(1, "New Account", parent.getId());
//
//        assertNotNull("Account should be created.", newAccount);
//        assertThat(newAccount.getParent(), is(equalTo(parent)));
    }

//    @Test
//    public void testSeedSubAccount() {
//        User currentUser = User.findById(3L).orElseThrow(Ex::userNotFound);
//        Account parent = Account.findById(currentUser, 10L)
//            .orElseThrow(() -> new AssertionError("Account 10 should exist in the seed data."));
//
//        assertThat(parent.getSubAccounts().size(), is(1));
//        assertThat(parent.getSubAccounts().iterator().next().getName(),
//            is(equalTo("MegaCorp Inc.")));
//    }
//
//    @Test
//    public void testSubAccounts() {
//        final String account1name = "Sub Account 1";
//        final String account2name = "Sub Account 2";
//
//        User currentUser = User.findById(1L).orElseThrow(Ex::userNotFound);
//        Account parent = Account.findById(currentUser, 1L)
//            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));
//
//        Optional<Account> newAccountOptional1 =
//            accountManager.createAccount(1L, account1name, parent.getId());
//
//        assertTrue("Account should be created.", newAccountOptional1.isPresent());
//
//        Optional<Account> newAccountOptional2 =
//                accountManager.createAccount(1L, account2name, parent.getId());
//
//        assertTrue("Account should be created.", newAccountOptional2.isPresent());
//
//        parent = Account.findById(currentUser, 1L)
//            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));
//
//        assertThat(parent.getSubAccounts().size(), is(2));
//
//        Set<Account> children = parent.getSubAccounts();
//
//        assertTrue(account1name + " should be presisted.",
//                children.stream().anyMatch(a -> a.getName().equals(account1name)));
//
//        assertTrue(account2name + " should be persisted.",
//                children.stream().anyMatch(a -> a.getName().equals(account2name)));
//    }
//
//    @Test
//    public void testNestAccounts() {
//        User currentUser = User.findById(3L).orElseThrow(Ex::userNotFound);
//
//        Account subAccount = Account.findById(currentUser, 18L)
//            .orElseThrow(Ex::accountNotFound);
//
//        assertNull("The sub account should not have a parent.", subAccount.getParent());
//
//        accountManager.nestAccount(currentUser.getId(), 11L, 18L);
//
//        Account parentAccount = Account.findById(currentUser, 11L).get();
//        subAccount = Account.findById(currentUser, 18L).get();
//
//        assertThat(subAccount.getParent(), is(equalTo(parentAccount)));
//    }
//
//    @Test
//    public void testCreateAccountWithBlankName() {
//        String accountName = "";
//        Optional<Account> account = accountManager.createAccount(1L, accountName);
//        assertFalse("Account with blank name should not be created.", account.isPresent());
//        assertEquals("There should be a message when an account is not created.",
//            "Account name cannot be blank.", accountManager.getMessage());
//
//        accountName = "   ";
//        account = accountManager.createAccount(1L, accountName);
//        assertFalse("Account with only spaces in name should not be created.",
//            account.isPresent());
//
//        accountName = "\t\t\t";
//        account = accountManager.createAccount(1L, accountName);
//        assertFalse("Account with only tabs in name should not be created.",
//            account.isPresent());
//    }
//
//    @Test
//    public void testHTMLCodeInAccountName() {
//        String accountName = "</a><a href=\"bad_place.com\">";
//        Account account = accountManager.createAccount(1L, accountName).get();
//        assertNotEquals("HTML should not show up in the account name.",
//            account.getName(), accountName);
//    }
//
//    @Test
//    public void testSQLInjectionInAccountName() {
//        String accountName = "' or 1";
//        Account account = accountManager.createAccount(1L, accountName).get();
//        assertFalse("HTML should not show up in the account name.",
//                account.getName().contains("'"));
//    }
//
//    @Test
//    public void testDeleteAccount() {
//        if(!accountManager.deleteAccount(1L, 1L)) {
//            fail("Should be able to delete an account. " + accountManager.getMessage());
//        }
//
//        User currentUser = User.findById(1L).orElseThrow(Ex::userNotFound);
//        Account.findById(currentUser, 1L).ifPresent(a -> fail("Account deletion should be persisted."));
//        Transaction.findById(1L).ifPresent(t -> fail("Transaction in the account should be deleted."));
////        Expense.findById(1L).ifPresent(t -> fail("Expenses in the transactions of the account should be deleted."));
//    }
}
