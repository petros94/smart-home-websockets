package com.example.chatclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${broker.relay.host}")
    private String brokerRelayHost;

    @Value("${broker.relay.user}")
    private String brokerRelayUser;

    @Value("${broker.relay.password}")
    private String brokerRelayPass;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost(brokerRelayHost)
                .setClientLogin(brokerRelayUser)
                .setClientPasscode(brokerRelayPass)
                .setSystemLogin(brokerRelayUser)
                .setSystemPasscode(brokerRelayPass);
        config.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters){
//        messageConverters.add(new MappingJackson2MessageConverter());
//        return true;
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*");
        registry.addEndpoint("/websocket").withSockJS();
    }
}
