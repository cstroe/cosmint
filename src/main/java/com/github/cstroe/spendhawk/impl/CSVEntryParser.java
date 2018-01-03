package com.github.cstroe.spendhawk.impl;

import com.github.cstroe.spendhawk.api.Entry;
import com.github.cstroe.spendhawk.api.EntryParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVEntryParser implements EntryParser {
    @Override
    public List<Entry> parse(Reader reader) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .parse(reader);

        List<Entry> entries = new ArrayList<>();
        for(CSVRecord record : records) {
            AbstractEntry entry;
            if(!StringUtils.isBlank(record.get("Debit"))) {
                String rawDebit = record.get("Debit");
                entry = new Debit();
                entry.setAmount(Money.of(Double.parseDouble(rawDebit), "USD"));
            } else if(!StringUtils.isBlank(record.get("Credit"))) {
                String rawCredit = record.get("Credit");
                entry = new Credit();
                entry.setAmount(Money.of(Double.parseDouble(rawCredit), "USD"));
            } else {
                throw new IllegalArgumentException("Both 'Credit' and 'Debit' fields are blank.");
            }

            entry.setDescription(record.get("Description"));
            entry.setTransactionDate(LocalDate.parse(record.get("Transaction Date")));
            entry.setPostedDate(LocalDate.parse(record.get("Posted Date")));
            entries.add(entry);
        }
        return entries;
    }
}
