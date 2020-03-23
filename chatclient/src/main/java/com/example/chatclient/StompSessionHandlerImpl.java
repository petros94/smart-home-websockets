package com.example.chatclient;

import com.example.chatclient.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@Component
public class StompSessionHandlerImpl implements StompSessionHandler {

    @Override
    public void afterConnected(
            StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/topic/messages/outgoing", this);
            session.subscribe("/user/queue/payment", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        log.info("Received : " + msg.getContent()+ " from : " + msg.getFrom());
    }
}
