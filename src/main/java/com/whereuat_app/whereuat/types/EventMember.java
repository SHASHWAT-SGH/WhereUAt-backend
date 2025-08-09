package com.whereuat_app.whereuat.types;

import com.whereuat_app.whereuat.enums.JoinStatus;
import com.whereuat_app.whereuat.model.User;
import lombok.Builder;
import lombok.Data;

@Data
public class EventMember {
    private String userId;   // Reference to User._id
    private JoinStatus status;
}
