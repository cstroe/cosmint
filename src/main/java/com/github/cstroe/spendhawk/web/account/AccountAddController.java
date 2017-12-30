package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dto.AddAccountForm;
import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/account/add")
public class AccountAddController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AccountAddController(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public String add(@PathVariable Long userId, Model model) {
        UserDao currentUser = userRepository.findById(userId);
        if(currentUser != null) {
            model.addAttribute("user", currentUser);
            return "add-account";
        } else {
            return "error";
        }
    }

    @PostMapping
    public String create(@Valid AddAccountForm accountForm, BindingResult bindingResult, Model model) {
        UserDao currentUser = userRepository.findById(accountForm.getUserId());
        if(currentUser != null) {
            if(bindingResult.hasErrors()) {
                model.addAttribute("user", currentUser);
                return "add-account";
            } else {
                AccountDao account = new AccountDao();
                account.setName(accountForm.getAccountName());
                account.setUser(currentUser);
                AccountDao newAccount = accountRepository.save(account);
                return format("redirect:/user/%d/account/%d", currentUser.getId(), newAccount.getId());
            }
        } else {
            return "error";
        }
    }
}
