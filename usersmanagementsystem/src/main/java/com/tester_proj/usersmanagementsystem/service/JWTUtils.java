package com.tester_proj.usersmanagementsystem.service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtils {

    // A secret key used for signing and verifying JWT tokens (Bob's "ticket" is signed with this).
    private SecretKey Key;

    // The time in milliseconds before a token expires (Bob's ticket will expire in 24 hours).
    private static final long EXPIRATION_TIME = 86400000;

    // Constructor: Initializes the secret key used to sign tokens.
    public JWTUtils() {
        // A secret string used to create the signing key for HMAC SHA-256 (this is the coffee shop's private key).
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        // Decoding the secret string into bytes and setting up the key (this is like turning a secret code into a lock that only the coffee shop can use).
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256"); // The lock (secret key) is set.
    }

    // Generates a JWT token for Bob (Bob's ticket to access the special tea).
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Adds Bob's username (email) as the subject (this is Bob's name on the ticket).
                .issuedAt(new Date(System.currentTimeMillis())) // Sets the time when Bob's ticket is created (ticket issued when Bob logs in).
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Sets the expiration time (Bob's ticket expires in 24 hours).
                .signWith(Key) // Signs Bob's ticket with the secret key (only the coffee shop can verify this ticket).
                .compact(); // Builds and returns Bob's JWT token (this is Bob's "ticket" to access tea).
    }

    // Generates a refresh token with additional claims (extra data) for Bob (this is like a backup ticket in case the original expires).
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims) // Adds any extra information (like Bob's permissions) to the refresh token.
                .subject(userDetails.getUsername()) // Adds Bob's username (email) as the subject (who this ticket belongs to).
                .issuedAt(new Date(System.currentTimeMillis())) // Sets the time when the refresh token is created.
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Sets the expiration time for the refresh token (expires in 24 hours).
                .signWith(Key) // Signs the refresh token with the secret key.
                .compact(); // Builds and returns the refresh token (this ticket allows Bob to get a new regular token).
    }

    // Extracts Bob's username (subject) from the given token (checks whose ticket it is).
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject); // Retrieves Bob's username from the token (the ticket's owner).
    }

    // Helper method to extract specific claims (like username, expiration date) from the token.
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
                Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload() // Decodes and verifies the token, retrieving its claims.
        );
    }

    // Checks if Bob's token is valid by matching the username and ensuring it's not expired (is the ticket still valid?).
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extracts Bob's username from the token (who the ticket belongs to).
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Check if the token is for Bob and not expired.
    }

    // Checks if the token is expired by comparing its expiration date to the current time (is Bob's ticket still good?).
    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date(0)); // Checks if the ticket's expiration time has passed.
    }
}
