package com.tester_proj.usersmanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tester_proj.usersmanagementsystem.service.JWTUtils;
import com.tester_proj.usersmanagementsystem.service.OurUserDetailsService;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils; // Utility to generate and validate JWT tokens
    @Autowired
    private OurUserDetailsService ourUserDetailsService; // Service to load user details from database

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Retrieve the Authorization header from the incoming request.
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // 2. If the Authorization header is missing, proceed to the next filter (request not authenticated).
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the JWT token by removing "Bearer " from the header.
        jwtToken = authHeader.substring(7); // Get the token part (after "Bearer ")
        userEmail = jwtUtils.extractUsername(jwtToken); // Extract the username (email) from the JWT token

        // 4. If a username is found in the token and there is no existing authentication, authenticate the user.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5. Load user details from the database using the email extracted from the JWT token.
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

            // 6. If the JWT token is valid for this user, set the authentication in the Spring Security context.
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                // Create an empty SecurityContext to store the authentication information.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Create a new authentication token with the user's details and authorities (roles).
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Add request details

                // 7. Set the created authentication token into the SecurityContext.
                securityContext.setAuthentication(token);

                // 8. Store the SecurityContext in the SecurityContextHolder.
                SecurityContextHolder.setContext(securityContext);
            }
        }

        // 9. Continue with the next filter or the requested endpoint (controller).
        filterChain.doFilter(request, response);
    }
}


//46.19