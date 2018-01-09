package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import org.junit.Ignore;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class TransactionManagerIT {

    @EJB
    private TransactionManagerBean tMan;

    @Ignore
    @Test
    public void create_transaction() {
        // Given:
        UserDao u3 = null; // UserDao.findById(3L).orElseThrow(Ex::userNotFound);
        AccountDao a10; // = AccountDao.findById(u3, 10).orElseThrow(Ex::accountNotFound);
        AccountDao a17; // = AccountDao.findById(u3, 17).orElseThrow(Ex::accountNotFound);

//        final Collection<CashFlow> a10FlowsBefore = a10.getCashFlows().stream()
//            .collect(Collectors.toList());

//        final Collection<CashFlow> a17FlowsBefore = a17.getCashFlows().stream()
//            .collect(Collectors.toList());

        // When:
        Optional<TransactionDao> tOpt = tMan.createTransaction(3L, new Date(),
            "New TransactionDao", "This is a note.",
            new String[] {"10", "17", "", "", ""},
            new String[] {"-1000.00", "1000.00", "", "", ""});

        assertTrue(tOpt.isPresent());

//        assertThat(tOpt.get().getDescription(), is(equalTo("New TransactionDao")));
//        assertThat(tOpt.get().getNotes(), is(equalTo("This is a note.")));

//        Collection<CashFlow> cashFlows = tOpt.get().getCashFlows();
//        assertThat(cashFlows.size(), is(2));

//        assertThat(cashFlows.stream().mapToDouble(CashFlow::getAmount).sum(), is(0d));

//        Optional<CashFlow> outbound = cashFlows.stream()
//            .filter((CashFlow c) -> c.getAmount() == -1000d).findFirst();

//        assertTrue(outbound.isPresent());
//        assertNotNull(outbound.get().getAccount());
//        assertThat(outbound.get().getAccount().getId(), is(10L));
//
//        Optional<CashFlow> inbound = cashFlows.stream()
//                .filter((CashFlow c) -> c.getAmount() == 1000d).findFirst();
//
//        assertTrue(inbound.isPresent());
//        assertNotNull(inbound.get().getAccount());
//        assertThat(inbound.get().getAccount().getId(), is(17L));

        // Then:
        u3 = null; //UserDao.findById(3L).orElseThrow(Ex::userNotFound);
//        a10 = AccountDao.findById(u3, 10).orElseThrow(Ex::accountNotFound);

//        final Collection<CashFlow> a10FlowsAfter = a10.getCashFlows();

//        assertThat("AccountDao 10 should have one new CashFlow.",
//            a10FlowsAfter.size() - a10FlowsBefore.size(), is(1));

//        List<CashFlow> newCashFlowA10 = a10FlowsAfter.stream()
//            .filter((CashFlow c) -> !a10FlowsBefore.contains(c))
//            .collect(Collectors.toList());

//        assertThat("Contains filtering on CashFlows should work", newCashFlowA10.size(), is(1));
//        assertThat(newCashFlowA10.get(0).getAmount(), is(equalTo(-1000d)));

//        a17 = AccountDao.findById(u3, 17).orElseThrow(Ex::accountNotFound);

//        final Collection<CashFlow> a17FlowsAfter = a17.getCashFlows();

//        assertThat("AccountDao 17 should have one new CashFlow.",
//            a17FlowsAfter.size() - a17FlowsBefore.size(), is(1));

//        List<CashFlow> newCashFlowA17 = a17FlowsAfter.stream()
//                .filter((CashFlow c) -> !a17FlowsBefore.contains(c))
//                .collect(Collectors.toList());

//        assertThat("Contains filtering on CashFlows should work", newCashFlowA17.size(), is(1));
//        assertThat(newCashFlowA17.get(0).getAmount(), is(equalTo(1000d)));

//        assertThat(tOpt.get().getId(), is(equalTo(newCashFlowA10.get(0).getTransaction().getId())));
//        assertThat(tOpt.get().getId(), is(equalTo(newCashFlowA17.get(0).getTransaction().getId())));
    }

}
