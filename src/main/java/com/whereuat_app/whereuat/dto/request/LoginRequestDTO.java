package com.whereuat_app.whereuat.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String firstName;
    private String lastName;
    private String userEmail;
}
