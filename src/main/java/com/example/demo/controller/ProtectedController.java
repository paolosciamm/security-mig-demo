package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/user")
    public String userArea() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Benvenuto nell'area USER, " + auth.getName() + "!";
    }

    @GetMapping("/admin")
    public String adminArea() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Benvenuto nell'area ADMIN, " + auth.getName() + "!";
    }

    @GetMapping("/info")
    public String userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "User: " + auth.getName() +
                ", Roles: " + auth.getAuthorities().toString();
    }
}