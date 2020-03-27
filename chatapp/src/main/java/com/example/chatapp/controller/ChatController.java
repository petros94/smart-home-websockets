package com.example.chatapp.controller;

import com.example.chatapp.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/public")
    @SendTo("/topic/messages/outgoing")
    public Message send(Message message) throws Exception {
        return Message.builder()
                .from(message.getFrom())
                .content(message.getContent())
                .build();
    }

    @PostMapping("/server/private/{username}")
    public ResponseEntity<String> send(@RequestBody Message message, @PathVariable String username) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/payment", message, headerAccessor.getMessageHeaders());
        return ok("sent");
    }

}
