package com.tester_Accnt.auth0_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tester_Accnt.auth0_demo.controller.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    // Custom LogoutHandler Bean
    @Bean
    public LogoutHandler logoutHandler() {
        return new LogoutHandler(clientRegistrationRepository);
    }

    // Define JwtDecoder Bean
    @Bean
    public JwtDecoder jwtDecoder() {
        // You can configure this for your JWT provider, e.g., Auth0
        String issuerUri = "https://dev-iqwsuwq0hjdquab4.us.auth0.com/"; // Replace with your issuer URI
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_admin")
                        .requestMatchers("/user/**").hasAuthority("ROLE_user")
                        .requestMatchers("/guest/**").hasAuthority("ROLE_guest")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .addLogoutHandler(logoutHandler())
                )
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }
}
