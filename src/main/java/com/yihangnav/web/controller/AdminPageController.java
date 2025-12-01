package com.yihangnav.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping({"", "/"})
    public String login() {
        return "forward:/admin/login.html";
    }

    @GetMapping({"/index", "/dashboard"})
    public String index() {
        return "forward:/admin/index.html";
    }
}
