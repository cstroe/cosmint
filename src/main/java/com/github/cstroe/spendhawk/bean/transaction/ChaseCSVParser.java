package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

import javax.ejb.Stateful;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse CSV files downloaded from chase.com
 */
@Stateful
public class ChaseCSVParser implements TransactionParser {

    private final static String ROW_TYPE = "Type";
    private final static String ROW_DATE = "Post Date";
    private final static String ROW_DESCRIPTION = "Description";
    private final static String ROW_AMOUNT = "Amount";
    private final static String ROW_NOTES = "Check or Slip #";

    private final DateBean dateBean;

    private static final Logger log = Logger.getLogger(ChaseCSVParser.class);

    @Inject
    public ChaseCSVParser(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    /**
     * @throws Exception thrown when invalid data exists in the fileContent
     */
    public List<Transaction> parse(InputStream fileContent, Account account,
           Account incomeAccount, Account expenseAccount) throws Exception {
        List<Transaction> generatedTransactions = new LinkedList<>();
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

    private Transaction processRecord(CSVRecord record, Account bankAccount,
                                      Account incomeAccount, Account expenseAccount) throws Exception {

        final String flow = record.get(ROW_TYPE); // CREDIT or DEBIT


        Transaction newTransaction = new Transaction();

        newTransaction.setDescription(record.get(ROW_DESCRIPTION));
        newTransaction.setNotes(record.get(ROW_NOTES));
        newTransaction.setEffectiveDate(dateBean.parse(record.get(ROW_DATE)));


        // see: http://stackoverflow.com/questions/6724031/how-can-a-primitive-float-value-be-0-0-what-does-that-mean/8153449#8153449
        Double amount = Double.parseDouble(record.get(ROW_AMOUNT)) + 0.0d;

        if(amount.compareTo(0d) == 0) {
            throw new Exception("Transaction amount is zero.");
        }

        if("CREDIT".equals(flow) || "DSLIP".equals(flow)) {
            if (amount < 0d) {
                throw new Exception("CREDIT transactions must have positive amount.");
            }

            createCashFlows(newTransaction, incomeAccount, bankAccount, amount);
        } else if("DEBIT".equals(flow)) {
            if (amount > 0d) {
                throw new Exception("DEBIT transactions must have negative amount.");
            }

            // Remember: amount is negative, so the outflow account must be the expenseAccount
            createCashFlows(newTransaction, expenseAccount, bankAccount, amount);
        } else if("CHECK".equals(flow)) {
            if(amount.compareTo(0d) < 0 ) {
                // DEBIT
                createCashFlows(newTransaction, expenseAccount, bankAccount, amount);
            } else {
                // CREDIT
                createCashFlows(newTransaction, incomeAccount, bankAccount, amount);
            }
        } else {
            throw new Exception("Invalid type of transaction: " + flow);
        }

        return newTransaction;
    }

    /**
     * The amountToTransfer is taken out of the outFlowAccount and added to the inFlowAccount.
     * Therefore, the outFlowAccount gets a CashFlow with -1 * amountToTransfer.
     */
    private void createCashFlows(Transaction t, Account outFlowAccount, Account inFlowAccount, Double amountToTransfer) {
        // out flow
        CashFlow cf_out = new CashFlow();
        cf_out.setTransaction(t);
        cf_out.setAmount(amountToTransfer * -1);
        cf_out.setAccount(outFlowAccount);
        t.getCashFlows().add(cf_out);


        // in flow
        CashFlow cf_in = new CashFlow();
        cf_in.setTransaction(t);
        cf_in.setAmount(amountToTransfer);
        cf_in.setAccount(inFlowAccount);
        t.getCashFlows().add(cf_in);
    }
}
