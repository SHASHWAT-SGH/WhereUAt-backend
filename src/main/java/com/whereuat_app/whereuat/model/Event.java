package com.whereuat_app.whereuat.model;

import com.whereuat_app.whereuat.types.EventMember;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class Event {
    @Id
    private String id;
    private String eventName;
    private String eventDescription;
    private GeoJsonPoint eventLocation;
    private Instant eventTimeStamp;
    private String eventImageUrl;
    private String eventOrganizerId;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
