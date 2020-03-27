package com.example.chatclient.advisor;

import com.example.chatclient.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ChatclientExceptionHandler {

    private final WebSocketService webSocketService;

    @ExceptionHandler(ConnectionLostException.class)
    public void handleConnectionLost(ConnectionLostException ex) {
        log.info("Lost connection to server. Retrying...", ex);
        webSocketService.initSession("someone", "pass");
    }
}
