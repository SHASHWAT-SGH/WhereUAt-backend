package com.whereuat_app.whereuat.service;

import com.whereuat_app.whereuat.dto.response.UserResponseDTO;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository userRepository;

    public List<UserResponseDTO> searchUser(String searchText) {
        return userRepository.findByUserEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(searchText, searchText);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user =  userRepository.findByUserEmail(email);
        if(user != null) {
            return new UserResponseDTO(user);
        } else {
            return null;
        }
    }
}
