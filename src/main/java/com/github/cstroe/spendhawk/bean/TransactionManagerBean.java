package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Ex;

import javax.ejb.Stateful;
import java.util.*;

@Stateful
public class TransactionManagerBean extends DatabaseBean {

    private String message;

    public Optional<Transaction> createTransaction(Long userId,
            Date effectiveDate, String description, String notes,
            String[] cashflowAccounts, String[] cashflowAmounts) {
        User currentUser = User.findById(userId).orElseThrow(Ex::userNotFound);

        Transaction newT = new Transaction();
        newT.setDescription(description);
        newT.setNotes(notes);
        newT.setEffectiveDate(effectiveDate);

        if(cashflowAccounts.length != cashflowAmounts.length) {
            this.message = "The number of CashFlow accounts doesn't match the number of CashFlow amounts.";
            return Optional.empty();
        }

        newT.save();

        Set<CashFlow> cashFlows = new HashSet<>();
        for(int i = 0; i < cashflowAccounts.length; i++) {
            CashFlow newCashFlow = new CashFlow();
            Account account = Account.findById(currentUser, Long.parseLong(cashflowAccounts[i]))
                .orElseThrow(Ex::accountNotFound);
            newCashFlow.setAccount(account);
            Double amount = Double.parseDouble(cashflowAmounts[i]);
            newCashFlow.setAmount(amount);
            newCashFlow.setTransaction(newT);
            newCashFlow.save();
            cashFlows.add(newCashFlow);
        }

        newT.setCashFlows(cashFlows);
        return Optional.of(newT);
    }

    public String getMessage() {
        return message;
    }
}
