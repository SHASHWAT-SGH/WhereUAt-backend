package com.whereuat_app.whereuat.service;

import com.whereuat_app.whereuat.dto.request.CreateEventRequestDTO;
import com.whereuat_app.whereuat.dto.response.EventDetailsResponseDTO;
import com.whereuat_app.whereuat.dto.response.EventMemberDetailsDTO;
import com.whereuat_app.whereuat.enums.JoinStatus;
import com.whereuat_app.whereuat.model.Event;
import com.whereuat_app.whereuat.model.User;
import com.whereuat_app.whereuat.repository.EventMemberRepository;
import com.whereuat_app.whereuat.repository.EventRepository;
import com.whereuat_app.whereuat.repository.UsersRepository;
import com.whereuat_app.whereuat.model.EventMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository userRepository;
    private final EventMemberRepository eventMemberRepository;

    public EventDetailsResponseDTO createEvent(CreateEventRequestDTO request) {
        // Create and save the event
        Event event = getEvent(request);
        event = eventRepository.save(event);

        // Add organizer as JOINED
        EventMember organizer = new EventMember();
        organizer.setEventId(event.getId());
        organizer.setUserId(request.getEventOrganizerId());
        organizer.setStatus(JoinStatus.JOINED);
        organizer.setJoinedAt(java.time.Instant.now());
        eventMemberRepository.save(organizer);

        // Add other members as PENDING
        for (String memberId : request.getEventMembersId()) {
            if (!memberId.equals(request.getEventOrganizerId())) {
                EventMember member = new EventMember();
                member.setEventId(event.getId());
                member.setUserId(memberId);
                member.setStatus(JoinStatus.PENDING);
                member.setJoinedAt(null);
                eventMemberRepository.save(member);
            }
        }

        // Fetch all event members
        List<EventMember> eventMembers = eventMemberRepository.findByEventId(event.getId());
        List<User> users = userRepository.findAllById(
                eventMembers.stream().map(EventMember::getUserId).toList()
        );

        // Map userId to User for easy lookup
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

        // Build member details
        List<EventMemberDetailsDTO> memberDetails = eventMembers.stream().map(member -> {
            EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
            dto.setUserId(member.getUserId());
            User user = userMap.get(member.getUserId());
            if (user != null) {
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
                dto.setProfileImageUrl(user.getProfileImageUrl());
                dto.setEmail(user.getUserEmail());
            }
            dto.setStatus(member.getStatus());
            dto.setJoinedAt(member.getJoinedAt());
            return dto;
        }).toList();

        // Organizer details
        EventMemberDetailsDTO organizerDetails = memberDetails.stream()
                .filter(m -> m.getUserId().equals(request.getEventOrganizerId()))
                .findFirst().orElse(null);

        // Build response
        EventDetailsResponseDTO response = new EventDetailsResponseDTO();
        response.setEvent(event);
        response.setOrganizer(organizerDetails);
        response.setMembers(memberDetails);

        return response;
    }

    private Event getEvent(CreateEventRequestDTO request) {
        Event event = new Event();
        event.setEventName(request.getEventName());
        event.setEventDescription(request.getEventDescription());
        event.setEventTimeStamp(request.getEventTimeStamp());
        event.setEventImageUrl(request.getEventImageUrl());
        event.setEventOrganizerId(request.getEventOrganizerId());
        // get the latitude and longitude from the request and make it of type GeoJsonPoint
        GeoJsonPoint location = new GeoJsonPoint(request.getEventLongitude(), request.getEventLatitude());
        event.setEventLocation(location);
        return event;
    }

    public ResponseEntity<EventDetailsResponseDTO> getEvent(String eventId) {
        // Logic to get an event
        return eventRepository.findById(eventId)
                .map(event -> {
                    List<EventMember> eventMembers = eventMemberRepository.findByEventId(event.getId());
                    List<User> users = userRepository.findAllById(
                            eventMembers.stream().map(EventMember::getUserId).toList()
                    );

                    // Map userId to User for easy lookup
                    Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

                    // Build member details
                    List<EventMemberDetailsDTO> memberDetails = eventMembers.stream().map(member -> {
                        EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
                        dto.setUserId(member.getUserId());
                        User user = userMap.get(member.getUserId());
                        if (user != null) {
                            dto.setFirstName(user.getFirstName());
                            dto.setLastName(user.getLastName());
                            dto.setProfileImageUrl(user.getProfileImageUrl());
                            dto.setEmail(user.getUserEmail());
                        }
                        dto.setStatus(member.getStatus());
                        dto.setJoinedAt(member.getJoinedAt());
                        return dto;
                    }).toList();

                    // Organizer details
                    EventMemberDetailsDTO organizerDetails = memberDetails.stream()
                            .filter(m -> m.getUserId().equals(event.getEventOrganizerId()))
                            .findFirst().orElse(null);

                    // Build response
                    EventDetailsResponseDTO response = new EventDetailsResponseDTO();
                    response.setEvent(event);
                    response.setOrganizer(organizerDetails);
                    response.setMembers(memberDetails);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> updateEvent(String eventId, CreateEventRequestDTO request) {
        // Logic to update an event
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        Event event = eventOptional.get();

        // Map existing members by userId for easy lookup
//        Map<String, JoinStatus> existingStatusMap = event.getEventMembers().stream()
//                .collect(Collectors.toMap(EventMember::getUserId, EventMember::getStatus));

        List<User> users = userRepository.findAllById(request.getEventMembersId());

        // Build the updated member list, keeping previous status if exists
        List<EventMember> updatedMembers = users.stream().map(user -> {
            EventMember member = new EventMember();
            member.setUserId(user.getId());

//            JoinStatus previousStatus = existingStatusMap.get(user.getId());
//            member.setStatus(previousStatus != null ? previousStatus : JoinStatus.PENDING); // fallback if new member

            return member;
        }).toList();


        event.setEventName(request.getEventName());
        event.setEventDescription(request.getEventDescription());
        event.setEventTimeStamp(request.getEventTimeStamp());
//        event.setEventLatitude(request.getEventLatitude());
//        event.setEventLongitude(request.getEventLongitude());
//        event.setEventImageUrl(request.getEventImageUrl());
//        event.setEventOrganizerId(request.getEventOrganizerId());
//        event.setEventMembers(updatedMembers);

        eventRepository.save(event);

        return new ResponseEntity<>("Event updated successfully", HttpStatus.OK);

    }

    public ResponseEntity<String> deleteEvent(String eventId) {
        // Logic to delete an event
        eventRepository.deleteById(eventId);
        return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<List<EventDetailsResponseDTO>> getEventsByOrganizerId(String organizerId) {
        List<Event> events = eventRepository.getByEventOrganizerId(organizerId);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<EventDetailsResponseDTO> responseList = events.stream().map(event -> {
            List<EventMember> eventMembers = eventMemberRepository.findByEventId(event.getId());
            List<User> users = userRepository.findAllById(
                    eventMembers.stream().map(EventMember::getUserId).toList()
            );
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

            List<EventMemberDetailsDTO> memberDetails = eventMembers.stream().map(member -> {
                EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
                dto.setUserId(member.getUserId());
                User user = userMap.get(member.getUserId());
                if (user != null) {
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setProfileImageUrl(user.getProfileImageUrl());
                    dto.setEmail(user.getUserEmail());
                }
                dto.setStatus(member.getStatus());
                dto.setJoinedAt(member.getJoinedAt());
                return dto;
            }).toList();

            EventMemberDetailsDTO organizerDetails = memberDetails.stream()
                    .filter(m -> m.getUserId().equals(event.getEventOrganizerId()))
                    .findFirst().orElse(null);

            EventDetailsResponseDTO response = new EventDetailsResponseDTO();
            response.setEvent(event);
            response.setOrganizer(organizerDetails);
            response.setMembers(memberDetails);

            return response;
        }).toList();

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public List<EventDetailsResponseDTO> getEventsByOrganizerIdAndJoinStatus(String organizerId, JoinStatus joinStatus) {
        List<Event> events = eventRepository.getByEventOrganizerId(organizerId);
        return events.stream()
                .map(event -> {
                    List<EventMember> eventMembers = eventMemberRepository.findByEventId(event.getId());
                    List<User> users = userRepository.findAllById(
                            eventMembers.stream().map(EventMember::getUserId).toList()
                    );
                    Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

                    // Find organizer's EventMember
                    EventMember organizerMember = eventMembers.stream()
                            .filter(m -> m.getUserId().equals(event.getEventOrganizerId()))
                            .findFirst().orElse(null);

                    // Only include if organizer's status matches
                    if (organizerMember == null || organizerMember.getStatus() != joinStatus) {
                        return null;
                    }

                    // Organizer details
                    EventMemberDetailsDTO organizerDetails = new EventMemberDetailsDTO();
                    organizerDetails.setUserId(organizerMember.getUserId());
                    User organizerUser = userMap.get(organizerMember.getUserId());
                    if (organizerUser != null) {
                        organizerDetails.setFirstName(organizerUser.getFirstName());
                        organizerDetails.setLastName(organizerUser.getLastName());
                        organizerDetails.setProfileImageUrl(organizerUser.getProfileImageUrl());
                        organizerDetails.setEmail(organizerUser.getUserEmail());
                    }
                    organizerDetails.setStatus(organizerMember.getStatus());
                    organizerDetails.setJoinedAt(organizerMember.getJoinedAt());

                    // All members
                    List<EventMemberDetailsDTO> memberDetails = eventMembers.stream()
                            .map(member -> {
                                EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
                                dto.setUserId(member.getUserId());
                                User user = userMap.get(member.getUserId());
                                if (user != null) {
                                    dto.setFirstName(user.getFirstName());
                                    dto.setLastName(user.getLastName());
                                    dto.setProfileImageUrl(user.getProfileImageUrl());
                                    dto.setEmail(user.getUserEmail());
                                }
                                dto.setStatus(member.getStatus());
                                dto.setJoinedAt(member.getJoinedAt());
                                return dto;
                            }).toList();

                    EventDetailsResponseDTO response = new EventDetailsResponseDTO();
                    response.setEvent(event);
                    response.setOrganizer(organizerDetails);
                    response.setMembers(memberDetails);

                    return response;
                })
                .filter(dto -> dto != null)
                .toList();
    }

    public List<EventDetailsResponseDTO> getEventsByMemberId(String memberId) {
        List<EventMember> memberEventLinks = eventMemberRepository.findByUserId(memberId);
        if (memberEventLinks.isEmpty()) {
            return List.of();
        }

        List<String> eventIds = memberEventLinks.stream()
                .map(EventMember::getEventId)
                .distinct()
                .toList();

        List<Event> events = eventRepository.findAllById(eventIds);

        return events.stream().map(event -> {
            // Fetch all members for this event
            List<EventMember> allEventMembers = eventMemberRepository.findByEventId(event.getId());
            List<User> users = userRepository.findAllById(
                    allEventMembers.stream().map(EventMember::getUserId).toList()
            );
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

            List<EventMemberDetailsDTO> memberDetails = allEventMembers.stream()
                    .map(member -> {
                        EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
                        dto.setUserId(member.getUserId());
                        User user = userMap.get(member.getUserId());
                        if (user != null) {
                            dto.setFirstName(user.getFirstName());
                            dto.setLastName(user.getLastName());
                            dto.setProfileImageUrl(user.getProfileImageUrl());
                            dto.setEmail(user.getUserEmail());
                        }
                        dto.setStatus(member.getStatus());
                        dto.setJoinedAt(member.getJoinedAt());
                        return dto;
                    }).toList();

            // Organizer details
            EventMemberDetailsDTO organizerDetails = memberDetails.stream()
                    .filter(m -> m.getUserId().equals(event.getEventOrganizerId()))
                    .findFirst().orElse(null);

            EventDetailsResponseDTO response = new EventDetailsResponseDTO();
            response.setEvent(event);
            response.setOrganizer(organizerDetails);
            response.setMembers(memberDetails);

            return response;
        }).toList();
    }

    public List<EventDetailsResponseDTO> getEventsByMemberIdAndJoinStatus(String memberId, JoinStatus joinStatus) {
        List<EventMember> memberEventLinks = eventMemberRepository.findByUserIdAndStatus(memberId, joinStatus);
        if (memberEventLinks.isEmpty()) {
            return List.of();
        }

        List<String> eventIds = memberEventLinks.stream()
                .map(EventMember::getEventId)
                .distinct()
                .toList();

        List<Event> events = eventRepository.findAllById(eventIds);

        return events.stream().map(event -> {
            // Fetch all members for this event
            List<EventMember> allEventMembers = eventMemberRepository.findByEventId(event.getId());
            List<User> users = userRepository.findAllById(
                    allEventMembers.stream().map(EventMember::getUserId).toList()
            );
            Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

            List<EventMemberDetailsDTO> memberDetails = allEventMembers.stream()
                    .map(member -> {
                        EventMemberDetailsDTO dto = new EventMemberDetailsDTO();
                        dto.setUserId(member.getUserId());
                        User user = userMap.get(member.getUserId());
                        if (user != null) {
                            dto.setFirstName(user.getFirstName());
                            dto.setLastName(user.getLastName());
                            dto.setProfileImageUrl(user.getProfileImageUrl());
                            dto.setEmail(user.getUserEmail());
                        }
                        dto.setStatus(member.getStatus());
                        dto.setJoinedAt(member.getJoinedAt());
                        return dto;
                    }).toList();

            // Organizer details
            EventMemberDetailsDTO organizerDetails = memberDetails.stream()
                    .filter(m -> m.getUserId().equals(event.getEventOrganizerId()))
                    .findFirst().orElse(null);

            EventDetailsResponseDTO response = new EventDetailsResponseDTO();
            response.setEvent(event);
            response.setOrganizer(organizerDetails);
            response.setMembers(memberDetails);

            return response;
        }).toList();
    }

    public ResponseEntity<?> joinEvent(String userId, String eventId) {
        Optional<EventMember> existingMember = eventMemberRepository.findByEventIdAndUserId(eventId, userId);

        if (existingMember.isPresent()) {
            EventMember member = existingMember.get();
            member.setStatus(JoinStatus.JOINED);
            member.setJoinedAt(java.time.Instant.now());
            eventMemberRepository.save(member);
            return new ResponseEntity<>("User status updated to JOINED", HttpStatus.OK);
        } else {
            EventMember newMember = new EventMember();
            newMember.setEventId(eventId);
            newMember.setUserId(userId);
            newMember.setStatus(JoinStatus.JOINED);
            newMember.setJoinedAt(java.time.Instant.now());
            eventMemberRepository.save(newMember);
            return new ResponseEntity<>("User added as member and marked as JOINED", HttpStatus.OK);
        }
    }

}
