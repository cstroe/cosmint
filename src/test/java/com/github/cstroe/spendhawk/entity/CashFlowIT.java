package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.Ex;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class CashFlowIT {

    private static final Long USER_ID = 1L;

    @Test
    public void save() {
        User user = User.findById(USER_ID).orElseThrow(Ex::userNotFound);
        Account account = Account.findById(user, 1L).orElseThrow(Ex::accountNotFound);
        Transaction transaction = Transaction.findById(1L).orElseThrow(Ex::transactionNotFound);
        CashFlow cashFlow = new CashFlow();
        cashFlow.setAccount(account);
        cashFlow.setTransaction(transaction);
        cashFlow.setAmount(1d);
        boolean saveSuccess = cashFlow.save();
        assertTrue("Save should return true upon successful persistence.", saveSuccess);

        assertNotNull("CashFlow should have an ID after being persisted.", cashFlow.getId());

        CashFlow.findById(cashFlow.getId()).orElseThrow(Ex::cashFlowNotFound);
    }

    @Test
    public void delete() {
        CashFlow cashFlow = CashFlow.findById(USER_ID).orElseThrow(Ex::cashFlowNotFound);
        cashFlow.delete();

        Optional<CashFlow> cashFlow2 = CashFlow.findById(1L);
        assertFalse("CashFlow should have been deleted.", cashFlow2.isPresent());
    }
}
