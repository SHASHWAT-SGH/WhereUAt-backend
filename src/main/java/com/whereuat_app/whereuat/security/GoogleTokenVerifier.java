package com.whereuat_app.whereuat.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

public class GoogleTokenVerifier {

//    @Value("${google.client-id}")
    private static final String CLIENT_ID = "";

    private static  final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
            JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

    public static GoogleIdToken.Payload verify(String token) {
        System.out.println("CLIENT_ID "+CLIENT_ID);
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
