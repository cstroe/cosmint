package com.github.cstroe.spendhawk.web.account;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.dto.AddAccountForm;
import com.github.cstroe.spendhawk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/account/add")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountAddController {
    private final UserRepository userRepository;
    private final AccountService accountService;

    @GetMapping
    public String add(@ModelAttribute("accountForm") AddAccountForm accountForm,
                      @PathVariable Long userId, Model model) {
        Optional<UserDao> currentUser = userRepository.findById(userId);
        if(currentUser.isPresent()) {
            model.addAttribute("user", currentUser.get());
            return "add-account";
        } else {
            return "error";
        }
    }

    @PostMapping
    public String create(@ModelAttribute("accountForm") @Valid AddAccountForm accountForm,
                         BindingResult bindingResult, Model model) {
        Optional<UserDao> optUser = userRepository.findById(accountForm.getUserId());
        if(optUser.isPresent()) {
            UserDao currentUser = optUser.get();
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
