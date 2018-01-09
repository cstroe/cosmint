package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;

import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    List<TransactionDao> parse(InputStream fileContent, AccountDao account,
                               AccountDao incomeAccount, AccountDao expenseAccount) throws Exception;
}
