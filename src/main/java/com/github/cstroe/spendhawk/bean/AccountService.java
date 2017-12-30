package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.UserDao;
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

    public AccountDao createAccount(Long userId, String accountName) throws ServiceException {
        return createAccount(userId, accountName, null);
    }

    public AccountDao createAccount(Long userId, String accountName, Integer parentId) throws ServiceException {
        if(janitor.isBlank(accountName)) {
            throw new ServiceException("AccountDao name cannot be blank.");
        }

        accountName = janitor.sanitize(accountName);

        final UserDao currentUser = getUser(userId, () -> new ServiceException(format("UserDao not found, id = %d", userId)));

        final AccountDao theAccount = new AccountDao();
        theAccount.setName(accountName);
        theAccount.setUser(currentUser);

        if(parentId != null) {
            AccountDao parentAccount = accountRepository.findByIdAndUserId(parentId.longValue(), currentUser.getId())
                .orElseThrow(() -> new ServiceException(format("Parent account id is not valid, id = %d", parentId)));
//            theAccount.setParent(parentAccount);
        }

        AccountDao saved = accountRepository.save(theAccount);
        if(saved.getId() == null) {
            throw new ServiceException("Could not save account.");
        }

        return theAccount;
    }

    public AccountDao nestAccount(Long userId, Integer parentAccountId, Integer subAccountId) throws ServiceException {
        final UserDao currentUser = getUser(userId,
                () -> new ServiceException(format("UserDao not found, id = %d", userId)));

        final AccountDao parentAccount = getAccount(currentUser, parentAccountId,
                () -> new ServiceException("Parent account id is not valid."));

        final AccountDao subAccount = getAccount(currentUser, subAccountId,
                () -> new ServiceException("Sub account id is not valid."));

//        subAccount.setParent(parentAccount);

        AccountDao saved = accountRepository.save(subAccount);
        if(saved.getId() == null) {
            throw new ServiceException("Could not save account.");
        }

        return subAccount;
    }

    public void deleteAccount(Long userId, Integer accountId) throws ServiceException {
        final UserDao currentUser = getUser(userId,
                () -> new ServiceException(format("UserDao not found, id = %d", userId)));

        AccountDao account = accountRepository.findByUserId(currentUser.getId()).stream()
                .filter(a->a.getId().equals(accountId)).findFirst()
                .orElseThrow(() -> new ServiceException(format("Could not find account with id = %d", accountId)));

        accountRepository.delete(account);
    }

    private <T extends Exception> UserDao getUser(Long userId, Supplier<T> exceptionSupplier) throws T {
        return Optional.ofNullable(userRepository.findById(userId)).orElseThrow(exceptionSupplier);
    }

    private <T extends Exception> AccountDao getAccount(UserDao user, Integer accountId, Supplier<T> exceptionSupplier) throws T {
        return accountRepository.findByIdAndUserId(user.getId(), accountId.longValue()).orElseThrow(exceptionSupplier);
    }

    public Optional<AccountDao> getDefaultExpenseAccount() {
        return accountRepository.findByName(DEFAULT_EXPENSE_ACCOUNT_NAME);
    }

    public Optional<AccountDao> getDefaultIncomeAccount() {
        return accountRepository.findByName(DEFAULT_INCOME_ACCOUNT_NAME);
    }

    public Optional<AccountDao> getDefaultAssetAccount() {
        return accountRepository.findByName(DEFAULT_ASSET_ACCOUNT_NAME);
    }
}
