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
import java.util.Optional;

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

    public List<Transaction> parse(InputStream fileContent, Account account,
           Account incomeAccount, Account expenseAccount) {
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
                processRecord(record, account, incomeAccount, expenseAccount)
                    .ifPresent(generatedTransactions::add);
            }
        } catch(IOException exception) {
            log.error("Exception while parsing Chase CSV file.", exception);
        }
        return generatedTransactions;
    }

    private Optional<Transaction> processRecord(CSVRecord record,
            Account bankAccount, Account incomeAccount, Account expenseAccount) {
        try {
            final String flow = record.get(ROW_TYPE); // CREDIT or DEBIT


            Transaction newTransaction = new Transaction();

            newTransaction.setDescription(record.get(ROW_DESCRIPTION));
            newTransaction.setNotes(record.get(ROW_NOTES));
            newTransaction.setEffectiveDate(dateBean.parse(record.get(ROW_DATE)));


            Double amount = Double.parseDouble(record.get(ROW_AMOUNT));

            if ("CREDIT".equals(flow)) {
                if (amount < 0d) {
                    log.warn("Amount is negative when expected to be positive for CREDIT.");
                    amount = amount * -1;
                }
                // in flow
                CashFlow cf_in = new CashFlow();
                cf_in.setTransaction(newTransaction);
                cf_in.setAmount(amount);
                cf_in.setAccount(bankAccount);
                newTransaction.getCashFlows().add(cf_in);

                // out flow
                CashFlow cf_out = new CashFlow();
                cf_out.setTransaction(newTransaction);
                cf_out.setAmount(amount * -1);
                cf_out.setAccount(incomeAccount);
                newTransaction.getCashFlows().add(cf_out);
            } else if ("DEBIT".equals(flow)) {
                if (amount > 0d) {
                    log.warn("Amount is positive when expected to be negative for DEBIT.");
                    amount = amount * -1;
                }

                // out flow
                CashFlow cf_out = new CashFlow();
                cf_out.setTransaction(newTransaction);
                cf_out.setAmount(amount);
                cf_out.setAccount(bankAccount);
                newTransaction.getCashFlows().add(cf_out);

                // in flow
                CashFlow cf_in = new CashFlow();
                cf_in.setTransaction(newTransaction);
                cf_in.setAmount(amount * -1);
                cf_in.setAccount(expenseAccount);
                newTransaction.getCashFlows().add(cf_in);
            }

            return Optional.of(newTransaction);
        } catch(Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
