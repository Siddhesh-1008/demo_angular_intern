package com.tester_Accnt.auth0_demo.config;

import org.springframework.context.annotation.Bean;  // Import your custom LogoutHandler
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tester_Accnt.auth0_demo.controller.LogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting the ClientRegistrationRepository so it can be used to create LogoutHandler
    private final ClientRegistrationRepository clientRegistrationRepository;

    // Constructor injection to provide the ClientRegistrationRepository
    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    // Create a @Bean for LogoutHandler so it can be injected into the security configuration
    @Bean
    public LogoutHandler logoutHandler() {
        return new LogoutHandler(clientRegistrationRepository);  // Create and return an instance of LogoutHandler
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()  // Allow unrestricted access to the root URL ("/")
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  // Define the logout URL pattern
                        .addLogoutHandler(logoutHandler())  // Use the custom logout handler
                )
                .formLogin(Customizer.withDefaults())  // Enable default form login
                .oauth2Login(Customizer.withDefaults())  // Enable default OAuth2 login (for Auth0, etc.)
                .build();
    }
}


/*In easy words, this code configures Spring Security to handle logout requests in a specific way:

logoutRequestMatcher(new AntPathRequestMatcher("/logout")):

This sets up a URL pattern (in this case, /logout) that Spring Security will listen for when a logout request is made.
When a user tries to access this URL, Spring Security will trigger the logout process.
addLogoutHandler(logoutHandler):

This adds a custom logout handler (logoutHandler) that will perform additional actions during the logout process.
The logoutHandler could be used to redirect the user to a specific page or perform any custom logic during logout (e.g., logging out of Auth0 or other external systems). */