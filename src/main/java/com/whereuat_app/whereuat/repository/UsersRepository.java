package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    User findByUserEmail(String userEmail);

}
