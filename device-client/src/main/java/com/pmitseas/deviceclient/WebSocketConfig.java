package com.pmitseas.deviceclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.tcp.TcpConnection;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSocketConfig {

    @Retryable(value = {Exception.class},
            maxAttempts = 10,
            backoff = @Backoff(delay=1000, multiplier = 2, random = true))
    public StompSession initSession(String username, String password) throws Exception {
        String plainCredentials = username + ":" + password;
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

        final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);

        return enableSSL ?
                stompClient().connect(secureServerURL, headers, sessionHandler).get() :
                stompClient().connect(serverURL, headers, sessionHandler).get();
    }

    @Value("${ws.server.host}")
    private String serverURL;

    @Value("${ws.server.secureHost}")
    private String secureServerURL;

    @Value("${ws.server.enableSSL}")
    private boolean enableSSL;

    private final StompSessionHandler sessionHandler;

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        List<Transport> transports = List.of(new WebSocketTransport(simpleWebSocketClient));
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        stompClient.setTaskScheduler(scheduler);
        return stompClient;
    }
}
