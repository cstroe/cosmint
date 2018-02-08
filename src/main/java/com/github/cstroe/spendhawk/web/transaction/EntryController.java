package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.bean.EntryService;
import com.github.cstroe.spendhawk.bean.TransactionService;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dvo.EntryDvo;
import com.github.cstroe.spendhawk.dvo.TransactionDvo;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.EntryRepository;
import com.github.cstroe.spendhawk.repository.TransactionRepository;
import com.github.cstroe.spendhawk.util.Ex;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user/{userId}/account/{accountId}/entry/{entryId}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EntryController {
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;
    private final EntryService entryService;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @GetMapping
    public String view(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @PathVariable Long entryId,
            Model model) {
        AccountDao account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(Ex::userNotFound);
        model.addAttribute("user", account.getUser());
        model.addAttribute("account", account);

        EntryDao entry = entryRepository.findByIdAndAccountId(entryId, account.getId())
                .orElseThrow(Ex::transactionNotFound);
        EntryDvo entryDvo = entryService.format(entry);

        model.addAttribute("entry", entryDvo);

        Optional<TransactionDao> transaction = transactionRepository.findByEntry(entry.getId());
        if(transaction.isPresent()) {
            TransactionDvo dvo = transactionService.toDvo(transaction.get());
            model.addAttribute("transaction", dvo);
        }

        return "entry";
    }
}