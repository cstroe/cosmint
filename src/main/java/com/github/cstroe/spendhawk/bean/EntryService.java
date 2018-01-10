package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.api.Entry;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dvo.EntryDvo;
import com.github.cstroe.spendhawk.impl.CSVEntryParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EntryService {
    private final FormatService formatService;
    private final CSVEntryParser parser;

    public List<Entry> readFromFile(MultipartFile file) {
        checkNotNull(file);

        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            return parser.parse(reader);
        } catch(FileNotFoundException ex) {
            throw new IllegalArgumentException("File not found", ex);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Money computeTotal(List<? extends Entry> entries) {
        return entries.stream()
                .map(Entry::getAmount)
                .reduce(Money.of(0d, "USD"), Money::add);
    }

    public EntryDvo format(EntryDao dao) {
        return new EntryDvo(
                dao.getId(),
                formatService.format(dao.getAmount()),
                dao.getTransactionDate().toString(),
                dao.getPostedDate().toString(),
                dao.getDescription()
        );
    }
}
