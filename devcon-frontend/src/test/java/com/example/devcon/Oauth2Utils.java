package com.example.devcon;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class Oauth2Utils {
    public static String obtainAccessToken(String username, String password) {


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "jwtClient");
        map.add("client_secret", "jwtClientSecret");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response =
                restTemplate.exchange("http://localhost:8083/auth/realms/devcon/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        String.class);


        String resultString = response.getBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    public static Authentication getAuthentication(OidcIdToken accessToken) {
        return new OAuth2AuthenticationToken(new DefaultOidcUser(
                Collections.emptyList(),
                accessToken
        ), Collections.emptyList(), "keycloak");
    }
}
