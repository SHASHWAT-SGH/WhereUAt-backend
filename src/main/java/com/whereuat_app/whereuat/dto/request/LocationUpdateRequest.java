package com.whereuat_app.whereuat.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationUpdateRequest {
    @NotBlank
    private String eventId;

    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    // optional extras from mobile SDKs
    private Double accuracy; // meters
    private Double heading;  // degrees
    private Double speed;    // m/s
}
