package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;

import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    List<Transaction> parse(InputStream fileContent, Account account,
                                   Account incomeAccount, Account expenseAccount) throws Exception;
}
