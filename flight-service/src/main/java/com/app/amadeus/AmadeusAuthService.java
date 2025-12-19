package com.app.amadeus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AmadeusAuthService {

    @Value("${amadeus.base-url}")
    private String baseUrl;

    @Value("${amadeus.client-id}")
    private String clientId;

    @Value("${amadeus.client-secret}")
    private String clientSecret;

    private String accessToken;
    private long tokenExpiryTime;

    public String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() > tokenExpiryTime) {
            fetchToken();
        }
        return accessToken;
    }

    private void fetchToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        baseUrl + "/v1/security/oauth2/token",
                        entity,
                        Map.class
                );

        accessToken = (String) response.getBody().get("access_token");
        Integer expiresIn = (Integer) response.getBody().get("expires_in");
        tokenExpiryTime = System.currentTimeMillis() + (expiresIn * 1000L);
    }
}
