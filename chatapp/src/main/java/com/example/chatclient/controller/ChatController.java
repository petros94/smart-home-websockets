package com.example.chatclient.controller;

import com.example.chatclient.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DiscoveryClient discoveryClient;

    @MessageMapping("/public")
    @SendTo("/topic/messages/outgoing")
    public Message send(Message message) throws Exception {
        return Message.builder()
                .from(message.getFrom())
                .content(message.getContent())
                .build();
    }

    @MessageMapping("/private")
    @SendToUser("/queue/payment")
    public Message sendToUser(Message message, MessageHeaderAccessor messageHeaderAccessor) throws Exception {
        return Message.builder()
                .from(message.getFrom())
                .content(message.getContent())
                .build();
    }

    @MessageMapping("/private/{sessionid}")
    public void sendToUser(Message message, @DestinationVariable String sessionid) throws Exception {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionid);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(sessionid, "/queue/payment", message, headerAccessor.getMessageHeaders());
    }

    @PostMapping("/private/{sessionid}")
    public void send(@RequestBody Message message, @PathVariable String sessionid) throws Exception {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionid);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(sessionid, "/queue/payment", message, headerAccessor.getMessageHeaders());
    }
}
