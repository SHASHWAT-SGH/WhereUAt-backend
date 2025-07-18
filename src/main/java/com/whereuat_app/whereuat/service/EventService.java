package com.whereuat_app.whereuat.service;

import com.whereuat_app.whereuat.dto.request.CreateEventRequestDTO;
import com.whereuat_app.whereuat.model.Event;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.EventRepository;
import com.whereuat_app.whereuat.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository userRepository;

    public ResponseEntity<String> updateEvent(String eventId, CreateEventRequestDTO request) {
        // Logic to update an event
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        Event event = eventOptional.get();

        List<User> eventMembers = userRepository.findAllById(request.getEventMembersId());

        event.setEventName(request.getEventName());
        event.setEventDescription(request.getEventDescription());
        event.setEventTimeStamp(request.getEventTimeStamp());
        event.setEventLatitude(request.getEventLatitude());
        event.setEventLongitude(request.getEventLongitude());
        event.setEventImageUrl(request.getEventImageUrl());
        event.setEventOrganizerId(request.getEventOrganizerId());
        event.setEventMembersId(eventMembers);

        eventRepository.save(event);

        return new ResponseEntity<>("Event updated successfully", HttpStatus.OK);

    }

    public ResponseEntity<String> deleteEvent(String eventId) {
        // Logic to delete an event
        eventRepository.deleteById(eventId);
        return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
    }

}
