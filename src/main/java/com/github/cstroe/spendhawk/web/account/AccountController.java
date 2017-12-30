package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.repository.AccountRepository;
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
        model.addAttribute("transactions", stubTransactions());
        return "transactions";
    }

    private List<TransactionDao> stubTransactions() {
        return Lists.newArrayList(
                t(new Date(), 0.01, "TransactionDao 1"),
                t(new Date(), 0.02, "TransactionDao 2"),
                t(new Date(), 0.03, "TransactionDao 3")
        );
    }

    private TransactionDao t(Date effectiveDate, Double amount, String description) {
        TransactionDao t = new TransactionDao();
        t.setDescription(description);
        return t;
    }
}
