package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class AccountManagerBeanIT {
    @Autowired
    private AccountService accountManager;

    @Ignore
    @Test
    public void testCreateAccount() throws ServiceException {
        String accountName = "Test Created AccountDao";
        AccountDao account = accountManager.createAccount(1L, accountName);

        assertNotNull("AccountDao should be created.", account);

        UserDao currentUser = null; //UserDao.findById(1L).orElseThrow(Ex::userNotFound);
//        Optional<AccountDao> maybeRetrieved = AccountDao.findById(currentUser, account.getId());
//
//        assertTrue("AccountDao should be persisted.", maybeRetrieved.isPresent());
//
//        AccountDao retrieved = maybeRetrieved.get();
//        assertEquals("AccountDao name should be correctly persisted.", accountName, retrieved.getName());
//        assertEquals("AccountDao user should be correctly persisted.", 1L, (long)retrieved.getUser().getId());
//        assertEquals("Empty account should have 0 transactions.", 0, retrieved.getTransactions().size());
//        assertEquals("Empty account should have a 0 balance.", 0d, retrieved.getBalance(), 0.0001);
    }

    @Ignore
    @Test
    public void testCreateAccountWithParent() throws ServiceException {
        UserDao currentUser = null; //UserDao.findById(1L).orElseThrow(Ex::userNotFound);
//        AccountDao parent = AccountDao.findById(currentUser, 1)
//            .orElseThrow(() -> new AssertionError("AccountDao 1 should exist in the seed data."));
//
//        AccountDao newAccount =
//            accountManager.createAccount(1, "New AccountDao", parent.getId());
//
//        assertNotNull("AccountDao should be created.", newAccount);
//        assertThat(newAccount.getParent(), is(equalTo(parent)));
    }

//    @Test
//    public void testSeedSubAccount() {
//        UserDao currentUser = UserDao.findById(3L).orElseThrow(Ex::userNotFound);
//        AccountDao parent = AccountDao.findById(currentUser, 10L)
//            .orElseThrow(() -> new AssertionError("AccountDao 10 should exist in the seed data."));
//
//        assertThat(parent.getSubAccounts().size(), is(1));
//        assertThat(parent.getSubAccounts().iterator().next().getName(),
//            is(equalTo("MegaCorp Inc.")));
//    }
//
//    @Test
//    public void testSubAccounts() {
//        final String account1name = "Sub AccountDao 1";
//        final String account2name = "Sub AccountDao 2";
//
//        UserDao currentUser = UserDao.findById(1L).orElseThrow(Ex::userNotFound);
//        AccountDao parent = AccountDao.findById(currentUser, 1L)
//            .orElseThrow(() -> new AssertionError("AccountDao 1 should exist in the seed data."));
//
//        Optional<AccountDao> newAccountOptional1 =
//            accountManager.createAccount(1L, account1name, parent.getId());
//
//        assertTrue("AccountDao should be created.", newAccountOptional1.isPresent());
//
//        Optional<AccountDao> newAccountOptional2 =
//                accountManager.createAccount(1L, account2name, parent.getId());
//
//        assertTrue("AccountDao should be created.", newAccountOptional2.isPresent());
//
//        parent = AccountDao.findById(currentUser, 1L)
//            .orElseThrow(() -> new AssertionError("AccountDao 1 should exist in the seed data."));
//
//        assertThat(parent.getSubAccounts().size(), is(2));
//
//        Set<AccountDao> children = parent.getSubAccounts();
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
//        UserDao currentUser = UserDao.findById(3L).orElseThrow(Ex::userNotFound);
//
//        AccountDao subAccount = AccountDao.findById(currentUser, 18L)
//            .orElseThrow(Ex::accountNotFound);
//
//        assertNull("The sub account should not have a parent.", subAccount.getParent());
//
//        accountManager.nestAccount(currentUser.getId(), 11L, 18L);
//
//        AccountDao parentAccount = AccountDao.findById(currentUser, 11L).get();
//        subAccount = AccountDao.findById(currentUser, 18L).get();
//
//        assertThat(subAccount.getParent(), is(equalTo(parentAccount)));
//    }
//
//    @Test
//    public void testCreateAccountWithBlankName() {
//        String accountName = "";
//        Optional<AccountDao> account = accountManager.createAccount(1L, accountName);
//        assertFalse("AccountDao with blank name should not be created.", account.isPresent());
//        assertEquals("There should be a message when an account is not created.",
//            "AccountDao name cannot be blank.", accountManager.getMessage());
//
//        accountName = "   ";
//        account = accountManager.createAccount(1L, accountName);
//        assertFalse("AccountDao with only spaces in name should not be created.",
//            account.isPresent());
//
//        accountName = "\t\t\t";
//        account = accountManager.createAccount(1L, accountName);
//        assertFalse("AccountDao with only tabs in name should not be created.",
//            account.isPresent());
//    }
//
//    @Test
//    public void testHTMLCodeInAccountName() {
//        String accountName = "</a><a href=\"bad_place.com\">";
//        AccountDao account = accountManager.createAccount(1L, accountName).get();
//        assertNotEquals("HTML should not show up in the account name.",
//            account.getName(), accountName);
//    }
//
//    @Test
//    public void testSQLInjectionInAccountName() {
//        String accountName = "' or 1";
//        AccountDao account = accountManager.createAccount(1L, accountName).get();
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
//        UserDao currentUser = UserDao.findById(1L).orElseThrow(Ex::userNotFound);
//        AccountDao.findById(currentUser, 1L).ifPresent(a -> fail("AccountDao deletion should be persisted."));
//        TransactionDao.findById(1L).ifPresent(t -> fail("TransactionDao in the account should be deleted."));
////        Expense.findById(1L).ifPresent(t -> fail("Expenses in the transactions of the account should be deleted."));
//    }
}
