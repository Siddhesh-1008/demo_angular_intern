package com.tester_Accnt.auth0_demo.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomGrantedAuthoritiesConverter implements org.springframework.core.convert.converter.Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract roles from the JWT claim (e.g., "roles" or "permissions")
        Collection<String> roles = jwt.getClaimAsStringList("roles"); // Replace "roles" with your claim name
        if (roles == null) {
            roles = jwt.getClaimAsStringList("permissions"); // Alternative claim
        }

        // Map roles to GrantedAuthority (prepend "ROLE_" if necessary)
        return roles != null ? roles.stream()
                .map(role -> "ROLE_" + role.toUpperCase()) // Spring Security expects "ROLE_*"
                .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : new JwtGrantedAuthoritiesConverter().convert(jwt);
    }
}
