package com.tester_proj.usersmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tester_proj.usersmanagementsystem.dto.ReqRes;
import com.tester_proj.usersmanagementsystem.entity.OurUsers;
import com.tester_proj.usersmanagementsystem.service.UsersManagementService;

@RestController
public class UserManagementController {

    // Inject the UsersManagementService to handle service logic
    @Autowired
    private UsersManagementService usersManagementService;

    // Endpoint for registering a new user
    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        // Call the service to handle registration and return the response
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    // Endpoint for logging in an existing user
    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        // Call the service to handle login and return the response
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    // Endpoint for refreshing the user's token
    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        // Call the service to refresh the token and return the response
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    // Endpoint for fetching a list of all users (Admin only)
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        // Call the service to get all users and return the response
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    // Endpoint for fetching user details by their ID (Admin only)
    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUserByID(@PathVariable Integer userId) {
        // Call the service to get user details by ID and return the response
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));
    }

    // Endpoint for updating user details by their ID (Admin only)
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody OurUsers reqres) {
        // Call the service to update user details and return the response
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    // Endpoint for fetching the logged-in user's profile (Admin/User)
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        // Retrieve the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // Call the service to fetch the user's profile using their email
        ReqRes response = usersManagementService.getMyInfo(email);
        // Return the response with the appropriate HTTP status
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Endpoint for deleting a user by their ID (Admin only)
    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId) {
        // Call the service to delete the user and return the response
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}
