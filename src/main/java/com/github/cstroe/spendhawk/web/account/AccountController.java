package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.EntryRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user/{userId}/account/{accountId}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;

    @GetMapping
    public String view(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            Model model)
    {
        AccountDao account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(Ex::userNotFound);
        model.addAttribute("user", account.getUser());
        model.addAttribute("account", account);
        List<EntryDao> entries = entryRepository.findByAccountId(account.getId());
        model.addAttribute("entries", entries);
        return "account";
    }
}
