package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BulkUpdateBean {

    /**
     * Given a list of cashflows, return all accounts associated with their
     * transactions.  Don't return duplicates.
     */
    public Collection<Account> getAccounts(List<CashFlow> cashFlows) {
        return cashFlows.stream()
            .map(CashFlow::getTransaction)
            .map(Transaction::getCashFlows)
            .flatMap(Collection::stream)
            .map(CashFlow::getAccount)
            .collect(Collectors.toSet());
    }

    public Collection<CashFlow> previewReplace(List<CashFlow> cfList, Account toReplace, Account replacement) {
        List<CashFlow> newList = new LinkedList<>();
        for(CashFlow cf : cfList) {
            Collection<CashFlow> allCF = cf.getTransaction().getCashFlows();
            for(CashFlow currentCF: allCF) {
                if(currentCF.getAccount().equals(toReplace)) {
                    currentCF.setAccount(replacement);
                    newList.add(cf);
                }
            }
        }

        return newList;
    }

}
