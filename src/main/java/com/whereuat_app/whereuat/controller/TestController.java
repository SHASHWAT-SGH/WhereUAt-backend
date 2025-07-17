package com.whereuat_app.whereuat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
//    @GetMapping("/user")
//    public String getUserInfo(@AuthenticationPrincipal String email) {
//        return "Authenticated user: " + email;
//    }

    @GetMapping("/ping")
    public String ping() {
        return "Server is on!";
    }

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        System.out.println("Authentication: " + authentication);
        return "Hello, " + (authentication != null ? authentication.getName() : "Guest") + "!";
    }
}
