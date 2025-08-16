package com.whereuat_app.whereuat.dto.response;

import com.whereuat_app.whereuat.model.Event;
import lombok.Data;

import java.util.List;

@Data
public class EventDetailsResponseDTO {
    private Event event;
    private EventMemberDetailsDTO organizer;
    private List<EventMemberDetailsDTO> members;
}
