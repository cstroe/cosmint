package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.dto.AddUserForm;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import java.util.Collections;

import static java.lang.String.format;

@Controller
@RequestMapping("/user")
public class UserAddController {
    private final UserRepository userRepository;

    public UserAddController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/add")
    public String add() {
        return "add-user";
    }

    @PostMapping("/add")
    public String create(@Valid AddUserForm userForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "add-user";
        }

        User newUser = userRepository.save(genUser(userForm));
        return format("redirect:/user/%d", newUser.getId());
    }

    private User genUser(AddUserForm userForm) {
        return new User(-1l, userForm.getUsername(), Collections.emptyList());
    }
}
