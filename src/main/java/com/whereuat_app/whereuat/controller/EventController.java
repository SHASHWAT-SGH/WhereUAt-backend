package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.dto.request.CreateEventRequestDTO;
import com.whereuat_app.whereuat.model.Event;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.EventRepository;
import com.whereuat_app.whereuat.repository.UsersRepository;
import com.whereuat_app.whereuat.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UsersRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequestDTO request) {
        // Logic to create an event
        System.out.println("Creating event: " + request);

        List<User> eventMembers = userRepository.findAllById(request.getEventMembersId());
        // save the event
        Event event = new Event();
        event.setEventName(request.getEventName());
        event.setEventDescription(request.getEventDescription());
        event.setEventTimeStamp(request.getEventTimeStamp());
        event.setEventLatitude(request.getEventLatitude());
        event.setEventLongitude(request.getEventLongitude());
        event.setEventImageUrl(request.getEventImageUrl());
        event.setEventOrganizerId(request.getEventOrganizerId());
        event.setEventMembersId(eventMembers);
        // Assuming you have an EventRepository to save the event
        event = eventRepository.save(event);

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody String eventId, @RequestBody CreateEventRequestDTO request) {
        // Logic to update an event
        return eventService.updateEvent(eventId, request);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteEvent(String eventId) {
        // Logic to delete an event
        return eventService.deleteEvent(eventId);
    }

    @GetMapping("/get-event")
    public ResponseEntity<Event> getEvent(@RequestParam String eventId) {
        // Logic to get an event
        return eventRepository.findById(eventId)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<Event>> getAllEvents() {
        // Logic to get an event
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get-events-by-organizer")
    public ResponseEntity<?> getEventsByOrganizerId(@RequestParam String organizerId) {
        // Logic to get events by organizer ID
        List<Event> events = eventRepository.getByEventOrganizerId(organizerId);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);

    }

    @GetMapping("/get-events-by-member")
    public ResponseEntity<?> getEventsByMemberId(@RequestParam String memberId) {
        // Logic to get events by member ID
        System.out.println("REQUEST MEMBER ID: " + memberId);
        List<Event> events = eventRepository.findByEventMembersIdContaining(memberId);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }



}
