package com.tester_Accnt.auth0_demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    /* Retrieve User Data: The method retrieves user information (called claims) from the OidcUser object, which is available after the user has logged in through OAuth2/OIDC.

    The OidcUser object contains user information (like name, email, roles, etc.) after the user has logged in using Auth0.
    This object is populated automatically by Spring Security's OAuth2 login mechanism, so you don't need to explicitly fetch the user data from Auth0.

    Add to Model: The user data is added to the model under the key "profile", so it can be accessed in the view (e.g., in a Thymeleaf template).

    Return Profile View: Finally, it returns the view named "profile", which is typically a page that will display the user's information.*/
  
    /*
     How Spring Security Gets Data from Auth0:
  Spring Security handles the OAuth2 login flow for you:
  When the user is redirected to Auth0 for login, they authenticate and grant permissions (such as accessing their profile).
  After successful authentication, Auth0 sends the user's details (claims) back to the Spring application.
  Spring Security parses this information into an OidcUser object, which contains the user's claims (data) from Auth0.
  The data you are accessing with oidcUser.getClaims() comes from Auth0, as part of the OAuth2/OpenID Connect flow.
     */
    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        // Add the claims from the OidcUser to the model as the "profile" attribute
        model.addAttribute("profile", oidcUser.getClaims());
        return "profile"; // Return the view name "profile"
    }


}
