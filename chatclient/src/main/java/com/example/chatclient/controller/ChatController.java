package com.example.chatclient.controller;


import com.example.chatclient.model.Message;
import com.example.chatclient.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final WebSocketService webSocketService;

    @GetMapping("/public")
    public ResponseEntity broadcastMessage(@RequestParam String message) {
        StompSession stompSession = this.webSocketService.getStompSession();

        stompSession.send("/app/public", Message.builder().from("General Kenobi").content(message).build());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/private")
    public ResponseEntity privateMessage(@RequestParam String message) {
        StompSession stompSession = this.webSocketService.getStompSession();

        stompSession.send("/app/private", Message.builder().from("Private Message").content(message).build());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/private/{sessionid}")
    public ResponseEntity privateMessageWithSessionId(@RequestParam String message, @PathVariable String sessionid){
        StompSession stompSession = this.webSocketService.getStompSession();

        stompSession.send("/app/private/" + sessionid, Message.builder().from("Private Message").content(message).build());
        return ResponseEntity.ok(null);
    }
}
