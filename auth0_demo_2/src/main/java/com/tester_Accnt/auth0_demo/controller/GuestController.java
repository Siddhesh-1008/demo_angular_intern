package com.tester_Accnt.auth0_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuestController {

    @PreAuthorize("hasAuthority('ROLE_guest')")
    @GetMapping("/guest")
    public String guestAccess() {
        return "Welcome Guest!"; // Return a simple response for guest access
    }
}
