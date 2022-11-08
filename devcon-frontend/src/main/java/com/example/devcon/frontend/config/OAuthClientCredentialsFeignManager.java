package com.example.devcon.frontend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import static java.util.Objects.isNull;

public class OAuthClientCredentialsFeignManager {
    public static final String CLIENT_REGISTRATION_ID = "keycloak";

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientCredentialsFeignManager.class);

    public String getAccessToken() {
        try {
            OidcIdToken idToken = ((DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getIdToken();
            return idToken.getTokenValue();
        } catch (Exception exp) {
            logger.error("client credentials error " + exp.getMessage());
        }
        return null;
    }
}