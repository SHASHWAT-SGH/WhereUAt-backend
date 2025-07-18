package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.dto.request.LoginRequestDTO;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsersRepository usersRepository;

    @PostMapping("/login")
     public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
         // Logic to authenticate user
         User fetchedUser = usersRepository.findByUserEmail(request.getUserEmail());
         System.out.println("Login request received for user: " + request);

         User user;
        // if user does not exist
        // add to the database
        user = Objects.requireNonNullElseGet(fetchedUser, User::new);
        user.setId(request.getId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserEmail(request.getUserEmail());
        user.setProfileImageUrl(request.getProfileImageUrl());
        usersRepository.save(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
     }

}
