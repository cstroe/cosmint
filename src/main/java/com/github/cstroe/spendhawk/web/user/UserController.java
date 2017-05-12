package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.UserRepository;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    @GetMapping
    public String all(Model model) {
        List<User> fakeUsers = Lists.newArrayList(
                genUser(1, "User 1"),
                genUser(2, "User 2"));

        model.addAttribute("users", fakeUsers);
        return "users";
    }

    @GetMapping("/{userId}")
    public String view(@PathVariable Integer userId, Model model) {
        User user1 = genUser(1, "User 1");

        user1.setAccounts(
                Lists.newArrayList(
                        genAccount(1, "Account 1", user1),
                        genAccount(2, "Account 2", user1)
                )
        );

        model.addAttribute("user", user1);
        return "accounts";
    }

    private User genUser(Integer id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    private Account genAccount(Integer id, String name, User user) {
        Account acct = new Account();
        acct.setId(id);
        acct.setName(name);
        acct.setUser(user);
        return acct;
    }
}
