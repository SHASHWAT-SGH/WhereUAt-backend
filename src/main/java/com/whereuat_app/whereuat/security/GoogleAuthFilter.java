package com.whereuat_app.whereuat.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleAuthFilter extends OncePerRequestFilter {

    private final UsersRepository usersRepository;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(token);

            if (payload != null) {
                String email = payload.getEmail();
                String firstName = (String) payload.get("firstName");
                String lastName = (String) payload.get("lastName");
                // You can load or create user in DB here

                User user = usersRepository.findByUserEmail(email);
                if (user == null) {
                    user = new User();
                    user.setUserEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);

                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } else {
//            System.out.println("No Bearer token found in the request.");
        }

        filterChain.doFilter(request, response);
    }
}
