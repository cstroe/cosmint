package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dto.AddAccountForm;
import com.github.cstroe.spendhawk.dao.UserDao;
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

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/account/add")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountAddController {
    private final UserRepository userRepository;
    private final AccountService accountService;

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
                AccountDao newAccount = accountService.createAccount(currentUser, accountForm);
                return format("redirect:/user/%d/account/%d", currentUser.getId(), newAccount.getId());
            }
        } else {
            return "error";
        }
    }
}
