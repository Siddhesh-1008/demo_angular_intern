package com.tester_Accnt.auth0_demo.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PreAuthorize("hasAuthority('ROLE_user')")
    @GetMapping("/user")
    public String userAccess() {
        return "Welcome User!"; // Return a simple response for user access
    }
}