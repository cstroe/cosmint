package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse CSV files downloaded from chase.com
 */
@Service
public class ChaseCSVParser implements TransactionParser {

    private final static String ROW_TYPE = "Type";
    private final static String ROW_DATE = "Post Date";
    private final static String ROW_DESCRIPTION = "Description";
    private final static String ROW_AMOUNT = "Amount";
    private final static String ROW_NOTES = "Check or Slip #";

    private final DateBean dateBean;

    private static final Logger log = Logger.getLogger(ChaseCSVParser.class);

    @Autowired
    public ChaseCSVParser(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    /**
     * @throws Exception thrown when invalid data exists in the fileContent
     */
    @Override
    public List<TransactionDao> parse(InputStream fileContent, AccountDao account,
                                      AccountDao incomeAccount, AccountDao expenseAccount) throws Exception {
        List<TransactionDao> generatedTransactions = new LinkedList<>();
        try (InputStreamReader reader = new InputStreamReader(fileContent)){
            Iterable<CSVRecord> records = CSVFormat.newFormat(',')
                .withQuote('"')
                .withHeader(ROW_TYPE, ROW_DATE, ROW_DESCRIPTION, ROW_AMOUNT, ROW_NOTES)
                .withSkipHeaderRecord(true)
                .parse(reader);
            for(CSVRecord record : records) {
                if(!record.isConsistent()) {
                    continue;
                }
                generatedTransactions.add(processRecord(record, account, incomeAccount, expenseAccount));
            }
        } catch(IOException exception) {
            log.error("Exception while parsing Chase CSV file.", exception);
        }
        return generatedTransactions;
    }

    private TransactionDao processRecord(CSVRecord record, AccountDao bankAccount,
                                         AccountDao incomeAccount, AccountDao expenseAccount) throws Exception {

        final String flow = record.get(ROW_TYPE); // CREDIT or DEBIT


        TransactionDao newTransaction = new TransactionDao();

//        newTransaction.setDescription(record.get(ROW_DESCRIPTION));
//        newTransaction.setNotes(record.get(ROW_NOTES));
//        newTransaction.setEffectiveDate(dateBean.parse(record.get(ROW_DATE)));


        // see: http://stackoverflow.com/questions/6724031/how-can-a-primitive-float-value-be-0-0-what-does-that-mean/8153449#8153449
        Double amount = Double.parseDouble(record.get(ROW_AMOUNT)) + 0.0d;

        if(amount.compareTo(0d) == 0) {
            throw new Exception("TransactionDao amount is zero.");
        }

        if("CREDIT".equals(flow) || "DSLIP".equals(flow)) {
            if (amount < 0d) {
                throw new Exception("CREDIT transactions must have positive amount.");
            }

            //createCashFlows(newTransaction, incomeAccount, bankAccount, amount);
        } else if("DEBIT".equals(flow)) {
            if (amount > 0d) {
                throw new Exception("DEBIT transactions must have negative amount.");
            }

            // Remember: amount is negative, so the outflow account must be the expenseAccount
            //createCashFlows(newTransaction, expenseAccount, bankAccount, amount);
        } else if("CHECK".equals(flow)) {
            if(amount.compareTo(0d) < 0 ) {
                // DEBIT
                //createCashFlows(newTransaction, expenseAccount, bankAccount, amount);
            } else {
                // CREDIT
                //createCashFlows(newTransaction, incomeAccount, bankAccount, amount);
            }
        } else {
            throw new Exception("Invalid type of transaction: " + flow);
        }

        return newTransaction;
    }
}
