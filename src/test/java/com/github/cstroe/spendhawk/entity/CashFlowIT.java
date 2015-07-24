package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Ex;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CashFlowIT extends BaseIT {

    private static final Long USER_ID = 1l;

    @Test
    public void save() {
        startTransaction();
        User user = User.findById(USER_ID).orElseThrow(Ex::userNotFound);
        Account account = Account.findById(user, 1l).orElseThrow(Ex::accountNotFound);
        Transaction transaction = Transaction.findById(1l).orElseThrow(Ex::transactionNotFound);
        CashFlow cashFlow = new CashFlow();
        cashFlow.setAccount(account);
        cashFlow.setTransaction(transaction);
        cashFlow.setAmount(1d);
        boolean saveSuccess = cashFlow.save();
        assertTrue("Save should return true upon successful persistence.", saveSuccess);
        commitTransaction();

        assertNotNull("CashFlow should have an ID after being persisted.", cashFlow.getId());

        startTransaction();
        CashFlow.findById(cashFlow.getId()).orElseThrow(Ex::cashFlowNotFound);
        commitTransaction();
    }

    @Test
    public void delete() {
        startTransaction();
        CashFlow cashFlow = CashFlow.findById(USER_ID).orElseThrow(Ex::cashFlowNotFound);
        cashFlow.delete();
        commitTransaction();

        startTransaction();
        Optional<CashFlow> cashFlow2 = CashFlow.findById(1l);
        assertFalse("CashFlow should have been deleted.", cashFlow2.isPresent());
    }
}
