package com.github.cstroe.spendhawk.web.transaction;


import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.dao.projection.AccountNameOnly;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.EntryRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/user/{userId}/account/{accountId}/entry/{entryId}/transaction/reconcileExisting")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReconcileWithExistingEntryController {
    public static final String USER = "user";
    public static final String ACCOUNT = "account";
    public static final String ENTRY = "entry";
    public static final String ACCOUNT_NAMES = "accountNames";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;

    @GetMapping("/selectAccount")
    public String add(@PathVariable Long userId,
                      @PathVariable Long accountId,
                      @PathVariable Long entryId,
                      Model model) {
        Optional<UserDao> optUser = userRepository.findById(userId);
        if(!optUser.isPresent()) {
            return "error";
        }

        model.addAttribute(USER, optUser.get());

        Optional<AccountDao> optAccount = accountRepository.findByIdAndUserId(accountId, optUser.get().getId());

        if(!optAccount.isPresent()) {
            return "error";
        }

        model.addAttribute(ACCOUNT, optAccount.get());

        Optional<EntryDao> optEntry = entryRepository.findByIdAndAccountId(entryId, optAccount.get().getId());

        if(!optEntry.isPresent()) {
            return "error";
        }

        model.addAttribute(ENTRY, optEntry.get());

        Collection<AccountNameOnly> accountNames = accountRepository.findByUserId(optUser.get().getId());
        model.addAttribute(ACCOUNT_NAMES, accountNames);

        return "transaction/reconcile-select-account";
    }


}
