package com.tester_proj.usersmanagementsystem.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tester_proj.usersmanagementsystem.entity.OurUsers;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// The @Data annotation from Lombok generates getters, setters, equals, hashCode, and toString methods automatically.
// The @JsonInclude annotation ensures that only non-null fields are included when this class is serialized to JSON.
public class ReqRes {

    private int statusCode; 
    // Set by the backend: HTTP status code for the response (e.g., 200 for success, 500 for error).

    private String error; 
    // Set by the backend: Describes the error message if something goes wrong.

    private String message; 
    // Set by the backend: A success or error message for the frontend.

    private String token; 
    // Set by the backend: JWT token for secure communication after login or registration.

    private String refreshToken; 
    // Set by the backend: Token to renew the authentication token when it expires.

    private String expirationTime; 
    // Set by the backend: Time when the authentication token will expire.

    private String name; 
    // Sent by the frontend during registration or updates, and returned by the backend in responses.

    private String city; 
    // Sent by the frontend during registration or updates, and returned by the backend in responses.

    private String role; 
    // Usually set by the backend: Specifies the user's role (e.g., "ADMIN" or "USER" ).

    private String password; 
    // Sent by the frontend during registration or login. Never sent by the backend for security reasons.

    private String email; 
    // Sent by the frontend during registration or login. May be returned by the backend in responses.

    private OurUsers ourUsers; 
    // Set by the backend: Holds a single user object retrieved from the database.

    private List<OurUsers> ourUsersList; 
    // Set by the backend: Holds a list of users, typically used in admin-related responses.

}





