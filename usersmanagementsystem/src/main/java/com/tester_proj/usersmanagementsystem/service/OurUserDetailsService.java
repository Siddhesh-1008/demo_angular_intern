package com.tester_proj.usersmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tester_proj.usersmanagementsystem.repository.UsersRepo;

// This class is a custom service that implements Spring Security's UserDetailsService.
// It is used to load user details (like username, password, roles) based on the username (email in this case).

@Service // This annotation marks the class as a Spring service, making it available for dependency injection.
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo; // This is a repository that interacts with the database to fetch user data.

    // This method is called to load user details by their username (email).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We use the UsersRepo to find a user by their email (username).
        // If the user is not found, an exception is thrown.
        return usersRepo.findByEmail(username).orElseThrow(); // If no user is found, it will throw an exception.
    }
}
