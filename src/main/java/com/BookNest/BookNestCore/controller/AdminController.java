package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/create")
    public String showCreateAdminForm() {
        return "create-admin";
    }

    @PostMapping("/admin/create")
    public String createAdmin(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        userService.addAdminUser(username, password);
        return "redirect:/admin/create?success";
    }

}

