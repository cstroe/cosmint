package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dvo.EntryWithAccountDvo;
import com.github.cstroe.spendhawk.dvo.TransactionDvo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {
    private final FormatService formatService;
    private final EntryService entryService;

    public TransactionDvo toDvo(TransactionDao dao) {
        TransactionDvo dvo = new TransactionDvo();
        dvo.setId(dao.getId());
        double total = dao.getEntries().stream()
                .map(EntryDao::getAmount)
                .map(Money::getNumber)
                .mapToDouble(Number::doubleValue)
                .sum();
        dvo.setTotal(formatService.format(total));

        List<EntryWithAccountDvo> entryList = dao.getEntries().stream()
                .map(entryService::formatWithAccount)
                .collect(Collectors.toList());

        dvo.setEntries(entryList);

        return dvo;
    }
}
