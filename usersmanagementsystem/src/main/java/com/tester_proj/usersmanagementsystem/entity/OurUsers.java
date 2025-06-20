package com.tester_proj.usersmanagementsystem.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ourusers")
@Data
// This marks the class as a JPA entity and maps it to a database table named "ourusers". 
// The @Data annotation from Lombok generates boilerplate code like getters, setters, and more.
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // This defines 'id' as the primary key and sets it to auto-increment in the database.
    private Integer id;

    private String email; // Email address of the user, also used as the username for login.
    private String name;  // Name of the user.
    private String password; // Password of the user.
    private String city; // City where the user lives.
    private String role; // Role of the user, e.g., "USER" or "ADMIN".

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This method provides the roles/authorities assigned to the user.
        // In this case, it creates a single authority using the 'role' field.
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        // Returns the username for authentication, which is the 'email' in this case.
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Indicates if the account is not expired. Always returns true, meaning accounts don't expire.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Indicates if the account is not locked. Always returns true, meaning accounts are never locked.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Indicates if the credentials (password) are not expired. Always returns true, meaning credentials don't expire.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Indicates if the user account is enabled. Always returns true, meaning accounts are always enabled.
        return true;
    }
}
