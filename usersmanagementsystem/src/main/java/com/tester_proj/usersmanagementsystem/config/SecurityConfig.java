package com.tester_proj.usersmanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tester_proj.usersmanagementsystem.service.OurUserDetailsService;

@Configuration // Marks this class as a configuration for Spring.
@EnableWebSecurity // Enables Spring Security in the application.
public class SecurityConfig {

    @Autowired
    private OurUserDetailsService ourUserDetailsService; // Service to load user details from the database.
    @Autowired
    private JWTAuthFilter jwtAuthFilter; // Custom filter for validating JWT tokens.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we use JWT for authentication.
                .cors(Customizer.withDefaults()) // Enable CORS for cross-origin requests (e.g., frontend hosted elsewhere).
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**", "/public/**").permitAll() // Public routes (no login required).
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN") // Admin routes (require ADMIN role).
                        .requestMatchers("/user/**").hasAnyAuthority("USER") // User routes (require USER role).
                        .requestMatchers("/guest/**").hasAnyAuthority("GUEST") // GUEST routes (require GUEST role).
                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN","USER","GUEST") // Shared routes for both ADMIN and USER.
                        .anyRequest().authenticated()) // All other routes require authentication.
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions: each request must include a JWT token.
                .authenticationProvider(authenticationProvider()) // Use the custom authentication provider for user validation.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT filter before the default authentication filter.
        return httpSecurity.build(); // Build the security configuration.
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService); // Use our custom UserDetailsService.
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Use BCrypt for hashing passwords.
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Passwords are hashed securely using BCrypt.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Provides the AuthenticationManager for login.
    }
}
