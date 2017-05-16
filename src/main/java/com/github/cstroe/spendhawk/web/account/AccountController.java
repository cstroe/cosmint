package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.util.Ex;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/{userId}/account/{accountId}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    private final AccountRepository accountRepository;

    @GetMapping
    public String view(
            @PathVariable Integer userId,
            @PathVariable Integer accountId,
            Model model)
    {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(Ex::userNotFound);
        model.addAttribute("user", account.getUser());
        model.addAttribute("account", account);
        return "transactions";
    }
}
