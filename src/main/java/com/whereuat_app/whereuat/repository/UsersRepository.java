package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.dto.response.UserResponseDTO;
import com.whereuat_app.whereuat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    User findByUserEmail(String userEmail);

    List<UserResponseDTO> findByUserEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String userEmail, String firstName);


}
