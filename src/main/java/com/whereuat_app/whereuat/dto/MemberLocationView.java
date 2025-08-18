package com.whereuat_app.whereuat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLocationView {
    private String eventId;
    private String memberId;
    private double latitude;
    private double longitude;
    private Double accuracy;
    private Double heading;
    private Double speed;
    private Instant updatedAt;
}
