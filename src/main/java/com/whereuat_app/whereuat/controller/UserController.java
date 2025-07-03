package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UsersRepository usersRepository;


    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> searchUser(@PathVariable String searchText) {
//        List<User> users = usersRepository.findAll();
        List<User> users = usersRepository.findByUserEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(searchText, searchText);
        return ResponseEntity.ok(users);
    }
}
