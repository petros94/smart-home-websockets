package com.pmitseas.deviceclient;

import com.pmitseas.deviceclient.model.CommandMessage;
import com.pmitseas.deviceclient.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class StompSessionHandlerImpl implements StompSessionHandler {

    private WebSocketService webSocketService;
    private AtomicBoolean isConnected;

    public StompSessionHandlerImpl() {
        this.isConnected = new AtomicBoolean();
    }

    @Autowired
    @Lazy
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.isConnected.set(true);
        session.subscribe("/topic/messages/outgoing", this);
        session.subscribe("/user/queue/device", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        if (this.isConnected.get()) {
            this.isConnected.set(false);
            webSocketService.initSession("lights_living_room", "password");
        }
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return CommandMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (payload == null) return;
        CommandMessage msg = (CommandMessage) payload;
        log.info("Received: " + msg.getCommand()+ ", time: " + msg.getTime());
        webSocketService.handleMessage(msg);
    }

}
