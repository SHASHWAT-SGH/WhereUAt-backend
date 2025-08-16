package com.whereuat_app.whereuat.dto.response;

import com.whereuat_app.whereuat.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
public class UserResponseDTO {
    private String id;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.userEmail = user.getUserEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
