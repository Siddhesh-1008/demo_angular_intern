package com.tester_proj.usersmanagementsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tester_proj.usersmanagementsystem.dto.ReqRes;
import com.tester_proj.usersmanagementsystem.entity.OurUsers;
import com.tester_proj.usersmanagementsystem.repository.UsersRepo;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //FOR USER REGISTERATION
    //What is ReqRes?
    // The ReqRes class is a Data Transfer Object (DTO) used to:
    // Receive data from the frontend (request data).
    // Send data to the frontend (response data).
    // Handles the user registration process
    //registrationRequest THIS CONTAINS ALL THE DATA OF THE USER THAT USER ENTER 
    //Frontend sends these fields in requests:name, city, role (optional), password, email.
    //Backend sends these fields in responses:statusCode, error, message, token, refreshToken, expirationTime, ourUsers, ourUsersList.
    public ReqRes register(ReqRes registrationRequest) {
        // Create a response object to store the result of registration
        ReqRes resp = new ReqRes();

        try {
            // Create a new user object to save the registration details
            OurUsers ourUser = new OurUsers();
            // Map the fields from the registration request to the user object
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            // Encode the password before saving it
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            // Save the user object to the database and get the result
            OurUsers ourUsersResult = usersRepo.save(ourUser);

            // If the user is saved successfully, set success message and status code
            if (ourUsersResult.getId() > 0) {
                resp.setOurUsers((ourUsersResult)); // Add saved user details to the response
                resp.setMessage("User Saved Successfully"); // Set success message
                resp.setStatusCode(200); // HTTP status 200 (OK)
            }

        } catch (Exception e) {
            // If an error occurs, set error message and status code
            resp.setStatusCode(500); // HTTP status 500 (Internal Server Error)
            resp.setError(e.getMessage()); // Add error message to the response
        }

        // Return the response object to the frontend
        return resp;
    }

    //FOR USER LOGIN
    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            // Extract the user's email from the provided refresh token
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

            // Find the user in the database using their email
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();

            // Check if the provided refresh token is valid for the user
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                // Generate a new JWT token for the user
                var jwt = jwtUtils.generateToken(users);

                // Populate the response object with the new token and success details
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;
        } catch (Exception e) {
            // Handle any errors and send a failure response
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();
        try {
            // Retrieve all users from the database
            List<OurUsers> result = usersRepo.findAll();

            // Check if the list is not empty
            if (!result.isEmpty()) {
                // Populate the response with the user list and success message
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                // No users found
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            // Handle any errors and send a failure response
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            // Retrieve the user by their ID or throw an exception if not found
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));

            // Populate the response with user details and success message
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            // Handle any errors and send a failure response
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    //DELETE USERS BASED ON ID
    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            // Check if the user exists in the database
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                // Delete the user if found
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                // User not found for deletion
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            // Handle any errors and send a failure response
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    //UPDATE USER BASED ON ID 
    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            // Check if the user exists in the database
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();

                // Update user details (email, name, city, role)
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if the request includes a password
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Hash the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                // Save the updated user details to the database
                OurUsers savedUser = usersRepo.save(existingUser);

                // Populate the response with updated user details and success message
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                // User not found for update
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            // Handle any errors and send a failure response
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    //GET USERINFO BASED ON EMAIL
    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            // Find user by their email
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                // Populate response with user details and success message
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                // User not found for the given email
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            // Handle any errors and send a failure response
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

}
