package com.github.cstroe.spendhawk.accounting.impl;

import com.github.cstroe.spendhawk.accounting.api.Entry;
import org.apache.commons.io.IOUtils;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.internal.MoneyAmountBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class CSVEntryParserTest {
    @Test
    public void parseCapitalOneCSV() throws IOException {
        CSVEntryParser parser = new CSVEntryParser();
        final String inputFile = "/entry-samples/capitalone-one-entry.csv";
        InputStream is = getClass().getResourceAsStream(inputFile);
        assertNotNull(is);
        List<Entry> entries = parser.parse(new InputStreamReader(is, "UTF-8"));
        assertEquals(5, entries.size());
        {
            //2017-11-28,2017-11-29,1234,"FOOD, INC.",Dining,4.89,
            Entry entry = entries.get(0);
            Money money = Money.of(4.89, "USD");
            assertTrue(entry.getAmount().isEqualTo(money));
            assertEquals("FOOD, INC.", entry.getDescription());
            assertEquals(LocalDate.of(2017, 11, 28), entry.getTransactionDate());
            assertEquals(LocalDate.of(2017, 11, 29), entry.getPostedDate());
            assertTrue(entry.isDebit());
        }{
            //2017-11-04,2017-11-05,1234,CREDIT-CASH BACK REWARD,Payment/Credit,,4.70
            Entry entry = entries.get(4);
            Money money = Money.of(4.70, "USD");
            assertTrue(entry.getAmount().isEqualTo(money));
            assertEquals("CREDIT-CASH BACK REWARD", entry.getDescription());
            assertEquals(LocalDate.of(2017, 11, 4), entry.getTransactionDate());
            assertEquals(LocalDate.of(2017, 11, 5), entry.getPostedDate());
            assertTrue(entry.isCredit());
        }
    }
}