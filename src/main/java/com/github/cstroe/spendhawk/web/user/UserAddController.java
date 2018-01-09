package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.dto.AddUserForm;
import com.github.cstroe.spendhawk.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String add(@ModelAttribute("userForm") AddUserForm userForm, Model model) {
        return "add-user";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("userForm") @Valid AddUserForm userForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "add-user";
        }

        UserDao newUser = userRepository.save(genUser(userForm));
        return format("redirect:/user/%d", newUser.getId());
    }

    private UserDao genUser(AddUserForm userForm) {
        return new UserDao(null, userForm.getUsername(), Collections.emptyList());
    }
}
