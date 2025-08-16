package com.whereuat_app.whereuat.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier(@Value("${google.client-id}") String clientId) {
        System.out.println("Google Client ID: " + clientId);
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verifyToken(String token) {
        System.out.println("TOKEN "+token);
        try {
            GoogleIdToken idToken = verifier.verify(token);
            System.out.println("ID Token: " + idToken);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                System.out.println("Invalid ID token.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error verifying ID token: " + e.getMessage());
            return null;
        }
    }

}
