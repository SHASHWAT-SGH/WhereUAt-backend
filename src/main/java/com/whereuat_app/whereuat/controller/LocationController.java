package com.whereuat_app.whereuat.controller;

import com.whereuat_app.whereuat.dto.socket.LocationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/location.update")
    public void updateLocation(@Payload LocationMessage locationMessage) {
        // Forward updated location to all members subscribed to the event
        String eventId = locationMessage.getEventId();
        messagingTemplate.convertAndSend("/location/" + eventId, locationMessage);
    }
}

