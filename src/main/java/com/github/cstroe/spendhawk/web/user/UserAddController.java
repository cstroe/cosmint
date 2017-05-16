package com.github.cstroe.spendhawk.web.user;

import com.github.cstroe.spendhawk.dto.AddUserForm;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static java.lang.String.format;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAddController {
    private final UserRepository userRepository;

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
        User newUser = new User();
        newUser.setName(userForm.getUsername());
        return newUser;
    }
}
