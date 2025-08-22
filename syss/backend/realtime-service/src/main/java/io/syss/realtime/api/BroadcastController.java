package io.syss.realtime.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class BroadcastController {
    private final SimpMessagingTemplate messagingTemplate;

    public BroadcastController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/ops/{id}/positions")
    public void positions(@DestinationVariable String id, Map<String, Object> payload) {
        messagingTemplate.convertAndSend("/topic/ops/" + id + "/positions", payload);
    }

    @MessageMapping("/ops/{id}/tasks")
    public void tasks(@DestinationVariable String id, Map<String, Object> payload) {
        messagingTemplate.convertAndSend("/topic/ops/" + id + "/tasks", payload);
    }

    @MessageMapping("/ops/{id}/messages")
    public void messages(@DestinationVariable String id, Map<String, Object> payload) {
        messagingTemplate.convertAndSend("/topic/ops/" + id + "/messages", payload);
    }
}