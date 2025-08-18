package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.dto.socket.LocationMessage;
import com.whereuat_app.whereuat.model.MemberLocation;
import com.whereuat_app.whereuat.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/locations")
public class LocationController {

    private final LocationService locationService;

    /**
     * Get all member locations for an event.
     */
    @GetMapping
    public ResponseEntity<List<MemberLocation>> getEventLocations(@PathVariable String eventId) {
        return  new ResponseEntity<>(locationService.getLocationsForEvent(eventId), HttpStatus.OK);
    }

    /*

    Update a member's location via REST (optional alternative to WebSocket).

    @PostMapping
    public MemberLocation updateLocation(@PathVariable String eventId,
                                   @RequestBody Location location) {
        location.setEventId(eventId);
        return locationService.updateLocation(location);
    }
     */
}

