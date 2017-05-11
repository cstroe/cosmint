package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

import static java.text.MessageFormat.format;

/**
 * Operations on accounts.
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {
    public static final String DEFAULT_INCOME_ACCOUNT_NAME = "Income";
    public static final String DEFAULT_EXPENSE_ACCOUNT_NAME = "Expenses";
    public static final String DEFAULT_ASSET_ACCOUNT_NAME = "Assets";


    private final JanitorBean janitor;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public String getMessage() {
        return null;
    }

    public Account createAccount(Integer userId, String accountName) throws ServiceException {
        return createAccount(userId, accountName, null);
    }

    public Account createAccount(Integer userId, String accountName, Integer parentId) throws ServiceException {
        if(janitor.isBlank(accountName)) {
            throw new ServiceException("Account name cannot be blank.");
        }

        accountName = janitor.sanitize(accountName);

        final User currentUser = getUser(userId, () -> new ServiceException(format("User not found, id = %d", userId)));

        final Account theAccount = new Account();
        theAccount.setName(accountName);
        theAccount.setUser(currentUser);

        if(parentId != null) {
            Account parentAccount = Account.findById(currentUser, parentId)
                .orElseThrow(() -> new ServiceException(format("Parent account id is not valid, id = %d", parentId)));
//            theAccount.setParent(parentAccount);
        }

        boolean saved = theAccount.save();
        if(!saved) {
            throw new ServiceException("Could not save account.");
        }

        return theAccount;
    }

    public Account nestAccount(Integer userId, Integer parentAccountId, Integer subAccountId) throws ServiceException {
        final User currentUser = getUser(userId,
                () -> new ServiceException(format("User not found, id = %d", userId)));

        final Account parentAccount = getAccount(currentUser, parentAccountId,
                () -> new ServiceException("Parent account id is not valid."));

        final Account subAccount = getAccount(currentUser, subAccountId,
                () -> new ServiceException("Sub account id is not valid."));

//        subAccount.setParent(parentAccount);

        boolean saved = subAccount.save();
        if(!saved) {
            throw new ServiceException("Could not save account.");
        }

        return subAccount;
    }

    public void deleteAccount(Integer userId, Integer accountId) throws ServiceException {
        final User currentUser = getUser(userId,
                () -> new ServiceException(format("User not found, id = %d", userId)));

        Account account = accountRepository.findByUserId(currentUser.getId()).stream()
                .filter(a->a.getId().equals(accountId)).findFirst()
                .orElseThrow(() -> new ServiceException(format("Could not find account with id = %d", accountId)));

        accountRepository.delete(account);
    }

    private <T extends Exception> User getUser(Integer userId, Supplier<T> exceptionSupplier) throws T {
        return userRepository.findById(userId).orElseThrow(exceptionSupplier);
    }

    private <T extends Exception> Account getAccount(User user, Integer accountId, Supplier<T> exceptionSupplier) throws T {
        return accountRepository.findByIdAndUserId(user.getId(), accountId).orElseThrow(exceptionSupplier);
    }

    public Optional<Account> getDefaultExpenseAccount() {
        return accountRepository.findByName(DEFAULT_EXPENSE_ACCOUNT_NAME);
    }

    public Optional<Account> getDefaultIncomeAccount() {
        return accountRepository.findByName(DEFAULT_INCOME_ACCOUNT_NAME);
    }

    public Optional<Account> getDefaultAssetAccount() {
        return accountRepository.findByName(DEFAULT_ASSET_ACCOUNT_NAME);
    }
}
