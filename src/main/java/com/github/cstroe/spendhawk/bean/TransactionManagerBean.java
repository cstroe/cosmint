package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.TransactionDao;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TransactionManagerBean extends DatabaseBean {

    private String message;

    public Optional<TransactionDao> createTransaction(Long userId,
                                                      Date effectiveDate, String description, String notes,
                                                      String[] cashflowAccounts, String[] cashflowAmounts) {
//        UserDao currentUser = null; //UserDao.findById(userId).orElseThrow(Ex::userNotFound);
//
//        TransactionDao newT = new TransactionDao();
//        newT.setNotes(notes);
//
//        if(cashflowAccounts.length != cashflowAmounts.length) {
//            this.message = "The number of CashFlow accounts doesn't match the number of CashFlow amounts.";
//            return Optional.empty();
//        }
//
//        newT.save();
//
//        Set<CashFlow> cashFlows = new HashSet<>();
//        for(int i = 0; i < cashflowAccounts.length; i++) {
//            if(cashflowAmounts[i] == null || cashflowAccounts[i] == null ||
//                cashflowAmounts[i].isEmpty() || cashflowAccounts[i].isEmpty()) {
//                continue;
//            }
//            CashFlow newCashFlow = new CashFlow();
//            AccountDao account = AccountDao.findById(currentUser, Long.parseLong(cashflowAccounts[i]))
//                .orElseThrow(Ex::accountNotFound);
//            newCashFlow.setAccount(account);
//            Double amount = Double.parseDouble(cashflowAmounts[i]);
//            newCashFlow.setAmount(amount);
//            newCashFlow.setTransaction(newT);
//            newCashFlow.save();
//            cashFlows.add(newCashFlow);
//        }
//
//        newT.setCashFlows(cashFlows);
//        return Optional.of(newT);
        return Optional.empty();
    }

    public String getMessage() {
        return message;
    }
}
