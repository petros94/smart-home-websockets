package com.example.chatclient.service;

import com.example.chatclient.WebSocketConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Getter
public class WebSocketService {

    private StompSession stompSession;
    private WebSocketConfig webSocketConfig;

    public WebSocketService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
        this.initSession("someone", "pass");
    }

    public void initSession(String username, String password) {
        log.info("Initializing websocket session");
        try {
            this.stompSession = this.webSocketConfig.initSession(username, password);
        } catch (Exception e) {
            log.info("Create stomp session failed ", e);
            this.stompSession = null;
        }
    }
}
