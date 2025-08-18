package com.whereuat_app.whereuat.service;

import com.whereuat_app.whereuat.dto.MemberLocationView;
import com.whereuat_app.whereuat.dto.request.LocationUpdateRequest;
import com.whereuat_app.whereuat.model.MemberLocation;
import com.whereuat_app.whereuat.repository.MemberLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final MemberLocationRepository locationRepository;
    private final SimpMessagingTemplate messagingTemplate;


    /**
     * Save or update a member's location and broadcast to the event channel.
     */
    public MemberLocation updateLocation(String memberId, LocationUpdateRequest location) {
        MemberLocation memberLocation = new MemberLocation();
        memberLocation.setEventId(location.getEventId());
        memberLocation.setMemberId(memberId);
        memberLocation.setLocation(new GeoJsonPoint(location.getLongitude(), location.getLatitude()));

        MemberLocation saved = locationRepository.save(memberLocation);

        // Broadcast to WebSocket subscribers of this event
        String destination = "/topic/events/" + location.getEventId() + "/locations";
        messagingTemplate.convertAndSend(destination, saved);

        return saved;
    }

    /**
     * Get all current member locations for a given event.
     */
    public List<MemberLocation> getLocationsForEvent(String eventId) {
        return locationRepository.findByEventId(eventId);
    }
}
