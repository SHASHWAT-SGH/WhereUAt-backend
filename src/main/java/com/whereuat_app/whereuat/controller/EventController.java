package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.dto.request.CreateEventRequestDTO;
import com.whereuat_app.whereuat.dto.response.EventDetailsResponseDTO;
import com.whereuat_app.whereuat.enums.JoinStatus;
import com.whereuat_app.whereuat.model.Event;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.EventRepository;
import com.whereuat_app.whereuat.repository.UsersRepository;
import com.whereuat_app.whereuat.service.EventService;
import com.whereuat_app.whereuat.types.EventMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UsersRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<EventDetailsResponseDTO> createEvent(@RequestBody CreateEventRequestDTO request) {
        EventDetailsResponseDTO savedEvent = eventService.createEvent(request);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody String eventId, @RequestBody CreateEventRequestDTO request) {
        // Logic to update an event
        // TODO: Change to new implementation
        return eventService.updateEvent(eventId, request);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteEvent(String eventId) {
        // Logic to delete an event
        // TODO: Change to new implementation
        return eventService.deleteEvent(eventId);
    }

    @GetMapping("/get-event")
    public ResponseEntity<EventDetailsResponseDTO> getEvent(@RequestParam String eventId) {
        // Logic to get an event
        return eventService.getEvent(eventId);
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<Event>> getAllEvents() {
        // Logic to get an event
        // TODO: Change to new implementation
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get-events-by-organizer")
    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByOrganizerId(@RequestParam String organizerId) {
        return eventService.getEventsByOrganizerId(organizerId);
    }

    @GetMapping("/get-events-by-organizer-auth")
    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByOrganizerId(Authentication auth) {
        User authPrincipal = (User) auth.getPrincipal();
        String organizerId = authPrincipal.getId();
        return eventService.getEventsByOrganizerId(organizerId);
    }

    @GetMapping("/get-events-by-organizer-status")
    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByOrganizerId(Authentication auth, @RequestParam JoinStatus joinStatus) {
        User authPrincipal = (User) auth.getPrincipal();
        String organizerId = authPrincipal.getId();
        List<EventDetailsResponseDTO> events = eventService.getEventsByOrganizerIdAndJoinStatus(organizerId, joinStatus);

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/get-events-by-member")
    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByMemberId(@RequestParam String memberId) {
        // Logic to get events by member ID
        List<EventDetailsResponseDTO> events = eventService.getEventsByMemberId(memberId);

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/get-events-by-member-status")
    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByMemberId(@RequestParam String memberId, @RequestParam JoinStatus joinStatus){
        // Logic to get events by member ID and join status
        List<EventDetailsResponseDTO> events = eventService.getEventsByMemberIdAndJoinStatus(memberId, joinStatus);

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/join-event")
    public ResponseEntity<?> joinEvent(Authentication authentication, @RequestParam String eventId){
        User user = (User) authentication.getPrincipal();
        String userId = user.getId();
        return eventService.joinEvent(userId, eventId);

    }

}
