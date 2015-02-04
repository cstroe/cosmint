package com.github.cstroe.spendhawk.helper;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * The transactions helper parses CSV files of transactions.
 *
 * Currently supported formats are:
 * <ul>
 *     <li>Chase: (Type,Date,Description,Amount,Check or Slip #)</li>
 *     <li>Capital One: (Date,Account No.,Description,Debit Amount,Credit Amount)</li>
 * </ul>
 *
 * Each of these is mapped to our {@link com.github.cstroe.spendhawk.entity.Transaction Transaction} class.
 *
 * Also, creates expenses for a set of transactions.
 */
public class TransactionsHelper {

    public static final String CHASE = "chase";
    public static final String CAPITAL_ONE = "capone";

    /**
     * @return A list of messages.
     */
    public static List<String> processFile(InputStream fileContent, Account account, boolean duplicateCheck, String format) {
        if(CHASE.equals(format)) {
            return processChaseFile(fileContent, account, duplicateCheck);
        }
        if(CAPITAL_ONE.equals(format)) {
            return processCapitalOneFile(fileContent, account, duplicateCheck);
        }

        List<String> newList = new LinkedList<>();
        newList.add("Could not find format: " + format);
        return newList;
    }


    public static List<String> processChaseFile(InputStream fileContent, Account account, boolean duplicateCheck) {
        List<String> messages = new LinkedList<>();
        try (InputStreamReader reader = new InputStreamReader(fileContent)){
            Iterable<CSVRecord> records = CSVFormat.newFormat(',')
                    .withDelimiter(',')
                    .withQuote('"')
                    .withHeader("Type", "Post Date", "Description", "Amount", "Check or Slip #")
                    .parse(reader);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            for(CSVRecord record : records) {
                if(!record.isConsistent()) {
                    continue;
                }
                try {
                    Transaction newTransaction = new Transaction();
                    newTransaction.setAccount(account);
                    newTransaction.setAmount(Double.parseDouble(record.get("Amount")));
                    newTransaction.setEffectiveDate(dateFormatter.parse(record.get("Post Date")));
                    newTransaction.setDescription(record.get("Description"));
                    newTransaction.setNotes(record.get("Check or Slip #"));

                    if(duplicateCheck && newTransaction.isDuplicate()) {
                        messages.add(newTransaction.getDescription() + " is a duplicate");
                        continue;
                    }

                    if (!newTransaction.save()) {
                        messages.add("Something went wrong with saving transaction");
                    } else {
                        messages.add("New transaction was added.");
                    }
                } catch(NumberFormatException nfe) {
                    messages.add(nfe.getMessage() + "\n" + record.toString());
                }
            }
        } catch(IOException | ParseException exception) {
            messages.add(exception.getMessage());
        }

        return messages;
    }

    public static List<String> processCapitalOneFile(InputStream fileContent, Account account, boolean duplicateCheck) {
        List<String> messages = new LinkedList<>();
        try (InputStreamReader reader = new InputStreamReader(fileContent)){
            Iterable<CSVRecord> records = CSVFormat.newFormat(',')
                    .withDelimiter(',')
                    .withQuote('"')
                    .withHeader("Date", "No.", "Description", "Debit", "Credit")
                    .parse(reader);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            for(CSVRecord record : records) {
                if(!record.isConsistent()) {
                    messages.add("Inconsistent record: " + record.toString());
                    continue;
                }
                try {
                    Transaction newTransaction = new Transaction();
                    newTransaction.setAccount(account);


                    String debit = record.get("Debit");
                    String credit = record.get("Credit");
                    Double amount = null;
                    if(!debit.isEmpty()) {
                        amount = Double.parseDouble(debit);
                        amount *= -1.0;
                    }
                    if(!credit.isEmpty()) {
                        amount = Double.parseDouble(credit);
                    }

                    newTransaction.setAmount(amount);
                    newTransaction.setEffectiveDate(dateFormatter.parse(record.get("Date")));
                    newTransaction.setDescription(record.get("Description"));

                    if(duplicateCheck && newTransaction.isDuplicate()) {
                        messages.add(newTransaction.getDescription() + " is a duplicate");
                        continue;
                    }

                    if (!newTransaction.save()) {
                        messages.add("Something went wrong with saving transaction");
                    } else {
                        messages.add("New transaction was added.");
                    }
                } catch(NumberFormatException nfe) {
                    messages.add(nfe.getMessage() + "\n" + record.toString());
                }
            }
        } catch(IOException | ParseException exception) {
            messages.add(exception.getMessage());
        }

        return messages;
    }

    /**
     * Creates expenses for the given transactions with the given category.  The
     * expense amount will be the entire amount of the Transaction.
     *
     * @param duplicateCheck If true, checks to see if an expense with the given
     *                       category already exists.  If the expense exists, it
     *                       will not enter another expense with the same category.
     */
    public static List<Transaction> createExpenses(List<Transaction> transactions, Category category, String merchant, boolean duplicateCheck, boolean persist) {
        for(Transaction currentTransaction : transactions) {
            boolean expenseExists = false;
            if(duplicateCheck) {
                for (Expense currentExpense : currentTransaction.getExpenses()) {
                    if (currentExpense.getCategory().getId().equals(category.getId())) {
                        expenseExists = true;
                        break;
                    }
                }
            }

            if(expenseExists) {
                continue;
            }

            Expense newExpense = new Expense();
            newExpense.setAmount(currentTransaction.getAmount());
            newExpense.setCategory(category);
            newExpense.setTransaction(currentTransaction);
            newExpense.setMerchant(merchant);
            if(persist) {
                newExpense.save();
            } else {
                // just for preview purposes
                currentTransaction.getExpenses().add(newExpense);
            }
        }

        return transactions;
    }
}