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

         // if user does not exist
         if (fetchedUser == null) {
             // add to the database
             User newUser = new User();
             newUser.setFirstName(request.getFirstName());
             newUser.setLastName(request.getLastName());
             newUser.setUserEmail(request.getUserEmail());
             usersRepository.insert(newUser);
             return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
         }
         // if user exists
        return new ResponseEntity<>("User exists", HttpStatus.OK);
     }

}
