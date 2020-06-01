package com.pmitseas.devicemgmt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

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
                .setSystemPasscode(brokerRelayPass)
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*");
        registry.addEndpoint("/websocket").withSockJS();
    }

	@Bean // experimental
	public DefaultSimpUserRegistry getDefaultSimpRegistry() {
		return new DefaultSimpUserRegistry();
	}

}
