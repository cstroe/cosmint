package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.bean.EntryService;
import com.github.cstroe.spendhawk.bean.FormatService;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dvo.EntryDvo;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.EntryRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/account/{accountId}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;
    private final EntryService entryService;
    private final FormatService formatService;

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

        List<EntryDao> daoList = entryRepository.findByAccountId(account.getId());
        List<EntryDvo> dvoList = entryService.convert(daoList);

        model.addAttribute("entries", dvoList);
        Money total = entryService.computeTotal(daoList);
        model.addAttribute("total", formatService.format(total));
        return "account";
    }

    @GetMapping(path = "/search")
    public String search(
            @PathVariable Long userId,
            @PathVariable Long accountId,
            @RequestParam("q") String searchTerm,
            Model model)
    {
        AccountDao account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(Ex::userNotFound);
        model.addAttribute("user", account.getUser());
        model.addAttribute("account", account);
        model.addAttribute("query", searchTerm);

        List<EntryDao> daoList = entryRepository.findByAccountIdAndDescriptionContainingIgnoreCase(account.getId(), searchTerm);
        List<EntryDvo> dvoList = entryService.convert(daoList);

        model.addAttribute("entries", dvoList);
        Money total = entryService.computeTotal(daoList);
        model.addAttribute("total", formatService.format(total));
        return "account";
    }
}
