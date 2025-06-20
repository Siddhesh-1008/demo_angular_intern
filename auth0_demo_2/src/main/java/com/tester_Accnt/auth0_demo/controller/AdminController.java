package com.tester_Accnt.auth0_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {


    @PreAuthorize("hasAuthority('ROLE_admin')")
    @GetMapping("/admin")
    public String adminAccess() {
        return "Welcome Admin!"; // Return a simple response for admin access
    }
}