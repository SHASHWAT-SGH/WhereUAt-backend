package com.whereuat_app.whereuat.dto.response;

import com.whereuat_app.whereuat.enums.JoinStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class EventMemberDetailsDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String email;
    private JoinStatus status;
    private Instant joinedAt;
}
