package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository repository) {
        userRepository = repository;
    }

    @GetMapping
    public String all(Model model) {
        model.addAttribute("users", Lists.newArrayList(userRepository.findAll()));
        return "users";
    }

    @GetMapping("/{userId}")
    public String view(@PathVariable Integer userId, Model model) {
        User user = userRepository.findById(userId);
        if(user != null) {
            model.addAttribute("user", user);
            return "accounts";
        } else {
            return "error";
        }
    }
}
