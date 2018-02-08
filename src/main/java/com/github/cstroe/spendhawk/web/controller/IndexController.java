package com.github.cstroe.spendhawk.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.lang.String.format;

@Controller
public class IndexController {
    @RequestMapping("/")
    String index() {
        return "redirect:/user";
    }
}
