package com.pmitseas.deviceclient.service;

import com.pmitseas.deviceclient.WebSocketConfig;
import com.pmitseas.deviceclient.model.CommandMessage;
import com.pmitseas.deviceclient.model.ResponseMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@Getter
public class WebSocketService {

    private StompSession stompSession;
    private WebSocketConfig webSocketConfig;

    public WebSocketService(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
        this.initSession("lights_living_room", "password");
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

    public void handleMessage(CommandMessage message){
        ResponseMessage response = ResponseMessage.builder()
                .id(message.getId())
                .time(LocalDateTime.now().toString())
                .status("OK")
                .data("random data".getBytes())
                .build();

        stompSession.send("/app/device", response);
    }
}
