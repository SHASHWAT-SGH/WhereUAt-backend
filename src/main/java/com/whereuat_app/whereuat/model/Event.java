package com.whereuat_app.whereuat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document
public class Event {
    @Id
    private String id;
    private String eventName;
    private String eventDescription;
    private Double eventLatitude;
    private Double eventLongitude;
    private Instant eventTimeStamp;
    private String eventImageUrl;
    private String eventOrganizerId;
    private List<String> eventMembersId;
}
