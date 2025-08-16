package com.whereuat_app.whereuat.model;

import com.whereuat_app.whereuat.enums.JoinStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
public class EventMember {
    @Id
    private String id;
    private String eventId;
    private String userId;
    private JoinStatus status;
    private Instant joinedAt;
}
