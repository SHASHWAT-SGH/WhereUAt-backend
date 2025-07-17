package com.whereuat_app.whereuat.dto.request;

import com.whereuat_app.whereuat.model.User;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CreateEventRequestDTO {
    private String eventName;
    private String eventDescription;
    private Double eventLatitude;
    private Double eventLongitude;
    private Instant eventTimeStamp;
    private String eventImageUrl;
    private String eventOrganizerId;
    private List<String> eventMembersId;

}
