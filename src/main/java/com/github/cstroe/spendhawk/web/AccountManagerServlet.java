package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.repository.UserRepository;
import com.github.cstroe.spendhawk.util.Ex;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/{userId}/account")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountManagerServlet {
    private static final String TEMPLATE = "/template/accounts/manage.ftl";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping(path = "/{accountId}")
    public String doGet(@PathVariable String userId, @PathVariable String accountId, Model model) {
        Account account = accountRepository.findByIdAndUserId(Integer.parseInt(userId), Integer.parseInt(accountId))
            .orElseThrow(Ex::userNotFound);

        model.addAttribute("account", account);
        return "index";
    }

/*    @PostMapping(path = "/{userId}")
    public String doPost() {
        String userIdRaw = Optional.ofNullable(request.getParameter("user.id"))
                .map(StringEscapeUtils::escapeHtml4)
                .orElseThrow(Ex::userIdRequired);
        String actionRaw = Optional.ofNullable(request.getParameter("action"))
                .orElseThrow(Ex::userIdRequired);

        Long userId, accountId;
        try {
            userId = Long.parseLong(userIdRaw);
        } catch (NumberFormatException ex) {
            throw new ServletException(ex);
        }

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        User currentUser = User.findById(userId).orElseThrow(Ex::userNotFound);

        //noinspection IfCanBeSwitch
        if("store".equals(actionRaw)) {
            String accountName = Optional.ofNullable(request.getParameter("account.name"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .orElseThrow(Ex::accountNameRequired);

            Optional<Account> newAccount = accountManager.createAccount(userId, accountName);

            if(newAccount.isPresent()) {
                request.setAttribute("message", "Added account <b>" + newAccount.get().getName() + "</b>.");
            } else {
                request.setAttribute("message", accountManager.getMessage());
            }
        } else if("delete".equals(actionRaw)) {
            String accountIdRaw = Optional.ofNullable(request.getParameter("account.id"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .orElseThrow(Ex::accountIdRequired);

            accountId = Long.parseLong(accountIdRaw);
            boolean deleteSuccessful = accountManager.deleteAccount(userId, accountId);

            if(deleteSuccessful) {
                request.setAttribute("message", "Account deleted.");
            } else {
                request.setAttribute("message", accountManager.getMessage());
            }
        } else if("Nest Account".equals(actionRaw)) {
            Long parentAccountId = Optional.ofNullable(request.getParameter("parentAccount.id"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .map(Long::parseLong)
                    .orElseThrow(() -> new RuntimeException("Parent account id is not valid."));

            Long subAccountId = Optional.ofNullable(request.getParameter("subAccount.id"))
                    .map(StringEscapeUtils::escapeHtml4)
                    .map(Long::parseLong)
                    .orElseThrow(() -> new RuntimeException("Sub account id is not valid."));

            Optional<Account> subAccount = accountManager.nestAccount(userId, parentAccountId, subAccountId);
            if(subAccount.isPresent()) {
                request.setAttribute("message", "Account nested.");
            } else {
                request.setAttribute("message", accountManager.getMessage());
            }
        }

        request.setAttribute("user", currentUser);
        request.setAttribute("fw", new TemplateForwarder(request));
        request.getRequestDispatcher(TEMPLATE).forward(request,response);
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
    }*/
}
