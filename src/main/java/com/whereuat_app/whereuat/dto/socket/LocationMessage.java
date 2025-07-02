package com.whereuat_app.whereuat.dto.socket;

import lombok.Data;

@Data
public class LocationMessage {
    private String userId;
    private String eventId;
    private double latitude;
    private double longitude;
    private String date;
    private String time;
}
