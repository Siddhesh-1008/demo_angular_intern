package com.tester_Accnt.auth0_demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;  // For accessing client registration info
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;  // For handling logout
import org.springframework.web.util.UriComponentsBuilder;  // For constructing the return URL

import jakarta.servlet.http.HttpServletRequest;  // For building the logout URL
import jakarta.servlet.http.HttpServletResponse;  // For handling HTTP request


//The class extends SecurityContextLogoutHandler, which is a built-in Spring class that handles the logout process.
public class LogoutHandler extends SecurityContextLogoutHandler {

  //Declaring a ClientRegistrationRepository field to hold the Auth0 client registration info, which will be used to retrieve the Auth0 configuration, like the issuer and client ID
    private final ClientRegistrationRepository clientRegistrationRepository;

    //The constructor accepts ClientRegistrationRepository as an argument and assigns it to the class field. This repository contains the information about OAuth2 providers (in this case, Auth0).
    
    public LogoutHandler(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    //This method is overridden from SecurityContextLogoutHandler to customize the logout behavior.
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      /*Calling super.logout(...) to perform the default logout functionality (e.g., clearing security context, invalidating session, etc.)*/
        super.logout(request, response, authentication);

        /*Retrieve the Auth0 issuer URL (e.g., https://dev-xyz.auth0.com/) from the ClientRegistrationRepository using the registration ID "auth0". This URL is used for the logout endpoint. */
        String issuer = (String) clientRegistrationRepository.findByRegistrationId("auth0")
                .getProviderDetails()
                .getConfigurationMetadata()
                .get("issuer");


      /*Retrieve the clientId from the same ClientRegistrationRepository for Auth0. This is needed to create a logout URL with client_id as a query parameter. */
        String clientId = clientRegistrationRepository.findByRegistrationId("auth0").getClientId();

        /*
         This creates the returnTo URL, which is where the user will be redirected after logging out. It typically points to the home page or the application's main URL.
         */
        String returnTo = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

         // Build the Auth0 logout URL using the new fromUriString() method
         String logoutUrl = UriComponentsBuilder
         .fromUriString(issuer + "/v2/logout?client_id={clientId}&returnTo={returnTo}")
         .encode()
         .buildAndExpand(clientId, returnTo)
         .toUriString();


        try {
          // Redirect to Auth0 logout URL to complete the logout process
            response.sendRedirect(logoutUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
