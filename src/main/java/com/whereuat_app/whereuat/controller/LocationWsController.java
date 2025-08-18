package com.whereuat_app.whereuat.controller;


import com.whereuat_app.whereuat.dto.request.LocationUpdateRequest;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LocationWsController {

    private final LocationService locationService;

    /**
     * Handle WebSocket location update messages.
     * Clients send to: /app/locations.update
     */
    @MessageMapping("/locations.update")
    public void handleLocationUpdate(Authentication auth, LocationUpdateRequest location) {
        User user = (User) auth.getPrincipal();
        locationService.updateLocation(user.getId(), location);
    }
}
