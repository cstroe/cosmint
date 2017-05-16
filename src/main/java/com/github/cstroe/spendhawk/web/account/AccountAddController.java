package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.dto.AddAccountForm;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/account/add")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountAddController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @GetMapping
    public String add(@PathVariable Integer userId, Model model) {
        Optional<User> currentUser = userRepository.findById(userId);
        if(currentUser.isPresent()) {
            model.addAttribute("user", currentUser.get());
            return "add-account";
        } else {
            return "error";
        }
    }

    @PostMapping
    public String create(@Valid AddAccountForm accountForm, BindingResult bindingResult, Model model) {
        Optional<User> currentUser = userRepository.findById(accountForm.getUserId());
        if(currentUser.isPresent()) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("user", currentUser.get());
                return "add-account";
            } else {
                Account account = new Account();
                account.setName(accountForm.getAccountName());
                account.setUser(currentUser.get());
                Account newAccount = accountRepository.save(account);
                return format("redirect:/user/%d/account/%d", currentUser.get().getId(), newAccount.getId());
            }
        } else {
            return "error";
        }
    }
}
