package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Ex;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class TransactionManagerIT extends BaseIT {

    @EJB
    private TransactionManagerBean tMan;

    @Test
    public void create_transaction() {
        // Given:
        startTransaction();
        User u3 = User.findById(3l).orElseThrow(Ex::userNotFound);
        Account a10 = Account.findById(u3, 10l).orElseThrow(Ex::accountNotFound);
        Account a17 = Account.findById(u3, 17l).orElseThrow(Ex::accountNotFound);

        final Collection<CashFlow> a10FlowsBefore = a10.getCashFlows().stream()
            .collect(Collectors.toList());

        final Collection<CashFlow> a17FlowsBefore = a17.getCashFlows().stream()
            .collect(Collectors.toList());

        commitTransaction();

        // When:
        startTransaction();
        Optional<Transaction> tOpt = tMan.createTransaction(3l, new Date(),
            "New Transaction", "This is a note.",
            new String[] {"10", "17"},
            new String[] {"-1000.00", "1000.00"});

        assertTrue(tOpt.isPresent());

        assertThat(tOpt.get().getDescription(), is(equalTo("New Transaction")));
        assertThat(tOpt.get().getNotes(), is(equalTo("This is a note.")));

        Collection<CashFlow> cashFlows = tOpt.get().getCashFlows();
        assertThat(cashFlows.size(), is(2));

        assertThat(cashFlows.stream().mapToDouble(CashFlow::getAmount).sum(), is(0d));

        Optional<CashFlow> outbound = cashFlows.stream()
            .filter((CashFlow c) -> c.getAmount() == -1000d).findFirst();

        assertTrue(outbound.isPresent());
        assertNotNull(outbound.get().getAccount());
        assertThat(outbound.get().getAccount().getId(), is(10l));

        Optional<CashFlow> inbound = cashFlows.stream()
                .filter((CashFlow c) -> c.getAmount() == 1000d).findFirst();

        assertTrue(inbound.isPresent());
        assertNotNull(inbound.get().getAccount());
        assertThat(inbound.get().getAccount().getId(), is(17l));

        commitTransaction();

        // Then:
        startTransaction();

        u3 = User.findById(3l).orElseThrow(Ex::userNotFound);
        a10 = Account.findById(u3, 10l).orElseThrow(Ex::accountNotFound);

        final Collection<CashFlow> a10FlowsAfter = a10.getCashFlows();

        assertThat("Account 10 should have one new CashFlow.",
            a10FlowsAfter.size() - a10FlowsBefore.size(), is(1));

        List<CashFlow> newCashFlowA10 = a10FlowsAfter.stream()
            .filter((CashFlow c) -> !a10FlowsBefore.contains(c))
            .collect(Collectors.toList());

        assertThat("Contains filtering on CashFlows should work", newCashFlowA10.size(), is(1));
        assertThat(newCashFlowA10.get(0).getAmount(), is(equalTo(-1000d)));

        a17 = Account.findById(u3, 17l).orElseThrow(Ex::accountNotFound);

        final Collection<CashFlow> a17FlowsAfter = a17.getCashFlows();

        assertThat("Account 17 should have one new CashFlow.",
            a17FlowsAfter.size() - a17FlowsBefore.size(), is(1));

        List<CashFlow> newCashFlowA17 = a17FlowsAfter.stream()
                .filter((CashFlow c) -> !a17FlowsBefore.contains(c))
                .collect(Collectors.toList());

        assertThat("Contains filtering on CashFlows should work", newCashFlowA17.size(), is(1));
        assertThat(newCashFlowA17.get(0).getAmount(), is(equalTo(1000d)));

        assertThat(tOpt.get().getId(), is(equalTo(newCashFlowA10.get(0).getTransaction().getId())));
        assertThat(tOpt.get().getId(), is(equalTo(newCashFlowA17.get(0).getTransaction().getId())));
        commitTransaction();
    }

}
