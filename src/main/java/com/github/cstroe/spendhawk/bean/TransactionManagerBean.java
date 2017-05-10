package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Ex;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionManagerBean extends DatabaseBean {

    private String message;

    public Optional<Transaction> createTransaction(Long userId,
            Date effectiveDate, String description, String notes,
            String[] cashflowAccounts, String[] cashflowAmounts) {
        User currentUser = User.findById(userId).orElseThrow(Ex::userNotFound);

        Transaction newT = new Transaction();
        newT.setNotes(notes);

        if(cashflowAccounts.length != cashflowAmounts.length) {
            this.message = "The number of CashFlow accounts doesn't match the number of CashFlow amounts.";
            return Optional.empty();
        }

        newT.save();

        Set<CashFlow> cashFlows = new HashSet<>();
        for(int i = 0; i < cashflowAccounts.length; i++) {
            if(cashflowAmounts[i] == null || cashflowAccounts[i] == null ||
                cashflowAmounts[i].isEmpty() || cashflowAccounts[i].isEmpty()) {
                continue;
            }
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
