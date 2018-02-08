package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.EntryDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.dto.TransactionCreateForm;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.EntryRepository;
import com.github.cstroe.spendhawk.repository.TransactionRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/user/{userId}/transaction/create")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionCreateController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;
    private final TransactionRepository transactionRepository;

    @PostMapping
    public String create(@Valid @ModelAttribute("transactionForm") TransactionCreateForm form,
                         @PathVariable Long userId,
                         BindingResult bindingResult,
                         Model model) {
        Optional<UserDao> optUser = userRepository.findById(userId);
        if(!optUser.isPresent()) {
            return "error";
        }

        UserDao user = optUser.get();

        if(bindingResult.hasErrors()) {
            return "error";
        }

        Optional<AccountDao> optAccount = accountRepository.findByIdAndUserId(form.getAccountId(), user.getId());
        if(!optAccount.isPresent()) {
            return "error";
        }

        AccountDao account = optAccount.get();


        Optional<EntryDao> optEntry = entryRepository.findByIdAndAccountId(form.getEntryId(), account.getId());
        if(!optEntry.isPresent()) {
            return "error";
        }

        EntryDao entry = optEntry.get();

        TransactionDao transaction = new TransactionDao();
        transaction.setEntries(Lists.newArrayList(entry));

        TransactionDao saved = transactionRepository.save(transaction);

        if(saved == null) {
            return "error";
        }

        return format("redirect:/user/%d/account/%d/entry/%d", user.getId(), account.getId(), entry.getId());
    }
}
